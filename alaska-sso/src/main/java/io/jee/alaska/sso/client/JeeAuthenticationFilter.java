package io.jee.alaska.sso.client;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import io.jee.alaska.sso.SSOConstant;

public class JeeAuthenticationFilter extends
		AbstractAuthenticationProcessingFilter {
	
	public JeeAuthenticationFilter() {
		super(new JeeProcessUrlRequestMatcher());
		super.setAuthenticationFailureHandler(new SimpleUrlAuthenticationFailureHandler("/"));
	}
	
    @Override
    protected final void successfulAuthentication(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        boolean continueFilterChain = proxyTicketRequest(serviceTicketRequest(request, response),request);
        if(!continueFilterChain) {
            super.successfulAuthentication(request, response, chain, authResult);
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Authentication success. Updating SecurityContextHolder to contain: " + authResult);
        }

        SecurityContextHolder.getContext().setAuthentication(authResult);
        
        // Fire event
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }
        if(syncRequest(request)) {
        	response.addHeader("P3P", "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");
        	response.getWriter().write("");
        	super.getRememberMeServices().loginSuccess(request, response, authResult);
        	return;
        }
        chain.doFilter(request, response);
        
    }

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {
		
		final String username = "";
        String password = obtainArtifact(request);
        if (password == null) {
            logger.debug("Failed to obtain an artifact (cas ticket)");
            password = "";
        }

        final JeeAuthenticationToken authRequest = new JeeAuthenticationToken(username, password);

        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
		return this.getAuthenticationManager().authenticate(authRequest);
	}
	
	/**
     * Overridden to provide proxying capabilities.
     */
/*    @Override
    protected boolean requiresAuthentication(final HttpServletRequest request, final HttpServletResponse response) {
        final boolean serviceTicketRequest = serviceTicketRequest(request, response);
        final boolean result = serviceTicketRequest || syncRequest(request) || proxyTicketRequest(serviceTicketRequest, request);
        if(logger.isDebugEnabled()) {
            logger.debug("requiresAuthentication = "+result);
        }
        return result;
    }*/
    
    /**
     * Indicates if the request is elgible to process a proxy ticket.
     * @param request
     * @return
     */
    private boolean proxyTicketRequest(final boolean serviceTicketRequest, final HttpServletRequest request) {
        if(serviceTicketRequest) {
            return false;
        }
        final boolean result = obtainArtifact(request) != null;
        if(logger.isDebugEnabled()) {
            logger.debug("proxyTicketRequest = "+result);
        }
        return result;
    }
    
    /**
     * Indicates if the request is elgible to process a service ticket. This method exists for readability.
     * @param request
     * @param response
     * @return
     */
    private boolean serviceTicketRequest(final HttpServletRequest request, final HttpServletResponse response) {
    	final String requestUri = request.getRequestURI();
        final boolean result = requestUri.endsWith(SSOConstant.SSO_CHECK_URL);
        if(logger.isDebugEnabled()) {
            logger.debug("serviceTicketRequest = "+result);
        }
        return result;
    }
	
    /**
     * Indicates if the request is elgible to be processed as the proxy receptor.
     * @param request
     * @return
     */
    private boolean syncRequest(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final boolean result = requestUri.endsWith(SSOConstant.SSO_SYNC_URL);
        if(logger.isDebugEnabled()) {
            logger.debug("syncRequest = "+result);
        }
        return result;
    }
    
    /**
     * If present, gets the artifact (CAS ticket) from the {@link HttpServletRequest}.
     * @param request
     * @return if present the artifact from the {@link HttpServletRequest}, else null
     */
    protected String obtainArtifact(HttpServletRequest request) {
        return request.getParameter("ticket");
    }

    private static final class JeeProcessUrlRequestMatcher implements RequestMatcher {
    	private final String checkProcessesUrl = SSOConstant.SSO_CHECK_URL;
    	
        private final String syncProcessesUrl = SSOConstant.SSO_SYNC_URL;


        public boolean matches(HttpServletRequest request) {
            String uri = request.getRequestURI();
            int pathParamIndex = uri.indexOf(';');

            if (pathParamIndex > 0) {
                // strip everything after the first semi-colon
                uri = uri.substring(0, pathParamIndex);
            }

            if ("".equals(request.getContextPath())) {
                return uri.endsWith(checkProcessesUrl)||uri.endsWith(syncProcessesUrl);
            }

            return uri.endsWith(request.getContextPath() + checkProcessesUrl)||uri.endsWith(request.getContextPath() + syncProcessesUrl);
        }
    }
}
