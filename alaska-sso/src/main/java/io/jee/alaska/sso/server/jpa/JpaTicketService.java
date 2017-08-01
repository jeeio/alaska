package io.jee.alaska.sso.server.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.jee.alaska.sso.TicketVerify;
import io.jee.alaska.sso.server.TicketService;

@Service
@Transactional
public class JpaTicketService implements TicketService {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private SSOTicketRepository ssoTicketRepository;

	@Override
	public String addTicket(String username) {
		SSOTicket ssoTicket = new SSOTicket();
		ssoTicket.setUsername(username);
		ssoTicket.setTime(System.currentTimeMillis());
		return ssoTicketRepository.save(ssoTicket).getTicket();
	}

	@Override
	public TicketVerify verifyTicket(String ticket) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				clear();
			}
		}).start();
		SSOTicket ssoTicket = ssoTicketRepository.findOne(ticket);
		TicketVerify ticketVerify = new TicketVerify();
		if(ssoTicket!=null&&ssoTicket.getTime()>System.currentTimeMillis()-60000){
			ticketVerify.setSuccess(true);
			ticketVerify.setUsername(ssoTicket.getUsername());
			ssoTicketRepository.delete(ssoTicket);
		}else{
			logger.warn("TK验证失败："+ticket);
			ticketVerify.setSuccess(false);
		}
		return ticketVerify;
	}

	public void clear() {
		ssoTicketRepository.deleteByTimeLessThan(System.currentTimeMillis()-100000);
	}
	
}
