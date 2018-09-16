package io.jee.alaska.data.jpa.condition;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;

public class Count<T> {

	private final EntityManager entityManager;
	private StringBuffer hql;
	
	private Where where = new Where();
	private Map<String, Object> param = new HashMap<>();
	private AtomicInteger p = new AtomicInteger(0);
	
	public Count(EntityManager entityManager, Class<T> clazz) {
		this.entityManager = entityManager;
		hql = new StringBuffer("select count(e) from " + clazz.getName() +" e");
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
		}else if(operation == Operation.IN){
			hql.append(" where e." + key + operation.getKeyword() + "(:" +random+")");
		}else{
			hql.append(" where e." + key + operation.getKeyword() + ":" + random);
		}
		if(value!=null){
			param.put(random, value);
		}
		return where;
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
			}else if(operation == Operation.IN){
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
			}else if(operation == Operation.IN){
				hql.append(" or e." + key + operation.getKeyword() + "(:" +random+")");
			}else{
				hql.append(" or e." + key + operation.getKeyword() + ":" + random);
			}
			if(value!=null){
				param.put(random, value);
			}
			return this;
		}
		
		public long end(){
			return (long) this.end(null);
		}
		
		public long end(LockModeType lockMode){
			TypedQuery<Long> query = entityManager.createQuery(hql.toString(), Long.class);
			for (Entry<String,Object> entry : param.entrySet()) {
				Object value = entry.getValue();
				query.setParameter(entry.getKey(), value);
			}
			if(lockMode!=null){
				query.setLockMode(lockMode);
			}
			return query.getSingleResult();
		}
	}
	

}
