package io.jee.alaska.boot.autoconfigure;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alaska.security")
public class AlaskaSecurityProperties {

	private String eurekaUsername, eurekaPassword;
	private String configUsername, configPassword;
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

	public String getConfigUsername() {
		return configUsername;
	}

	public void setConfigUsername(String configUsername) {
		this.configUsername = configUsername;
	}

	public String getConfigPassword() {
		return configPassword;
	}

	public void setConfigPassword(String configPassword) {
		this.configPassword = configPassword;
	}

	public Map<String, String> getService() {
		return service;
	}

	public void setService(Map<String, String> service) {
		this.service = service;
	}

}
