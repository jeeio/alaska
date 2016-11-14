package io.jee.alaska.firewall.spring.data.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jee.alaska.firewall.FirewallService;

@Service
public class FirewallRedisService implements FirewallService {
	
	@Autowired
	private FirewallActionCountRepository actionCountRepository;
	
	@Override
	public boolean verifyActionCount(String keyword, int count, byte type) {
		System.out.println(actionCountRepository.findByKeyword(keyword+"-"+type));
		return actionCountRepository.findByKeyword(keyword+"-"+type).size()<count;
	}

	@Override
	public void addActionCount(String keyword, long minuteAfter, byte type) {
		FirewallActionCount actionCount = new FirewallActionCount();
		actionCount.setKeyword(keyword+"-"+type);
		actionCount.setTimeout(minuteAfter*60);
		actionCountRepository.save(actionCount);
	}

}
