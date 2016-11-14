package io.jee.alaska.firewall.spring.data.redis;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FirewallActionCountRepository extends CrudRepository<FirewallActionCount, Integer> {

	List<FirewallActionCount> findByKeyword(String keyword);
	
}
