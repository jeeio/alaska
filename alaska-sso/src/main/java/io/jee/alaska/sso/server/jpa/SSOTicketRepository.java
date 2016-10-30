package io.jee.alaska.sso.server.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SSOTicketRepository extends JpaRepository<SSOTicket, String> {
	
	@Modifying
	@Query("delete SSOTicket where time < ?1")
	void deleteByTimeLessThan(long time);

}
