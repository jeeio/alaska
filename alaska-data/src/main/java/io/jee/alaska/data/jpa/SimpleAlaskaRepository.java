package io.jee.alaska.data.jpa;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import io.jee.alaska.data.jdbc.DynamicSpecifications;
import io.jee.alaska.data.jdbc.SearchFilter;
import io.jee.alaska.data.jpa.condition.Count;
import io.jee.alaska.data.jpa.condition.Delete;
import io.jee.alaska.data.jpa.condition.Select;
import io.jee.alaska.data.jpa.condition.Update;
import io.jee.alaska.data.page.PageInput;
import io.jee.alaska.data.page.PageOutput;

public class SimpleAlaskaRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>
		implements AlaskaRepository<T, ID> {
	
	private final EntityManager entityManager;
	private final Class<T> domainClass;
	
	public SimpleAlaskaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
		domainClass = entityInformation.getJavaType();
	}
	
	@Override
	public T findOne(ID id) {
		return this.findById(id).orElse(null);
	}
	
	@Override
	public T findOne(ID id, LockModeType lockMode) {
		return entityManager.find(domainClass, id, lockMode);
	}
	
	@Override
	@Transactional
	public T update(ID id, String key, Object value) {
		T t = findById(id).get();
		try {
			PropertyDescriptor descriptor = new PropertyDescriptor(key, domainClass);
			Method set = descriptor.getWriteMethod();
			set.invoke(t, value);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return save(t);
	}
	
	@Override
	@Transactional
	public T update(ID id, Map<String, Object> keyVal) {
		T t = findById(id).get();
		Set<Entry<String, Object>> entrys = keyVal.entrySet();
		for (Entry<String, Object> entry : entrys) {
			try {
				PropertyDescriptor descriptor = new PropertyDescriptor(entry.getKey(), domainClass);
				Method set = descriptor.getWriteMethod();
				set.invoke(t, entry.getValue());
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return save(t);
	}

	@Override
	public Select<T> select(){
		return new Select<>(false, entityManager, domainClass);
	}
	
	@Override
	public Select<T> select(boolean cacheable){
		return new Select<>(cacheable, entityManager, domainClass);
	}
	
	@Override
	public Count<T> selectCount(){
		return new Count<>(entityManager, domainClass);
	}
	
	@Override
	public Update<T> update(){
		return new Update<>(entityManager, domainClass);
	}
	
	@Override
	public Delete<T> delete(){
		return new Delete<>(entityManager, domainClass);
	}

	@Override
	public void deleteAll(Serializable... ids) {
		Iterator<Serializable> iterator = Arrays.asList(ids).iterator();
		String queryString = String.format(QueryUtils.DELETE_ALL_QUERY_STRING, domainClass.getSimpleName());
		@SuppressWarnings("deprecation")
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
		Query query = entityManager.createQuery(builder.toString());
		iterator = Arrays.asList(ids).iterator();
		i = 0;
		while (iterator.hasNext()) {
			query.setParameter(++i, iterator.next());
		}
		query.executeUpdate();
	}

	@Override
	public PageOutput<T> queryForPage(PageInput pageInput, Map<String, Object> searchMap,
			Map<String, Boolean> orderMap) {
		Specification<T> spec = buildSpecification(searchMap);
		Sort sort = buildSort(orderMap);
		Pageable pageable = PageRequest.of(pageInput.getPage(), pageInput.getSize(), sort);
		Page<T> page = this.findAll(spec, pageable);
		return new PageOutput<>(page.getContent(), page.getTotalElements(), pageInput.getPage() + 1, pageInput.getSize());
	}

	@Override
	public List<T> queryForList(Map<String, Object> searchMap, Map<String, Boolean> orderMap) {
		Specification<T> spec = buildSpecification(searchMap);
		Sort sort = buildSort(orderMap);
		return this.findAll(spec, sort);
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
			//orders.add(new Order(Direction.DESC, "id"));
		}
		Sort sort = Sort.by(orders);
		return sort;
	}
}
