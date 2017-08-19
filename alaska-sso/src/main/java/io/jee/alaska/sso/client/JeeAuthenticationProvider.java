package io.jee.alaska.sso.client;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.jee.alaska.sso.SSOProperties;
import io.jee.alaska.sso.TicketService;
import io.jee.alaska.sso.TicketVerify;

public class JeeAuthenticationProvider implements AuthenticationProvider {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private UserDetailsService userDetailsService;
	
	@Resource
	private SSOProperties ssoProperties;
	@Resource
	private TicketService ticketService;
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		if (!supports(authentication.getClass())) {
            return null;
        }
		
		UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
		String ticket = authenticationToken.getCredentials().toString();
		
		String username = "";
		try {
			TicketVerify resultVerify = ticketService.verifyTicket(ticket);
			if(resultVerify.isSuccess()){
				username = resultVerify.getUsername();
			}else{
				throw new AuthenticationCredentialsNotFoundException("令牌错误");
			}
		}catch(Exception e){
			logger.error("令牌错了", e);
			throw new BadCredentialsException(e.getMessage(), e);
		}
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		JeeAuthenticationToken result = new JeeAuthenticationToken(userDetails, ticket, userDetails.getAuthorities());
		result.setDetails(authenticationToken.getDetails());
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (JeeAuthenticationToken.class.isAssignableFrom(authentication));
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

}
