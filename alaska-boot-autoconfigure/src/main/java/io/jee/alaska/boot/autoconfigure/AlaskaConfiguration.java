package io.jee.alaska.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

import io.jee.alaska.alibaba.aliyun.AliyunSmsHandler;
import io.jee.alaska.email.EmailSenderHandler;
import io.jee.alaska.email.SimpleEmailSenderHandler;
import io.jee.alaska.httpclient.DefaultHttpClientHelper;
import io.jee.alaska.httpclient.HttpClientHelper;
import io.jee.alaska.pay.alipay.AlipayConfig;
import io.jee.alaska.pay.alipay.AlipayService;
import io.jee.alaska.pay.alipay.AlipayServiceImpl;
import io.jee.alaska.pay.wxpay.WXPayConfig;
import io.jee.alaska.pay.wxpay.WXPayService;
import io.jee.alaska.pay.wxpay.WXPayServiceImpl;
import io.jee.alaska.pay.wxpay.sdk.WXPay;

@Configuration
@EnableConfigurationProperties({AlaskaProperties.class, AlaskaPayProperties.class})
public class AlaskaConfiguration {
	
	@ConditionalOnClass(name = "io.jee.alaska.httpclient.HttpClientHelper")
	static class CoreConfiguration{
		
		@Bean
		@ConditionalOnMissingBean
		public HttpClientHelper httpClientHelper(){
			return new DefaultHttpClientHelper();
		}
		
	}
	
	@ConditionalOnClass(name = "io.jee.alaska.pay.alipay.AlipayService")
	static class AlipayConfiguration{
		
		private AlaskaPayProperties properties;
		
		public AlipayConfiguration(AlaskaPayProperties properties) {
			this.properties = properties;
		}
		
		@Bean
		@ConditionalOnProperty(prefix = "alaska.pay.alipay", name="app-id")
		@ConditionalOnMissingBean
		public AlipayClient alipayClient(){
			return new DefaultAlipayClient(properties.getAlipay().isSandbox()?AlipayConfig.gatewayUrl_sandbox:AlipayConfig.gatewayUrl,
					properties.getAlipay().getAppId(), properties.getAlipay().getMerchantPrivateKey(),
					"json", AlipayConfig.charset, properties.getAlipay().getAlipayPublicKey(), AlipayConfig.sign_type);
		}
		
		@Bean
		@ConditionalOnProperty(prefix = "alaska.pay.alipay", name="app-id")
		@ConditionalOnMissingBean
		public AlipayService alipayService(){
			return new AlipayServiceImpl();
		}
		
	}
	
	@ConditionalOnClass(name = "io.jee.alaska.pay.wxpay.WXPayService")
	static class WXPayConfiguration{
		
		private AlaskaPayProperties properties;
		
		public WXPayConfiguration(AlaskaPayProperties properties) {
			this.properties = properties;
		}
		
		@Bean
		@ConditionalOnProperty(prefix = "alaska.pay.wxpay", name="app-id")
		@ConditionalOnMissingBean
		public WXPay wxpay() throws Exception{
			WXPayConfig wxPayConfig = new WXPayConfig(properties.getWxpay().getAppId(), properties.getWxpay().getMchId(), properties.getWxpay().getMchKey());
			if(properties.getWxpay().isSandbox()) {
				return new WXPay(wxPayConfig, true, true);
			}else {
				return new WXPay(wxPayConfig);
			}
			
		}
		
		@Bean
		@ConditionalOnProperty(prefix = "alaska.pay.wxpay", name="app-id")
		@ConditionalOnMissingBean
		public WXPayService wxpayService(){
			return new WXPayServiceImpl();
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
