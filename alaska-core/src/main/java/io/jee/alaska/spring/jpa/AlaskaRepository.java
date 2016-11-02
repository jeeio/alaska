package io.jee.alaska.spring.jpa;

import java.io.Serializable;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import io.jee.alaska.spring.jpa.condition.Condition;
import io.jee.alaska.spring.jpa.condition.Count;
import io.jee.alaska.spring.jpa.condition.Delete;
import io.jee.alaska.spring.jpa.condition.Select;
import io.jee.alaska.spring.jpa.condition.Update;

@NoRepositoryBean
public interface AlaskaRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>{

	T findOne(ID id, LockModeType lockMode);
	
	/**
	 * 二级选项已经提出到一级
	 * @return
	 */
	@Deprecated
	Condition<T> condition();
	
	Select<T> select();
	
	Select<T> select(boolean cacheable);
	
	Count<T> selectCount();
	
	Update<T> update();
	
	Delete<T> delete();
	
}
