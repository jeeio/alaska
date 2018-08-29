package io.jee.alaska.firewall.spring.data.jpa;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("io.jee.alaska.firewall.spring.data.jpa")
@EnableJpaRepositories("io.jee.alaska.firewall.spring.data.jpa")
public class FirewallSpringJpaConfig {
	
}
