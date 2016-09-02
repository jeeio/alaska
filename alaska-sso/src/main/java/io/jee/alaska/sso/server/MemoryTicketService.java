package io.jee.alaska.sso.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.jee.alaska.sso.TicketVerify;

public class MemoryTicketService implements TicketService {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public final static Map<String, TicketModel> TKS = new HashMap<>();

	@Override
	public String addTicket(String username) {
		String ticket = UUID.randomUUID().toString().replace("-", "");
		TicketModel ticketModel = new TicketModel();
		ticketModel.setTime(System.currentTimeMillis());
		ticketModel.setUsername(username);
		TKS.put(ticket, ticketModel);
		return ticket;
	}

	@Override
	public TicketVerify verifyTicket(String ticket) {
		TicketModel ticketModel = TKS.remove(ticket);
		
		TicketVerify ticketVerify = new TicketVerify();
		if(ticketModel!=null&&ticketModel.getTime()>System.currentTimeMillis()-60000){
			ticketVerify.setSuccess(true);
			ticketVerify.setUsername(ticketModel.getUsername());
		}else{
			ticketVerify.setSuccess(false);
		}
		return ticketVerify;
	}

	@Override
	public void clear() {
		Iterator<Entry<String, TicketModel>> iterator = TKS.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, TicketModel> entry = iterator.next();
			if(entry.getValue().getTime()<System.currentTimeMillis()-120000){
				iterator.remove();
				logger.warn("删除"+entry.getKey()+":"+TKS.size());
			}
		}
	}

	class TicketModel {
		
		private String username;
		private long time;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}

	}

}
