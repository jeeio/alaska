package io.jee.alaska.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Test
	public void test(){
		
//		FirewallActionCount actionCount = new FirewallActionCount();
//		actionCount.setKeyword("aaa-1");
//		actionCount.setTimeout(10l);
//		actionCountRepository.save(actionCount);
//		actionCountRepository.delete("d96ff94b-3ac9-476c-9eae-38fdc167b934");
//		System.out.println(actionCountRepository.findByKeyword("aaa-1"));
		
//		System.out.println(stringRedisTemplate.boundSetOps("aaa").add(RandomStringUtils.random(5)));
		System.out.println(stringRedisTemplate.boundSetOps("aaa").getExpire());
		System.out.println(stringRedisTemplate.boundSetOps("aaa").expire(30, TimeUnit.SECONDS));
		System.out.println(stringRedisTemplate.boundSetOps("aaa").getExpire());
		
		System.out.println(stringRedisTemplate.boundSetOps("aaa").members());
		
//		System.out.println(stringRedisTemplate.opsForList().size("bbb"));
	}

}
