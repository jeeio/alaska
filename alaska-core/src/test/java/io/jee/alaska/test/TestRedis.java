package io.jee.alaska.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.jee.alaska.firewall.spring.data.redis.FirewallActionCount;
import io.jee.alaska.firewall.spring.data.redis.FirewallActionCountRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {
	
	@Autowired
	private FirewallActionCountRepository actionCountRepository;
	
	@Test
	public void test(){
//		
		FirewallActionCount actionCount = new FirewallActionCount();
		actionCount.setKeyword("aaa-1");
		actionCount.setTimeout(1000l);
		actionCountRepository.save(actionCount);
		
		System.out.println(actionCountRepository.findByKeyword("aaa-1"));
		
		
	}

}
