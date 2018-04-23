package io.jee.alaska.data.jpa.hibernate.utils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Lists;

import io.jee.alaska.exception.BusinessException;

public class SearchFilter {
	public enum Operator {
		/**
		 * Equals
		 */
		EQ,
		/**
		 * NOT Equals
		 */
		NOTEQ,
		/**
		 * Like
		 **/
		LIKE,
		/**
		 * LEFT Like
		 **/
		LLIKE,
		/**
		 * RIGHT Like
		 **/
		RLIKE,
		/**
		 * NOT Like
		 **/
		NLIKE,
		/**
		 * GREAT
		 **/
		GT,
		/**
		 * LESS
		 **/
		LT,
		/**
		 * GREAT Equals
		 */
		GTE,
		/**
		 * LESS Equals
		 */
		LTE,
		/**
		 * IN
		 */
		IN,
		/**
		 * NOT IN
		 */
		NOTIN,
		/**
		 * IS NULL
		 */
		NULL,
		/**
		 * IS NOT NULL
		 */
		NOTNULL;

		public static Operator getOpeartor(String name) {
			for (Operator op : Operator.values()) {
				if (op.toString().equals(name)) {
					return op;
				}
			}
			return null;
		}
	}

	public final String fieldName;
	public String originalFieldName;
	public Object value;
	public final Object originalValue;
	public final Operator operator;

	public SearchFilter(String fieldName, String originalFieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.originalFieldName = originalFieldName;
		this.value = value;
		this.originalValue = value;
		this.operator = operator;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

	public static List<SearchFilter> parse(Map<String, Object> searchParams) {
		List<SearchFilter> filters = Lists.newArrayList();
		if (searchParams == null) {
			return Collections.emptyList();
		}
		for (Entry<String, Object> entry : searchParams.entrySet()) {
			String key = entry.getKey();
			if (!StringUtils.contains(key, '_')) {
				throw new BusinessException("查询条件不符合规范");
			}
			// int index = StringUtils.indexOfAny(key, "_");
			// String[] names = StringUtils.split(key, "_", (index + 1));
			// 将参数改为2，则names最多只有两个元素，是为了参数可以输入列名，而非必须是驼峰形式的属性名。
			// 例如：如果之前参数必须为EQ_fullName如果为EQ_full_name会报错
			String[] names = StringUtils.split(key, "_", 2);
			if (names.length < 2) {
				throw new BusinessException("查询条件截取出错");
			}
			Operator op = Operator.getOpeartor(names[0]);
			if (op == null) {
				throw new BusinessException("查询方式未定义");
			}
			String filedName = names[1];
			Object value = entry.getValue();
			if (value == null || StringUtils.isBlank(String.valueOf(value))) {
				// searchMap.put("NULL_goodsId", null);如果条件是NULL或NOTNULL是这种情况，值是允许为空的
				if (!(Operator.NULL.toString().equals(names[0]) || Operator.NOTNULL.toString().equals(names[0]))) {
					continue;
				}
			}
			filters.add(new SearchFilter(filedName, coverdColumName(filedName), op, value));
		}
		return filters;
	}

	public static String coverdColumName(String columName) {
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < columName.length(); i++) {
			char c = columName.charAt(i);
			// 如果是大写，转成小写并在前面添加下划线
			if (c >= 65 && c <= 90) {
				str.append((char) 95).append((char) (c + 32));
			} else {
				str.append(c);
			}
		}
		return str.toString();
	}
}
