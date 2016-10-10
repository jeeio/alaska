package io.jee.alaska.sso.server.ticket;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="sso_ticket")
public class SSOTicket {

	private String ticket;
	private String username;
	private long time;

	@Id
	@GenericGenerator(name="uuid", strategy="org.hibernate.id.UUIDGenerator")
	@GeneratedValue(generator="uuid")
	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}