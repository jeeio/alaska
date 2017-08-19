package io.jee.alaska.sso.ticket.redis;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import io.jee.alaska.sso.TicketService;
import io.jee.alaska.sso.TicketVerify;

@Service
public class RedisTicketService implements TicketService {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public String addTicket(String username) {
		String key = UUID.randomUUID().toString();
		stringRedisTemplate.boundValueOps(key).set(username, 1, TimeUnit.MINUTES);
		return key;
	}

	@Override
	public TicketVerify verifyTicket(String ticket) {
		String username = stringRedisTemplate.boundValueOps(ticket).get();
		TicketVerify ticketVerify = new TicketVerify();
		if(StringUtils.hasLength(username)){
			ticketVerify.setSuccess(true);
			ticketVerify.setUsername(username);
			stringRedisTemplate.delete(ticket);
		}else{
			logger.warn("TK验证失败："+ticket);
			ticketVerify.setSuccess(false);
		}
		return ticketVerify;
	}

}
