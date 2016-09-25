package io.jee.alaska.firewall.spring.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FirewallTempStorage {

	private String keyword;
	private String content;
	private long expire;
	
	@Id
	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
	
}
