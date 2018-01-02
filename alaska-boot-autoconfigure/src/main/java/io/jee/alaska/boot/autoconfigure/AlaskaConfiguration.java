package io.jee.alaska.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.jee.alaska.alibaba.alipay.AlipayService;
import io.jee.alaska.alibaba.alipay.AlipayServiceImpl;
import io.jee.alaska.alibaba.aliyun.AliyunSmsHandler;
import io.jee.alaska.email.EmailSenderHandler;
import io.jee.alaska.email.SimpleEmailSenderHandler;
import io.jee.alaska.httpclient.DefaultHttpClientHelper;
import io.jee.alaska.httpclient.HttpClientHelper;

@Configuration
@EnableConfigurationProperties(AlaskaProperties.class)
public class AlaskaConfiguration {
	
	@Bean
	@ConditionalOnClass(HttpClientHelper.class)
	@ConditionalOnMissingBean
	public HttpClientHelper httpClientHelper(){
		return new DefaultHttpClientHelper();
	}
	
	@ConditionalOnClass(name = "io.jee.alaska.alibaba.alipay.AlipayService")
	static class AlipayConfiguration{
		
		private AlaskaProperties properties;
		
		public AlipayConfiguration(AlaskaProperties properties) {
			this.properties = properties;
		}
		
		@Bean
		@ConditionalOnProperty(prefix = "alaska.alipay", name="app-id")
		@ConditionalOnMissingBean
		public AlipayService alipayService(){
			return new AlipayServiceImpl(properties.getAlipay().getAppId(), properties.getAlipay().getMerchantPrivateKey(), properties.getAlipay().getAlipayPublicKey(), properties.getAlipay().isSandbox());
		}
		
	}
	
	@ConditionalOnClass(name = "io.jee.alaska.alibaba.aliyun.AliyunSmsHandler")
	static class AliyunSmsConfiguration{
		
		private AlaskaProperties properties;
		
		public AliyunSmsConfiguration(AlaskaProperties properties) {
			this.properties = properties;
		}
		
		@Bean
		@ConditionalOnProperty(prefix = "alaska.aliyun", name="key-id")
		@ConditionalOnMissingBean
		public AliyunSmsHandler aliyunSmsHandler(){
			return new AliyunSmsHandler(properties.getAliyun().getKeyId(), properties.getAliyun().getKeySecret());
		}
		
	}
	
	@ConditionalOnClass(name = {"javax.mail.internet.MimeMessage","org.springframework.mail.javamail.JavaMailSender"})
	static class EmailConfiguration{
		
		@Bean
		@ConditionalOnMissingBean
		public EmailSenderHandler emailSenderHandler(){
			return new SimpleEmailSenderHandler();
		}
	}

}
