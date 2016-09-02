package io.jee.alaska.sso.server;

import io.jee.alaska.sso.TicketVerify;

public interface TicketService {
	
	String addTicket(String username);
	
	TicketVerify verifyTicket(String ticket);
	
	void clear();

}
