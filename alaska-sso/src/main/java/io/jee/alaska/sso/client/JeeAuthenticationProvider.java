package io.jee.alaska.sso.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jee.alaska.sso.SSOConstant;
import io.jee.alaska.sso.TicketVerify;

public class JeeAuthenticationProvider implements AuthenticationProvider, InitializingBean, DisposableBean {
	
	private UserDetailsService userDetailsService;
	
	private Environment env;
	
	private CloseableHttpClient httpClient = null;
	
	@Resource
	public void setEnv(Environment env) {
		this.env = env;
	}
	
	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		if (!supports(authentication.getClass())) {
            return null;
        }
		
		UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
		String ticket = authenticationToken.getCredentials().toString();
		
		String username = "";
		TicketVerify resultVerify = null;
		try {
			List<NameValuePair> parameters = new ArrayList<>();
			parameters.add(new BasicNameValuePair("ticket", ticket));
			
			StringBuffer validationUrl = new StringBuffer(env.getRequiredProperty("member.url"));
			validationUrl.append(SSOConstant.SSO_VERIFY_URL);
			
			HttpPost httpPost = new HttpPost(validationUrl.toString());
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
			httpPost.setEntity(formEntity);
			
			CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
			
			String centerResult = EntityUtils.toString(httpResponse.getEntity());
			
			HttpClientUtils.closeQuietly(httpResponse);
			
			ObjectMapper mapper = new ObjectMapper();
			resultVerify = mapper.readValue(centerResult, TicketVerify.class);
			if(resultVerify.isSuccess()){
				username = resultVerify.getUsername();
			}else{
				throw new AuthenticationCredentialsNotFoundException("令牌错误");
			}
		}catch(IOException e){
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


	@Override
	public void destroy() throws Exception {
		HttpClientUtils.closeQuietly(httpClient);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContext.getInstance(SSLConnectionSocketFactory.SSL);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		X509TrustManager tm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		try {
			sslcontext.init(null, new TrustManager[] { tm }, null);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		httpClient = HttpClients.custom().setSSLContext(sslcontext).build();
	}

}
