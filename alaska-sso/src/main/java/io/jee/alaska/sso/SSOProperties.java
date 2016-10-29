package io.jee.alaska.sso;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alaska.sso")
public class SSOProperties {
	
	private String[] rootDomains;
	private String url;
	private String cid;
	
	public String[] getRootDomains() {
		return rootDomains;
	}

	public void setRootDomains(String[] rootDomains) {
		this.rootDomains = rootDomains;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}
	
}
