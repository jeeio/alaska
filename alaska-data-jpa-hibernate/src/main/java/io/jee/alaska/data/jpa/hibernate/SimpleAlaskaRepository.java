package io.jee.alaska.data.jpa.hibernate;

import java.io.Serializable;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.google.common.collect.Lists;

import io.jee.alaska.data.jpa.hibernate.condition.Count;
import io.jee.alaska.data.jpa.hibernate.condition.Delete;
import io.jee.alaska.data.jpa.hibernate.condition.Select;
import io.jee.alaska.data.jpa.hibernate.condition.Update;

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
		return this.findById(id).get();
	}
	
	@Override
	public T findOne(ID id, LockModeType lockMode) {
		return entityManager.find(domainClass, id, lockMode);
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
		Iterator<Serializable> iterator = Lists.newArrayList(ids).iterator();
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
		iterator = Lists.newArrayList(ids).iterator();
		i = 0;
		while (iterator.hasNext()) {
			query.setParameter(++i, iterator.next());
		}
		query.executeUpdate();
	}

}
