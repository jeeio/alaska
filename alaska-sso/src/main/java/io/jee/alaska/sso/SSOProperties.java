package io.jee.alaska.sso;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alaska.sso")
public class SSOProperties {
	
	private String rootDomain;
	private String[] rootDomains;
	
	public String getRootDomain() {
		return rootDomain;
	}

	public void setRootDomain(String rootDomain) {
		this.rootDomain = rootDomain;
	}

	public String[] getRootDomains() {
		return rootDomains;
	}

	public void setRootDomains(String[] rootDomains) {
		this.rootDomains = rootDomains;
	}
	
}
