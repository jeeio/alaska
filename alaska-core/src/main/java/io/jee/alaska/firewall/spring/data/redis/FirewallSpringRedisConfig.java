package io.jee.alaska.firewall.spring.data.redis;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@ComponentScan("io.jee.alaska.firewall.spring.data.redis")
@EnableRedisRepositories("io.jee.alaska.firewall.spring.data.redis")
public class FirewallSpringRedisConfig {
	
}
