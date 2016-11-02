package io.jee.alaska.spring.jpa.condition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;

import org.hibernate.query.internal.AbstractProducedQuery;
import org.springframework.util.CollectionUtils;


public class Select<T> {
	
	private final EntityManager entityManager;
	private final Class<T> clazz;
	
	private StringBuffer hql;
	
	private Where where = new Where();
	private Order order = new Order();
	private End end = new End();
	
	private Map<String, Object> param = new HashMap<>();
	private Map<String, String> orderBy = new HashMap<>();
	private AtomicInteger p = new AtomicInteger(0);
	
	private boolean cacheable;
	
	public Select(boolean cacheable, EntityManager entityManager, Class<T> clazz) {
		this.cacheable = cacheable;
		this.entityManager = entityManager;
		this.clazz = clazz;
		hql = new StringBuffer("select e from " + clazz.getName() +" e");
	}
	
	/**
	 * 起始方法，默认为=
	 * @param key
	 * @param value
	 * @return
	 */
	public Where where(String key, Object value){
		return where(key, Operation.EQ, value);
	}
	
	/**
	 * 起始方法
	 * @param key
	 * @param operation
	 * @param value
	 * @return
	 */
	public Where where(String key, Operation operation, Object value){
		String random = "p"+p.incrementAndGet();
		if(operation == Operation.EQ && value == null){
			hql.append(" where e." + key + " is null");
		}else if(operation == Operation.NEQ && value == null){
			hql.append(" where e." + key + " is not null");
		}else if(operation == Operation.IN || operation == Operation.NIN){
			hql.append(" where e." + key + operation.getKeyword() + "(:" +random+")");
		}else{
			hql.append(" where e." + key + operation.getKeyword() + ":" + random);
		}
		if(value!=null){
			param.put(random, value);
		}
		return where;
	}
	
	public Order order(){
		return order;
	}
	
	public End end(){
		return end;
	}
	
	public class Where {
		
		/**
		 * AND 默认为=
		 * @param key
		 * @param value
		 * @return
		 */
		public Where and(String key, Object value){
			return and(key, Operation.EQ, value);
		}
		
		/**
		 * AND
		 * @param key
		 * @param operation
		 * @param value
		 * @return
		 */
		public Where and(String key, Operation operation, Object value){
			String random = "p"+p.incrementAndGet();
			if(operation == Operation.EQ && value == null){
				hql.append(" and e." + key + " is null");
			}else if(operation == Operation.NEQ && value == null){
				hql.append(" and e." + key + " is not null");
			}else if(operation == Operation.IN || operation == Operation.NIN){
				hql.append(" and e." + key + operation.getKeyword() + "(:" +random+")");
			}else{
				hql.append(" and e." + key + operation.getKeyword() + ":" + random);
			}
			if(value!=null){
				param.put(random, value);
			}
			return this;
		}
		
		/**
		 * OR 默认为=
		 * @param key
		 * @param value
		 * @return
		 */
		public Where or(String key, Object value){
			return or(key, Operation.EQ, value);
		}
		
		/**
		 * OR
		 * @param key
		 * @param operation
		 * @param value
		 * @return
		 */
		public Where or(String key, Operation operation, Object value){
			String random = "p"+p.incrementAndGet();
			if(operation == Operation.EQ && value == null){
				hql.append(" or e." + key + " is null");
			}else if(operation == Operation.NEQ && value == null){
				hql.append(" or e." + key + " is not null");
			}else if(operation == Operation.IN || operation == Operation.NIN){
				hql.append(" or e." + key + operation.getKeyword() + "(:" +random+")");
			}else{
				hql.append(" or e." + key + operation.getKeyword() + ":" + random);
			}
			if(value!=null){
				param.put(random, value);
			}
			return this;
		}
		
		public Order order(){
			return order;
		}
		
		public End end(){
			return end;
		}
	}
	
	public class Order {
		
		public Order asc(String key){
			orderBy.put(key, "ASC");
			return this;
		}
		
		public Order desc(String key){
			orderBy.put(key, "DESC");
			return this;
		}
		
		public Order on(String key, String type){
			orderBy.put(key, type);
			return this;
		}
		
		public End end(){
			return end;
		}
		
	}
	
	public class End {
		
		private <X> TypedQuery<X> common(String field, Class<X> clazz){
			
			if(!CollectionUtils.isEmpty(orderBy)){
				hql.append(" order by ");
				boolean hasFirst = true;
				for (Entry<String, String> entry : orderBy.entrySet()) {
					if(hasFirst){
						hql.append(entry.getKey()+" "+entry.getValue());
						hasFirst = false;
					}else{
						hql.append(", "+entry.getKey()+" "+entry.getValue());
					}
				}
			}
			
			if(field!=null){
				hql.replace(7, 8, "e."+field);
			}
			TypedQuery<X> query = entityManager.createQuery(hql.toString(), clazz);
			if(cacheable){
				query.setHint("org.hibernate.cacheable", true);
			}
			for (Entry<String,Object> entry : param.entrySet()) {
				Object value = entry.getValue();
				query.setParameter(entry.getKey(), value);
			}
			return query;
		}
		
		public T unique(){
			TypedQuery<T> query = common(null, clazz);
			return AbstractProducedQuery.uniqueElement(query.getResultList());
		}
		
		public <P> P uniqueField(String field, Class<P> clazz){
			TypedQuery<P> query = common(field, clazz);
			return AbstractProducedQuery.uniqueElement(query.getResultList());
		}
		
		public T unique(LockModeType lockMode){
			TypedQuery<T> query = common(null, clazz);
			if(lockMode!=null){
				query.setLockMode(lockMode);
			}
			return AbstractProducedQuery.uniqueElement(query.getResultList());
		}
		
		public List<T> list(){
			TypedQuery<T> query = common(null, clazz);
			return query.getResultList();
		}
		
		public List<T> list(int size){
			TypedQuery<T> query = common(null, clazz).setFirstResult(0).setMaxResults(size);
			return query.getResultList();
		}
	}

}
