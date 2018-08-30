package io.jee.alaska.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alaska")
public class AlaskaProperties {
	
	private Aliyun aliyun = new Aliyun();
	/**
	 * 签名（邮件、短信等）
	 */
	private String personal;
	
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
