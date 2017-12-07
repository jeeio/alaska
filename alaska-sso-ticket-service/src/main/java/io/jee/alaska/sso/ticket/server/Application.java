package io.jee.alaska.sso.ticket.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

import io.jee.alaska.sso.ticket.redis.SSOTicketServiceRedisConfig;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableEurekaClient
@Import({SSOTicketServiceRedisConfig.class})
public class Application extends SpringBootServletInitializer {
	
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

}