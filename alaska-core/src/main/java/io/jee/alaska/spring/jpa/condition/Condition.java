package io.jee.alaska.spring.jpa.condition;

import javax.persistence.EntityManager;

public class Condition<T> {
	
	private final EntityManager entityManager;
	private final Class<T> clazz;
	
	
	public Condition(EntityManager entityManager, Class<T> clazz) {
		this.entityManager = entityManager;
		this.clazz = clazz;
	}
	
	public Select<T> select(){
		return new Select<>(false, entityManager, clazz);
	}
	
	public Select<T> select(boolean cacheable){
		return new Select<>(cacheable, entityManager, clazz);
	}
	
	public Count<T> count(){
		return new Count<>(entityManager, clazz);
	}
	
	public Update<T> update(){
		return new Update<>(entityManager, clazz);
	}
	
	public Delete<T> delete(){
		return new Delete<>(entityManager, clazz);
	}

}
