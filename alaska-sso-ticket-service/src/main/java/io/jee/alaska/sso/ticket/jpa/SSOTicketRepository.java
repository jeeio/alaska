package io.jee.alaska.sso.ticket.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface SSOTicketRepository extends JpaRepository<SSOTicket, String> {
	
	@Modifying
	@Query("delete SSOTicket where time < ?1")
	@Transactional
	void deleteByTimeLessThan(long time);

}
