package io.jee.alaska.firewall;

import java.util.concurrent.TimeUnit;

import io.jee.alaska.firewall.spring.jpa.FirewallTempStorage;

public interface FirewallService {
	
	/**
	 * 
	 * @param keyword
	 * @param minuteAfter
	 * @param count
	 * @return sucess
	 */
	boolean verifyActionCount(String keyword, long minuteAfter, int count, byte type);
	
	void addActionCount(String keyword, byte type);
	
	void clearActionCount();
	
	void addTempStorage(String key, String content, long millisecond);
	
	void addTempStorage(String key, String content, TimeUnit timeUnit, long duration);
	
	FirewallTempStorage getTempStorage(String key);
	
	void removeTempStorage(FirewallTempStorage tempStorage);
	
	void clearTempStorage();

}
