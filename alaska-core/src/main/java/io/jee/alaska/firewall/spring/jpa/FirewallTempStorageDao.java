package io.jee.alaska.firewall.spring.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FirewallTempStorageDao extends JpaRepository<FirewallTempStorage, String>, JpaSpecificationExecutor<FirewallTempStorage> {
	
	@Modifying
	@Query("delete FirewallTempStorage where expire < ?1")
	void deleteByTimeLessThan(long time);
	
}
