package io.jee.alaska.sso.client;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;

public class JeeExceptionTranslationFilter extends ExceptionTranslationFilter {
	
	public JeeExceptionTranslationFilter(AuthenticationEntryPoint authenticationEntryPoint) {
		super(authenticationEntryPoint);
	}
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String tologin = request.getParameter("sso.login");
    	if(tologin!=null&&request.getMethod().endsWith("GET")){
    		if(super.getAuthenticationTrustResolver().isAnonymous(SecurityContextHolder.getContext().getAuthentication())){
    			super.sendStartAuthentication(request, response, chain, null);
    		}else{
    			String redirectUrl = request.getRequestURI();
    			response.sendRedirect(redirectUrl);
    		}
    	}else{
    		chain.doFilter(request, response);
    	}
	}

}
