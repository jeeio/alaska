package io.jee.data.framework.dao.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import io.jee.alaska.data.page.PageInput;
import io.jee.alaska.data.page.PageOutput;

public interface EntityJpaDao<T, ID extends Serializable> extends JpaRepository<T, ID> {
	/**
	 * 创建实体
	 * 
	 * @param entity
	 *            实体对象
	 */
	T create(T entity);

	/**
	 * 获取单个实体
	 * 
	 * @param id
	 *            实体ID
	 * @return 实体对象
	 */
	T get(Serializable id);

	/**
	 * 更新实体
	 * 
	 * @param entity
	 *            实体对象
	 */
	void update(T entity);

	/**
	 * 批量保存
	 * 
	 * @param entities
	 *            实体集合
	 */
	List<T> saveAll(List<T> entities);

	/**
	 * 删除实体
	 * 
	 * @param entity
	 *            实体对象
	 */
	void delete(T entity);

	/**
	 * 根据实体ID删除实体
	 * 
	 * @param id
	 *            实体ID
	 */
	void deleteById(Serializable id);

	/**
	 * 批量删除
	 * 
	 * @param ids
	 *            实体ID数组
	 */
	void deleteAll(Serializable... ids);

	/**
	 * 获取全部数据
	 * 
	 * @return 实体集合
	 */
	List<T> findAll();

	/**
	 * 多条件,多排序规则的分页查询
	 * 
	 * @param pageInput
	 *            分页对象(page和size为必填)
	 * @param searchMap
	 *            条件Map，Key--> LIKE/EQ/GT/GTE/LT/LTE_propertyName; value-->查询条件右值
	 * @param orderMap
	 *            排序Map, Key --> propertyName; value=true(ASC)/false(DESC)
	 * @return PageOutput<T> 查询结果：包括分页信息和当前页数据集合
	 */
	PageOutput<T> query(PageInput pageInput, Map<String, Object> searchMap, Map<String, Boolean> orderMap);

	/**
	 * 多条件,多排序规则的查询
	 * 
	 * @param searchMap
	 *            条件Map，Key--> LIKE/EQ/GT/GTE/LT/LTE_propertyName; value-->查询条件右值
	 * @param orderMap
	 *            排序Map, Key --> propertyName; value=true(ASC)/false(DESC)
	 * @return List<T> 查询结果：满足条件并按直接排序的实体集合
	 */
	List<T> query(Map<String, Object> searchMap, Map<String, Boolean> orderMap);
}
