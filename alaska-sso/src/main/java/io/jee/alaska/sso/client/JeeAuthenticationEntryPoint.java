package io.jee.alaska.sso.client;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import io.jee.alaska.sso.SSOProperties;

public class JeeAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Resource
	private SSOProperties ssoProperties;

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {

		StringBuilder buffer = new StringBuilder(ssoProperties.getUrl());
		buffer.append("/login");
		buffer.append("?cid=");
		buffer.append(ssoProperties.getCid());
		response.sendRedirect(buffer.toString());
	}

}
