package io.jee.alaska.firewall;

public interface FirewallService {
	
	boolean verifyActionCount(String keyword, int count, byte type);
	
	void addActionCount(String keyword, long minuteAfter, byte type);
	
}
