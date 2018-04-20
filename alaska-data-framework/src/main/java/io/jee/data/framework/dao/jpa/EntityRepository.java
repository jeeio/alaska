package io.jee.data.framework.dao.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.repository.NoRepositoryBean;

import io.jee.alaska.data.jpa.hibernate.AlaskaRepository;
import io.jee.alaska.data.page.PageInput;
import io.jee.alaska.data.page.PageOutput;

@NoRepositoryBean
public interface EntityRepository<T, ID extends Serializable> extends AlaskaRepository<T, ID> {

	public T findOne(ID id);
	
	public void deleteAll(Serializable... ids);

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
	public PageOutput<T> queryForPage(PageInput pageInput, Map<String, Object> searchMap, Map<String, Boolean> orderMap);

	/**
	 * 多条件,多排序规则的查询
	 * 
	 * @param searchMap
	 *            条件Map，Key--> LIKE/EQ/GT/GTE/LT/LTE_propertyName; value-->查询条件右值
	 * @param orderMap
	 *            排序Map, Key --> propertyName; value=true(ASC)/false(DESC)
	 * @return List<T> 查询结果：满足条件并按直接排序的实体集合
	 */
	public List<T> queryForList(Map<String, Object> searchMap, Map<String, Boolean> orderMap);
}
