package io.jee.alaska.data.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcOperations;

import io.jee.alaska.data.page.PageInput;
import io.jee.alaska.data.page.PageOutput;

public interface ExtJdbcTemplate extends JdbcOperations {

	public long queryForCount(String sql, Object... object);

	public long queryForCount(String sql, Map<String, Object> searchMap);

	public <T> List<T> queryForList(Class<T> transClass, String sql, Object... object);

	public <T> List<T> queryForList(Class<T> transClass, String sql, Map<String, Object> searchMap);

	public <T> List<T> queryForList(Class<T> transClass, String sql, Map<String, Object> searchMap, Map<String, Boolean> orderMap);

	public <T> PageOutput<T> queryForPage(Class<T> transClass, String sql, PageInput pageInput, Object... object);

	public <T> PageOutput<T> queryForPage(Class<T> transClass, String sql, PageInput pageInput, Map<String, Boolean> orderMap, Object... object);

	public <T> PageOutput<T> queryForPage(Class<T> transClass, String sql, PageInput pageInput, Map<String, Object> searchMap);

	public <T> PageOutput<T> queryForPage(Class<T> transClass, String sql, PageInput pageInput, Map<String, Object> searchMap, Map<String, Boolean> orderMap);

}
