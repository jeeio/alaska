package io.jee.alaska.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alaska.pay")
public class AlaskaPayProperties {
	
	private Alipay alipay = new Alipay();
	private Wxpay wxpay = new Wxpay();
	
	public Alipay getAlipay() {
		return alipay;
	}

	public void setAlipay(Alipay alipay) {
		this.alipay = alipay;
	}
	
	public Wxpay getWxpay() {
		return wxpay;
	}

	public void setWxpay(Wxpay wxpay) {
		this.wxpay = wxpay;
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
	
	public class Wxpay {
		
		private String appId, mchId, mchKey;
		private boolean sandbox;

		public String getAppId() {
			return appId;
		}

		public void setAppId(String appId) {
			this.appId = appId;
		}

		public String getMchId() {
			return mchId;
		}

		public void setMchId(String mchId) {
			this.mchId = mchId;
		}

		public String getMchKey() {
			return mchKey;
		}

		public void setMchKey(String mchKey) {
			this.mchKey = mchKey;
		}

		public boolean isSandbox() {
			return sandbox;
		}

		public void setSandbox(boolean sandbox) {
			this.sandbox = sandbox;
		}

	}
	
}
