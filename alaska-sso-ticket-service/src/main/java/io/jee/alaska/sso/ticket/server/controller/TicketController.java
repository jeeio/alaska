package io.jee.alaska.sso.ticket.server.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jee.alaska.sso.TicketService;
import io.jee.alaska.sso.TicketVerify;

@RestController
@RequestMapping("/ticket")
public class TicketController {
	
	@Resource
	private TicketService ticketService;

	@PutMapping("/add")
	public String addTicket(String username) {
		return ticketService.addTicket(username);
	}

	@GetMapping("/verify/{ticket}")
	public TicketVerify verifyTicket(@PathVariable String ticket) {
		return ticketService.verifyTicket(ticket);
	}

}
