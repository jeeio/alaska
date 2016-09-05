package io.jee.alaska.sso;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alaska.sso")
public class SSOProperties {
	
	private String rootDomain = "localhost";

	public String getRootDomain() {
		return rootDomain;
	}

	public void setRootDomain(String rootDomain) {
		this.rootDomain = rootDomain;
	}

}
