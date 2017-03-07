package io.jee.alaska.data.jpa.hibernate.condition;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Update<T> {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private final EntityManager entityManager;
	private StringBuffer hql;
	
	private Set set = new Set();
	private Where where = new Where();
	private Map<String, Object> param = new HashMap<>();
	private AtomicInteger p = new AtomicInteger(0);
	
	
	public Update(EntityManager entityManager, Class<T> clazz) {
		this.entityManager = entityManager;
		hql = new StringBuffer("update " + clazz.getName() +" e");
	}
	
	public Set set(String key, Object value){
		if(value != null){
			String random = "p"+p.incrementAndGet();
			hql.append(" set e." + key + " = :" + random);
			param.put(random, value);
		}else{
			hql.append(" set e." + key + " = null");
		}
		return set;
	}
	
	public class Set {
		
		public Set put(String key, Object value){
			if(value != null){
				String random = "p"+p.incrementAndGet();
				hql.append(", e." + key + " = :" + random);
				param.put(random, value);
			}else{
				hql.append(", e." + key + " = null");
			}
			return this;
		}
		
		public int end(){
			return where.end();
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
		
		public int end(){
			Query query = entityManager.createQuery(hql.toString());
			if(logger.isDebugEnabled()){
				logger.debug(hql.toString()+" "+param);
			}
			for (Entry<String,Object> entry : param.entrySet()) {
				Object value = entry.getValue();
				query.setParameter(entry.getKey(), value);
			}
			return query.executeUpdate();
		}
	}
	
}
