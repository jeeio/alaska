package io.jee.alaska.data.jpa;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import io.jee.alaska.data.jpa.condition.Count;
import io.jee.alaska.data.jpa.condition.Delete;
import io.jee.alaska.data.jpa.condition.Select;
import io.jee.alaska.data.jpa.condition.Update;

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

}
