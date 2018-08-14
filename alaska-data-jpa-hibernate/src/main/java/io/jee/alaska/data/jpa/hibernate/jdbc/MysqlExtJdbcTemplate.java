package io.jee.alaska.data.jpa.hibernate.jdbc;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import io.jee.alaska.data.page.PageInput;
import io.jee.alaska.data.page.PageOutput;
import io.jee.alaska.data.page.PageUtils;

public class MysqlExtJdbcTemplate extends JdbcTemplate implements ExtJdbcTemplate {

	static final String PAGE_QUERY_STRING = " %s limit %d, %d ";

	public MysqlExtJdbcTemplate() {
	}

	public MysqlExtJdbcTemplate(DataSource dataSource) {
		setDataSource(dataSource);
		afterPropertiesSet();
	}
	
	public MysqlExtJdbcTemplate(DataSource dataSource, boolean lazyInit) {
		setDataSource(dataSource);
		setLazyInit(lazyInit);
		afterPropertiesSet();
	}

	public long queryForCount(String sql, Object... object) {
		if (QueryCondition.isOutermostGroupBy(sql)) {// 如果SQL最后面是group by，则统计会报错，所以后面加一层
			sql = String.format(QueryCondition.COUNT_QUERY_STRING, sql);
		}
		Number number = queryForObject(sql, Long.class, object);
		return (number != null ? number.longValue() : 0);
	}

	public long queryForCount(String sql, Map<String, Object> searchMap) {
		QueryCondition countQuery = QueryCondition.createCountQuery(sql, searchMap);
		return queryForCount(countQuery.sql, countQuery.params);
	}

	@Override
	public <T> List<T> queryForList(Class<T> transClass, String sql, Object... object) {
		return super.query(sql, object, BeanPropertyRowMapper.newInstance(transClass));
	}

	@Override
	public <T> List<T> queryForList(Class<T> transClass, String sql, Map<String, Object> searchMap) {
		return queryForList(transClass, sql, searchMap, null);
	}

	@Override
	public <T> List<T> queryForList(Class<T> transClass, String sql, Map<String, Object> searchMap,
			Map<String, Boolean> orderMap) {
		QueryCondition result = QueryCondition.createResultQuery(sql, searchMap, orderMap);
		return queryForList(transClass, result.sql, result.params);
	}

	@Override
	public <T> PageOutput<T> queryForPage(Class<T> transClass, String sql, PageInput pageInput, Object... object) {
		return queryForPage(transClass, sql, pageInput, null, object);
	}

	@Override
	public <T> PageOutput<T> queryForPage(Class<T> transClass, String sql, PageInput pageInput,
			Map<String, Boolean> orderMap, Object... object) {
		Pageable pageable = pageInput.toPageRequest();
		long total = Long.MAX_VALUE;
		if (pageInput.getSize() != Integer.MAX_VALUE) {
			QueryCondition count = QueryCondition.createCountQuery(sql, object);
			total = queryForCount(count.sql, count.params);
			if (total < 1) {
				return PageUtils.toPageOutput(new PageImpl<T>(Collections.<T>emptyList(), pageable, total));
			}
		}

		if (pageable.getSort() != null) {
			Iterator<Order> orders = pageable.getSort().iterator();
			if(orderMap == null) orderMap = new LinkedHashMap<>();
			while (orders.hasNext()) {
				Order order = orders.next();
				orderMap.put(order.getProperty(), Direction.ASC.equals(order.getDirection()) ? true : false);
			}
		}

		if (orderMap != null && !orderMap.isEmpty()) {
			StringBuilder sqlbuilder = new StringBuilder(sql);
			QueryCondition.orderBy(sqlbuilder, orderMap);
			sql = sqlbuilder.toString();
		}
		sql = pageableQuery(sql, pageable);
		List<T> content = this.queryForList(transClass, sql, object);
		return PageUtils.toPageOutput(new PageImpl<T>(content, pageable, total));
	}

	@Override
	public <T> PageOutput<T> queryForPage(Class<T> transClass, String sql, PageInput pageInput,
			Map<String, Object> searchMap) {
		return queryForPage(transClass, sql, pageInput, searchMap, null);
	}

	@Override
	public <T> PageOutput<T> queryForPage(Class<T> transClass, String sql, PageInput pageInput,
			Map<String, Object> searchMap, Map<String, Boolean> orderMap) {
		Pageable pageable = pageInput.toPageRequest();
		long total = Long.MAX_VALUE;
		if (pageable.getPageSize() != Integer.MAX_VALUE) {
			QueryCondition count = QueryCondition.createCountQuery(sql, searchMap);
			total = queryForCount(count.sql, count.params);
			if (total < 1) {
				return PageUtils.toPageOutput(new PageImpl<T>(Collections.<T>emptyList(), pageable, total));
			}
		}

		if (pageable.getSort() != null) {
			Iterator<Order> orders = pageable.getSort().iterator();
			while (orders.hasNext()) {
				Order order = orders.next();
				orderMap.put(order.getProperty(), Direction.ASC.equals(order.getDirection()) ? true : false);
			}
		}

		QueryCondition result = QueryCondition.createResultQuery(sql, searchMap, orderMap);
		sql = pageableQuery(result.sql, pageable);
		List<T> content = queryForList(transClass, sql, result.params);
		return PageUtils.toPageOutput(new PageImpl<T>(content, pageable, total));
	}

	protected String pageableQuery(String sql, Pageable pageable) {
		int page = pageable.getPageNumber();
		int size = pageable.getPageSize();
		if (size != Integer.MAX_VALUE) {
			return String.format(PAGE_QUERY_STRING, sql, (page * size), size);
		} else {
			return sql;
		}
	}

}
