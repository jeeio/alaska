package io.jee.alaska.spring.jpa;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import io.jee.alaska.spring.jpa.condition.Condition;

@NoRepositoryBean
public interface AlaskaRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T>{

	Condition<T> condition();
	
}
