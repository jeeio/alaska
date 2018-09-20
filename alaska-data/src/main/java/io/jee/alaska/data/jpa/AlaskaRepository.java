package io.jee.alaska.data.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import io.jee.alaska.data.jpa.condition.Count;
import io.jee.alaska.data.jpa.condition.Delete;
import io.jee.alaska.data.jpa.condition.Select;
import io.jee.alaska.data.jpa.condition.Update;
import io.jee.alaska.data.page.PageInput;
import io.jee.alaska.data.page.PageOutput;

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

	PageOutput<T> queryForPage(PageInput pageInput, Map<String, Object> searchMap, Map<String, Boolean> orderMap);

	List<T> queryForList(Map<String, Object> searchMap, Map<String, Boolean> orderMap);

}
