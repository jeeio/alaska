package io.jee.alaska.firewall;

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
	
}
