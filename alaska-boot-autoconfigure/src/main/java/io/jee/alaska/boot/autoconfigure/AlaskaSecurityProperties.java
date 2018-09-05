package io.jee.alaska.boot.autoconfigure;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alaska.security")
public class AlaskaSecurityProperties {

	private String eurekaUsername, eurekaPassword;
	private String localUsername, localPassword;
	private Map<String, String> service;

	public String getEurekaUsername() {
		return eurekaUsername;
	}

	public void setEurekaUsername(String eurekaUsername) {
		this.eurekaUsername = eurekaUsername;
	}

	public String getEurekaPassword() {
		return eurekaPassword;
	}

	public void setEurekaPassword(String eurekaPassword) {
		this.eurekaPassword = eurekaPassword;
	}

	public String getLocalUsername() {
		return localUsername;
	}

	public void setLocalUsername(String localUsername) {
		this.localUsername = localUsername;
	}

	public String getLocalPassword() {
		return localPassword;
	}

	public void setLocalPassword(String localPassword) {
		this.localPassword = localPassword;
	}

	public Map<String, String> getService() {
		return service;
	}

	public void setService(Map<String, String> service) {
		this.service = service;
	}

}
