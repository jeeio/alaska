package io.jee.alaska.sso.client;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import io.jee.alaska.util.UrlUtils;

public class JeeAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private Environment env;

	@Resource
	public void setEnv(Environment env) {
		this.env = env;
	}

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {

		StringBuilder buffer = new StringBuilder(env.getRequiredProperty("member.url"));
		buffer.append("/login");
		buffer.append("?service=");
		buffer.append(UrlUtils.currentProject(request));
		response.sendRedirect(buffer.toString());
	}

}
