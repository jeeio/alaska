package io.jee.alaska.firewall.spring.jpa;

import java.util.concurrent.TimeUnit;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FirewallTempStorage {

	private String key;
	private String content;
	private long expire;

	@Id
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}
	
	public static void main(String[] args) {
		System.out.println(TimeUnit.MINUTES.toMillis(1));
		
	}

}
