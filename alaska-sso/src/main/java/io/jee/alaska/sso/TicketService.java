package io.jee.alaska.sso;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="alaska-sso-ticket-service", path="/ticket")
public interface TicketService {
	
	@PutMapping("/add")
	String addTicket(@RequestParam("username") String username);
	
	@GetMapping("/verify/{ticket}")
	TicketVerify verifyTicket(@PathVariable("ticket") String ticket);

}
