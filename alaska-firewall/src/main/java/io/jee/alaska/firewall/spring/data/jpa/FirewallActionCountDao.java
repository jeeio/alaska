package io.jee.alaska.firewall.spring.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FirewallActionCountDao extends JpaRepository<FirewallActionCount, Integer>, JpaSpecificationExecutor<FirewallActionCount> {
	
	@Modifying
	@Query("delete FirewallActionCount where timeout < ?1")
	void deleteByTimeoutLessThan(long time);
	
}
