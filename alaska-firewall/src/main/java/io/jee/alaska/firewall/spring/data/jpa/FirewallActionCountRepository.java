package io.jee.alaska.firewall.spring.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FirewallActionCountRepository extends JpaRepository<FirewallActionCount, Integer>, JpaSpecificationExecutor<FirewallActionCount> {
	
	@Modifying
	@Query("delete FirewallActionCount where timeout < ?1")
	@Transactional
	void deleteByTimeoutLessThan(long time);
	
}
