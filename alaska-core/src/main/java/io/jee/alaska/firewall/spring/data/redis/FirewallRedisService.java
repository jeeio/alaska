package io.jee.alaska.firewall.spring.data.redis;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import io.jee.alaska.firewall.FirewallService;

@Service
public class FirewallRedisService implements FirewallService {
	
	@Resource
	private RedisTemplate<String, Long> redisTemplate;
	
	@Override
	public boolean verifyActionCount(String keyword, int count, byte type) {
		BoundSetOperations<String, Long> setOperations = redisTemplate.boundSetOps("firewallActionCount:"+keyword+"-"+type);
		
		Set<Long> members = setOperations.members();
		Iterator<Long> iterator = members.iterator();
		int size = 0;
		while (iterator.hasNext()) {
			Long member = iterator.next();
			if(member < System.currentTimeMillis()){
				setOperations.remove(member);
			}else{
				size++;
			}
		}
		return size < count;
	}

	@Override
	public void addActionCount(String keyword, long minuteAfter, byte type) {
		BoundSetOperations<String, Long> setOperations = redisTemplate.boundSetOps("firewallActionCount:"+keyword+"-"+type);
		setOperations.add(System.currentTimeMillis() + minuteAfter*60*1000);
		setOperations.expire(minuteAfter, TimeUnit.MINUTES);
	}

}
