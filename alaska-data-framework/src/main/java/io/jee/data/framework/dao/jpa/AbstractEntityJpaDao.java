package io.jee.data.framework.dao.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import io.jee.alaska.data.page.PageInput;
import io.jee.alaska.data.page.PageOutput;

public class AbstractEntityJpaDao<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements EntityJpaDao<T, ID> {

	private Class<T> domainClass;
	private EntityManager em;
	
	public AbstractEntityJpaDao(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.domainClass = domainClass;
		this.em = em;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public T create(T entity) {
		return this.save(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Serializable id) {
		return this.findById((ID) id).get();
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void update(T entity) {
		this.save(entity);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public List<T> saveAll(List<T> entities){
		return this.saveAll(entities);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void delete(T entity) {
		this.delete(entity);
	}

	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deleteById(Serializable id) {
		this.deleteById((ID) id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void deleteAll(Serializable... ids){
		Iterator<Serializable> iterator = Lists.newArrayList(ids).iterator();
		String queryString = String.format(QueryUtils.DELETE_ALL_QUERY_STRING,
				getEntityName());
		String alias = QueryUtils.detectAlias(queryString);
		StringBuilder builder = new StringBuilder(queryString);
		builder.append(" where");
		int i = 0;
		while (iterator.hasNext()) {
			iterator.next();
			builder.append(String.format(" %s.id = ?%d", alias, ++i));
			if (iterator.hasNext()) {
				builder.append(" or");
			}
		}
		Query query = em.createQuery(builder.toString());
		iterator = Lists.newArrayList(ids).iterator();
		i = 0;
		while (iterator.hasNext()) {
			query.setParameter(++i, iterator.next());
		}
		query.executeUpdate();
	}

	@Override
	public List<T> findAll() {
		return this.findAll();
	}

	@Override
	public PageOutput<T> query(PageInput pageInput, Map<String, Object> searchMap, Map<String, Boolean> orderMap) {
		Specification<T> spec = buildSpecification(searchMap);
		Sort sort = buildSort(orderMap);
		Pageable pageable = PageRequest.of(pageInput.getPage(), pageInput.getSize(), sort);
		Page<T> page = this.findAll(spec, pageable);
		return new PageOutput<>(page.getContent(), page.getTotalElements());
	}

	@Override
	public List<T> query(Map<String, Object> searchMap, Map<String, Boolean> orderMap) {
		Specification<T> spec = buildSpecification(searchMap);
		Sort sort = buildSort(orderMap);
		return this.findAll(spec, sort);
	}

	protected Class<T> getDomainClass() {
		return domainClass;
	}

	protected EntityManager getEntityManager() {
		return em;
	}

	protected String getEntityName() {
		return domainClass.getSimpleName();
	}
	
	/**
	 * 生成查询条件
	 * 
	 * @param map
	 * @return
	 */
	private Specification<T> buildSpecification(Map<String, Object> map) {
		List<SearchFilter> searchFilters = SearchFilter.parse(map);
		return DynamicSpecifications.bySearchFilter(searchFilters, null);
	}

	/**
	 * 生成查询排序
	 * 
	 * @param orderMap
	 * @return
	 */
	private Sort buildSort(Map<String, Boolean> orderMap) {
		List<Order> orders = new ArrayList<Order>();
		if (orderMap != null && orderMap.size() > 0) {
			for (Map.Entry<String, Boolean> entry : orderMap.entrySet()) {
				orders.add(new Order(entry.getValue() ? Direction.ASC : Direction.DESC, entry.getKey()));
			}
		} else {
			orders.add(new Order(Direction.DESC, "id"));
		}
		Sort sort = Sort.by(orders);
		return sort;
	}
}
