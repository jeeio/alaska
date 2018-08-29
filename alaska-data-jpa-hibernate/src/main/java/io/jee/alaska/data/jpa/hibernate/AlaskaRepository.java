package io.jee.alaska.data.jpa.hibernate;

import java.io.Serializable;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import io.jee.alaska.data.jpa.hibernate.condition.Count;
import io.jee.alaska.data.jpa.hibernate.condition.Delete;
import io.jee.alaska.data.jpa.hibernate.condition.Select;
import io.jee.alaska.data.jpa.hibernate.condition.Update;

@NoRepositoryBean
public interface AlaskaRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>{

	T findOne(ID id);
	
	T findOne(ID id, LockModeType lockMode);
	
	Select<T> select();
	
	Select<T> select(boolean cacheable);
	
	Count<T> selectCount();
	
	Update<T> update();
	
	Delete<T> delete();
	
	void deleteAll(Serializable... ids);

}
