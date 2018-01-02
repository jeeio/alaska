package io.jee.alaska.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alaska")
public class AlaskaProperties {
	
	private Alipay alipay = new Alipay();
	private Aliyun aliyun = new Aliyun();
	/**
	 * 签名（邮件、短信等）
	 */
	private String personal;
	
	public Alipay getAlipay() {
		return alipay;
	}

	public void setAlipay(Alipay alipay) {
		this.alipay = alipay;
	}
	
	public Aliyun getAliyun() {
		return aliyun;
	}

	public void setAliyun(Aliyun aliyun) {
		this.aliyun = aliyun;
	}
	
	public String getPersonal() {
		return personal;
	}

	public void setPersonal(String personal) {
		this.personal = personal;
	}

	public class Alipay {
		
		private String appId, merchantPrivateKey, alipayPublicKey;
		private boolean sandbox;

		public String getAppId() {
			return appId;
		}

		public void setAppId(String appId) {
			this.appId = appId;
		}

		public String getMerchantPrivateKey() {
			return merchantPrivateKey;
		}

		public void setMerchantPrivateKey(String merchantPrivateKey) {
			this.merchantPrivateKey = merchantPrivateKey;
		}

		public String getAlipayPublicKey() {
			return alipayPublicKey;
		}

		public void setAlipayPublicKey(String alipayPublicKey) {
			this.alipayPublicKey = alipayPublicKey;
		}

		public boolean isSandbox() {
			return sandbox;
		}

		public void setSandbox(boolean sandbox) {
			this.sandbox = sandbox;
		}
		
	}
	
	public class Aliyun {
		
		private String keyId, keySecret;

		public String getKeyId() {
			return keyId;
		}

		public void setKeyId(String keyId) {
			this.keyId = keyId;
		}

		public String getKeySecret() {
			return keySecret;
		}

		public void setKeySecret(String keySecret) {
			this.keySecret = keySecret;
		}
	}
	
}
