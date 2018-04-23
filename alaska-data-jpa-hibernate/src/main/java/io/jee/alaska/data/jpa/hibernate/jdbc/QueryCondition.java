package io.jee.alaska.data.jpa.hibernate.jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.jee.alaska.data.jpa.hibernate.utils.SearchFilter;

class QueryCondition {

	final static String COUNT_QUERY_STRING = " select count(1) from (%s) x ";

	final static String selectKey = "SELECT ";
	final static String fromKey = " FROM ";
	final static String asKey = " AS ";
	final static String splitKey = ".";
	final static String spaceKey = " ";
	final static String asteriskKey = "*";

	final static String countKey = " COUNT(1) ";

	final static String GROUP_BY_KEY = " GROUP BY ";
	final static String whereReplaceKey = "${WHERE}";
	final static String whereKey = " WHERE ";
	final static String andKey = " AND ";
	final static char[] brackets = { '(', ')' };

	public final String sql;
	public final Object[] params;

	QueryCondition(String sql, Object[] params) {
		this.sql = sql;
		this.params = params;
	}

	public static QueryCondition createResultQuery(String sql, Map<String, Object> searchMap) {
		return createResultQuery(sql, searchMap, null);
	}

	/**
	 * Title:获取参数SQL中第一个 select 、 from 两个关键字之间列名，将列别名和真实列名取出来，放入Map中
	 * 如果用户查询条件字段为user_name，则最终的查询条件字段根据4种情况处理： 1、取AS关键字前面的 2、取最后一个空格前面的字段串
	 * 3、没有取列别名，如果是a.*或a.user_name,则取a.user_name
	 * 4、没有取列别名也没有表别名，如果user_name，则取user_name 示例： select
	 * 原查询的字段-------------------------key(表别名)-----value(真实列名，拼到where和order by后面的列名)
	 * a.user_name as user_name1----------user_name1----a.user_name（情况1,通过AS分割）
	 * a.user_name user_name2-------------user_name2----a.user_name（情况2,通过一个空格分割）
	 * case when ...... end user_name3----user_name3----case when ......
	 * end（情况2,通过一个空格分割）
	 * a.user_name4-----------------------user_name4----a.user_name4（情况3，通过关键字"."来分割）
	 * a.*--------------------------------*-------------a（情况3，通过关键字"."来分割）
	 * *------------------------------------------------（只有*的这种情况舍弃，不放到map中）
	 * user_name5-------------------------user_name-----user_name（情况4，不分割，key和value一样）
	 * from user_info as a
	 * 
	 * @author yuwu on 2016年8月18日
	 * @param sql
	 * @return HashMap{key:列别名,value:真实列名}
	 */
	public static HashMap<String, String> getColumnsMap(String sql) {
		String upperSql = sql.toUpperCase().replaceAll("	", " ");
		int seleteIndex = upperSql.indexOf(selectKey);
		int fromIndex = upperSql.indexOf(fromKey);
		// 获取列
		String columnStr = upperSql.substring(seleteIndex + selectKey.length(), fromIndex);
		String columns[] = columnStr.split(",");
		// 保存列别名及真实列名关系{key:列别名,value:真实列名}
		HashMap<String, String> columnsMap = Maps.newHashMap();
		String aliasColumnKey = null;// 列别名
		String trueColumnValue = null;// 真实列名
		for (String column : columns) {
			String tempColumn = column.trim();
			if (tempColumn.indexOf(asKey) != -1) {// 判断列有没有通过AS关键字取别名，例如：b.reply_count AS total_reply_count
				String columnsRename[] = tempColumn.split(asKey);
				aliasColumnKey = columnsRename[1].trim();
				trueColumnValue = columnsRename[0].trim();
			} else if (tempColumn.lastIndexOf(spaceKey) != -1) {// 判断列有没有通过空格键关键字取别名，例如：b.reply_count total_reply_count
				// case b.sex when 1 then 10 when 2 then 20 when 0 then 30 end sexTemp
				int lastIndex = tempColumn.lastIndexOf(spaceKey);
				aliasColumnKey = tempColumn.substring(lastIndex + spaceKey.length()).trim();
				trueColumnValue = tempColumn.substring(0, lastIndex).trim();
			} else if (tempColumn.indexOf(splitKey) != -1) {// 查询字段没有别名的情况，例如：b.img_url，则columnKey=img_url，tableNameValue=b
				int index = tempColumn.indexOf(splitKey);
				aliasColumnKey = tempColumn.substring(index + splitKey.length()).trim();
				if (asteriskKey.equals(aliasColumnKey)) {// 如果是A.*,则取tableNameValue=A,否则取tableNameValue=tempColumn
					trueColumnValue = tempColumn.substring(0, index);
				} else {
					trueColumnValue = tempColumn.trim();
				}
			} else {// 没有表别名也没有列别名的情况，例如：img_url
				aliasColumnKey = tempColumn;
				trueColumnValue = tempColumn;
			}
			// 如果tempColumn=tempColumn=*，则不添加到列名映射表中去
			if (!asteriskKey.equals(tempColumn)) {
				columnsMap.put(aliasColumnKey, trueColumnValue);
			}
		}
		return columnsMap;
	}

	/**
	 * Title:根据旧的列名获取新的列名
	 * 
	 * @author yuwu on 2016年9月6日
	 * @param columnsMap
	 * @param oldColumnName
	 * @return
	 */
	public static String getNewColumnName(HashMap<String, String> columnsMap, String oldColumnName) {
		String tableName1 = columnsMap.get(asteriskKey);// 表别名。例如查询字段为A.*,则asteriskKey=*，tableName1=A
		String columnName = columnsMap.get(oldColumnName.toUpperCase());// 必须转成大写，能从map中到新值
		String newColumnName = null;
		if (StringUtils.isNotBlank(columnName)) {
			newColumnName = columnName;
		} else if (StringUtils.isNotBlank(tableName1)) {
			// 表别名tableName1变成小写，不然排序字段里面要将驼峰转成下划线
			newColumnName = tableName1 + "." + oldColumnName;
		} else {
			// 使用原来的值
			newColumnName = oldColumnName;
		}
		return newColumnName;
	}

	public static QueryCondition createResultQuery(String sql, Map<String, Object> searchMap,
			Map<String, Boolean> orderMap) {
		// String countQuery = String.format(QUERY_STRING, sql);
		HashMap<String, String> columnsMap = getColumnsMap(sql);
		return createQuery(columnsMap, sql, searchMap, orderMap);
	}

	public static QueryCondition createResultQuery(String sql, Map<String, Object> searchMap,
			Map<String, Boolean> orderMap, HashMap<String, String> columnsMap) {
		// String countQuery = String.format(QUERY_STRING, sql);
		return createQuery(columnsMap, sql, searchMap, orderMap);
	}

	/**
	 * 处理统计SQL 处理前SQL：select * from user_info t1 处理后SQL：select count(1) from
	 * user_info t1
	 * 
	 * @author yuwu on 2016年8月18日
	 * @param sql
	 * @return
	 */
	public static String processCountSql(String sql) {
		StringBuilder countQuery = new StringBuilder();
		String upperSql = sql.toUpperCase();
		int seleteIndex = upperSql.indexOf(selectKey);
		int fromIndex = upperSql.indexOf(fromKey);
		countQuery.append(sql.substring(0, seleteIndex + selectKey.length()));
		countQuery.append(countKey);
		countQuery.append(sql.substring(fromIndex));
		return countQuery.toString();
	}

	public static QueryCondition createCountQuery(String sql, Object... objects) {
		// String countQuery = String.format(COUNT_QUERY_STRING, sql);
		String countQuery = processCountSql(sql);
		return new QueryCondition(countQuery, objects);
	}

	public static QueryCondition createCountQuery(String sql, Map<String, Object> searchMap) {
		// String countQuery = String.format(COUNT_QUERY_STRING2, sql);
		String countQuery = processCountSql(sql);
		// 获取查询SQL中的列名及别名
		HashMap<String, String> columnsMap = getColumnsMap(sql);
		return createQuery(columnsMap, countQuery, searchMap);
	}

	public static QueryCondition createQuery(HashMap<String, String> columnsMap, String sql, Map<String, Object> searchMap) {
		return createQuery(columnsMap, sql, searchMap, null);
	}

	private static QueryCondition createQuery(HashMap<String, String> columnsMap, String sql, Map<String, Object> searchMap, Map<String, Boolean> orderMap) {
		StringBuilder querysql = new StringBuilder(sql);

		List<SearchFilter> filters = SearchFilter.parse(searchMap);
		List<Object> params = Collections.emptyList();
		if (filters != null && !filters.isEmpty()) {
			params = new ArrayList<Object>();
			// 把查询条件字段加上别名
			filters = processSearchFilter(filters, columnsMap);
			filterCondions(filters, querysql, params);
		} else {
			// 删除SQL中的${WHERE}关键字
			int whereIndex = querysql.toString().toUpperCase().indexOf(whereReplaceKey);
			if (whereIndex != -1) {
				querysql.delete(whereIndex, whereIndex + whereReplaceKey.length());
			}
		}

		if (orderMap != null && !orderMap.isEmpty()) {
			// 把排序条件字段加上别名
			orderMap = processOrderBy(orderMap, columnsMap);
			orderBy(querysql, orderMap);
		}

		return new QueryCondition(querysql.toString(), params.toArray());
	}

	/**
	 * Title:替换查询条件中列别名为真实列名
	 * 
	 * @author yuwu on 2016年8月18日
	 * @param filters
	 * @param columnsMap
	 * @return
	 */
	public static List<SearchFilter> processSearchFilter(List<SearchFilter> filters, HashMap<String, String> columnsMap) {
		List<SearchFilter> list = Lists.newArrayList();
		for (SearchFilter searchFilter : filters) {
			String oldColumnName = searchFilter.originalFieldName.toUpperCase();
			// 获取新列名
			String newColumnName = QueryCondition.getNewColumnName(columnsMap, oldColumnName);
			// 设置新列名
			searchFilter.originalFieldName = newColumnName;
			list.add(searchFilter);
		}
		return list;
	}

	/**
	 * Title:替换order by中的列别名为真实列名
	 * 
	 * @author yuwu on 2016年8月18日
	 * @param filters
	 * @param columnsMap
	 * @return
	 */
	public static Map<String, Boolean> processOrderBy(Map<String, Boolean> orderMap, HashMap<String, String> columnsMap) {
		Map<String, Boolean> orderMapNew = Maps.newLinkedHashMap();
		for (Entry<String, Boolean> entry : orderMap.entrySet()) {
			// 示例数据：sortMap.put("registTime", false);
			// 取出原排序字段value:false
			Boolean oldValue = entry.getValue();
			// 取出原排序字段KEY:registTime
			String oldColumnName = entry.getKey();
			// 排序字段转成：先转成regist_time(驼峰字母变小写，前面加下划线)，再全转成大写：REGIST_TIME
			oldColumnName = SearchFilter.coverdColumName(oldColumnName).toUpperCase();
			// 取出新列名
			String newColumnName = QueryCondition.getNewColumnName(columnsMap, oldColumnName);
			orderMapNew.put(newColumnName.toLowerCase(), oldValue);
		}
		return orderMapNew;
	}

	/**
	 * Title:添加查询过滤条件
	 * 
	 * @author yuwu on 2016年8月26日
	 * @param filters
	 * @param querysql
	 * @param params
	 */
	private static void filterCondions(List<SearchFilter> filters, StringBuilder querysql, Collection<Object> params) {
		StringBuilder searchFilterSql = new StringBuilder();
		filterCondions2(filters, searchFilterSql, params);
		concatSearchFilterSql(querysql, searchFilterSql);
	}

	/**
	 * Title:将查询条件原入到原SQL中
	 * 
	 * @author yuwu on 2016年9月6日
	 * @param querysql，原查询SQL
	 * @param searchFilterSql，添加的条件SQL
	 */
	public static void concatSearchFilterSql(StringBuilder querysql, StringBuilder searchFilterSql) {
		if (searchFilterSql.length() == 0) {
			return;
		}
		// 假定filters的包含的查询条件为name。
		String upperQuerysql = querysql.toString().toUpperCase();
		// 获取${WHERE}关键字的位置
		int whereReplaceKeyIndex = upperQuerysql.indexOf(whereReplaceKey);
		if (whereReplaceKeyIndex != -1) {
			// 截取${WHERE}关键字之前的SQL
			String subString = upperQuerysql.substring(0, whereReplaceKeyIndex);
			// 不包含WHERE关键字或where关键不在最外层，则需要加where关键字
			if (!isOutermostWhere(subString)) {
				// 原SQL：select * from app_info t1 ${WHERE} order by t1.id desc
				// 替换后：select * from app_info t1 where name = ? order by t1.id desc

				// 删除查询条件中的第一个AND关键字
				searchFilterSql.delete(0, andKey.length());
				// 添加WHERE查询关键字
				searchFilterSql.insert(0, whereKey);
			}
			querysql.replace(whereReplaceKeyIndex, whereReplaceKeyIndex + whereReplaceKey.length(),
					searchFilterSql.toString());
		} else {
			// 不包含WHERE关键字或where关键不在最外层，则需要加where关键字
			if (!isOutermostWhere(upperQuerysql)) {
				// 删除查询条件中的第一个AND关键字
				searchFilterSql.delete(0, andKey.length());
				// 添加WHERE查询关键字
				searchFilterSql.insert(0, whereKey);
			}
			querysql.append(searchFilterSql);
		}
	}

	/**
	 * Title:判断where是否最外层的where,如果是最外层的，则后面直接跟and和条件，否则后面跟where关键字后再加条件
	 * 判断的逻辑是：where关键字后面的“(”和“)”的个数一致，则是最外层where
	 * 
	 * @author yuwu on 2016年9月5日
	 * @param querysql
	 * @return {true:最外层where条件,false:无where关键字或非最外层where条件}
	 */
	private static boolean isOutermostWhere(String querysql) {
		int whereIndex = 0;
		// 从第一个where开始匹配，如果是最外层则返回，否则匹配到最后一个where
		// 这种循环主要是处理where条件中包含exists（exists中又包含where这种情况）这种情况
		while ((whereIndex = querysql.indexOf(whereKey, whereIndex)) != -1) {
			CharSequence whereStr = querysql.subSequence(whereIndex + whereKey.length(), querysql.length());
			int count = 0;
			for (int i = 0; i < whereStr.length(); i++) {
				char ch = whereStr.charAt(i);
				if (ch == brackets[0]) {
					count++;
				} else if (ch == brackets[1]) {
					count--;
				}
			}
			if (count == 0) {
				return true;
			} else {
				// 第一次indexOf从0开始查询where关键字，之后的每一次都是从上一次的whereIndex +
				// whereKey.length()处开始查询where关键字
				whereIndex = whereIndex + whereKey.length();
			}
		}
		return false;
	}

	/**
	 * Title:判断最后一个GROUP BY后面有没有非成对出现的“(”，“)”，如果有非成对出现，则返回true，其它返回false(包括没有找到GROUP
	 * BY关键字)
	 * 
	 * @author yuwu on 2017年1月8日
	 * @param querysql
	 * @return {true:最后一个GROUP BY后面的括号是成对出现的,false:无where关键字或最后一个GROUP
	 *         BY后面的括号不是成对出现的}
	 */
	public static boolean isOutermostGroupBy(String querysql) {
		int whereIndex = 0;
		if ((whereIndex = querysql.lastIndexOf(GROUP_BY_KEY)) == -1) {
			return false;
		}

		CharSequence whereStr = querysql.subSequence(whereIndex + GROUP_BY_KEY.length(), querysql.length());
		int count = 0;
		for (int i = 0; i < whereStr.length(); i++) {
			char ch = whereStr.charAt(i);
			if (ch == brackets[0]) {
				count++;
			} else if (ch == brackets[1]) {
				count--;
			}
		}
		if (count == 0) {
			return true;
		}
		return false;
	}

	private static void filterCondions2(List<SearchFilter> filters, StringBuilder querysql, Collection<Object> params) {
		for (SearchFilter filter : filters) {
			switch (filter.operator) {
			case EQ:
				querysql.append(" AND ").append(filter.originalFieldName).append(" = ? ");
				params.add(filter.value);
				break;
			case NOTEQ:
				querysql.append(" AND ").append(filter.originalFieldName).append(" <> ? ");
				params.add(filter.value);
				break;
			case LIKE:
				querysql.append(" AND ").append(filter.originalFieldName).append(" like ? ");
				params.add("%" + filter.value + "%");
				break;
			case LLIKE:
				querysql.append(" AND ").append(filter.originalFieldName).append(" like ? ");
				params.add("%" + filter.value);
				break;
			case RLIKE:
				querysql.append(" AND ").append(filter.originalFieldName).append(" LIKE ? ");
				params.add(filter.value + "%");
				break;
			case NLIKE:
				querysql.append(" AND ").append(filter.originalFieldName).append(" NOT LIKE ? ");
				params.add(filter.value + "%");
				break;
			case GT:
				querysql.append(" AND ").append(filter.originalFieldName).append(" > ? ");
				params.add(filter.value);
				break;
			case LT:
				querysql.append(" AND ").append(filter.originalFieldName).append(" < ? ");
				params.add(filter.value);
				break;
			case GTE:
				querysql.append(" AND ").append(filter.originalFieldName).append(" >= ? ");
				params.add(filter.value);
				break;
			case LTE:
				querysql.append(" AND ").append(filter.originalFieldName).append(" <= ? ");
				params.add(filter.value);
				break;
			case NULL:
				querysql.append(" AND ").append(filter.originalFieldName).append(" IS NULL ");
				break;
			case NOTNULL:
				querysql.append(" AND ").append(filter.originalFieldName).append(" IS NOT NULL ");
				break;
			case IN:
				if (filter.value instanceof Collection) {
					querysql.append(" AND ").append(filter.originalFieldName).append(" IN ( ");
					Collection<?> cs = (Collection<?>) filter.value;
					for (int i = 0; cs != null && i < cs.size(); i++) {
						querysql.append(" ? ");
						if (i + 1 < cs.size()) {
							querysql.append(",");
						}
					}
					querysql.append(" ) ");
					params.addAll(cs);
				} else if (filter.value instanceof Object[]) {
					querysql.append(" AND ").append(filter.originalFieldName).append(" IN ( ");
					Object[] cs = (Object[]) filter.value;
					for (int i = 0; cs != null && i < cs.length; i++) {
						querysql.append(" ? ");
						if (i + 1 < cs.length) {
							querysql.append(",");
						}
					}
					querysql.append(" ) ");
					params.addAll(Arrays.asList(cs));
				} else {
					querysql.append(" AND ").append(filter.originalFieldName).append(" IN ( ? ) ");
					params.add(filter.value);
				}
				break;
			case NOTIN:
				if (filter.value instanceof Collection) {
					querysql.append(" AND ").append(filter.originalFieldName).append(" NOT IN ( ");
					Collection<?> cs = (Collection<?>) filter.value;
					for (int i = 0; cs != null && i < cs.size(); i++) {
						querysql.append(" ? ");
						if (i + 1 < cs.size()) {
							querysql.append(",");
						}
					}
					querysql.append(" ) ");
					params.addAll(cs);
				} else if (filter.value instanceof Object[]) {
					querysql.append(" AND ").append(filter.originalFieldName).append(" NOT IN ( ");
					Object[] cs = (Object[]) filter.value;
					for (int i = 0; cs != null && i < cs.length; i++) {
						querysql.append(" ? ");
						if (i + 1 < cs.length) {
							querysql.append(",");
						}
					}
					querysql.append(" ) ");
					params.addAll(Arrays.asList(cs));
				} else {
					querysql.append(" AND ").append(filter.originalFieldName).append(" NOT IN ( ? ) ");
					params.add(filter.value);
				}
				break;
			default:
				throw new NullPointerException("查询方式未定义");
			}
		}
	}

	/**
	 * 排序
	 * 
	 * @param sql
	 * @param orderMap
	 */
	static void orderBy(StringBuilder querysql, Map<String, Boolean> orderMap) {
		querysql.append(" order by ");
		int i = 1;
		for (String key : orderMap.keySet()) {
			boolean orders = orderMap.get(key);
			// key = SearchFilter.coverdColumName(key);
			querysql.append(key).append(orders ? " asc " : " desc ");
			if (i < orderMap.size()) {
				querysql.append(",");
			}
			i++;
		}
	}

	/**
	 * 去除select 子句，未考虑union的情况
	 */
	static String removeSelect(String hql) {
		Assert.hasText(hql, "定义SQL语句为空");
		int beginPos = hql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
		return hql.substring(beginPos);
	}

	/**
	 * 去除orderby 子句
	 */
	static String removeOrders(String hql) {
		Assert.hasText(hql, "定义SQL语句为空");
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(hql);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}
}
