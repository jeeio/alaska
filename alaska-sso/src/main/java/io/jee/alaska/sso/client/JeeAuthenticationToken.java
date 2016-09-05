package io.jee.alaska.sso.client;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JeeAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = -2330568932417388817L;
	
	public JeeAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}
	
	public JeeAuthenticationToken(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

}
