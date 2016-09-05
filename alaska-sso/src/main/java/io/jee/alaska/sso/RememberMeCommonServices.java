package io.jee.alaska.sso;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

public class RememberMeCommonServices extends TokenBasedRememberMeServices {
	
	@Resource
	private SSOProperties ssoProperties;
	
	public RememberMeCommonServices(String key, UserDetailsService userDetailsService) {
		super(key, userDetailsService);
	}
	
	@Override
	protected void setCookie(String[] tokens, int maxAge,
			HttpServletRequest request, HttpServletResponse response) {
		String cookieValue = encodeCookie(tokens);
        Cookie cookie = new Cookie(super.getCookieName(), cookieValue);
        String domain = request.getServerName();
        if(domain.endsWith(ssoProperties.getRootDomain())){
        	cookie.setDomain(ssoProperties.getRootDomain());
        }
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setSecure(request.isSecure());
        cookie.setHttpOnly(true);

        response.addCookie(cookie);
	}
	
	@Override
	protected void cancelCookie(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("Cancelling cookie");
        Cookie cookie = new Cookie(super.getCookieName(), null);
        String domain = request.getServerName();
        if(domain.endsWith(ssoProperties.getRootDomain())){
        	cookie.setDomain(ssoProperties.getRootDomain());
        }
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
	}
	
	@Override
	protected int calculateLoginLifetime(HttpServletRequest request,
			Authentication authentication) {
		String paramValue = request.getParameter(super.getParameter());

        if (paramValue != null) {
            if (paramValue.equalsIgnoreCase("true") || paramValue.equalsIgnoreCase("on") ||
                    paramValue.equalsIgnoreCase("yes") || paramValue.equals("1")) {
            	return super.calculateLoginLifetime(request, authentication);
            }
        }
        return -1;
	}
	
}
