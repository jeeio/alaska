package io.jee.alaska.httpclient;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class DefaultHttpClientHelper implements HttpClientHelper, InitializingBean, DisposableBean {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private CloseableHttpClient httpClient = null;
	
	@Override
	public HttpResult post(String uri, List <NameValuePair> nvps){
		return this.post(uri, 30000, 10000, 30000, nvps);
	}
	
	@Override
	public HttpResult post(String uri, int connectionRequestTimeout, int connectTimeout, int socketTimeout, List <NameValuePair> nvps){
		HttpPost httpPost = new HttpPost(uri);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			return this.execute(httpPost, connectionRequestTimeout, connectTimeout, socketTimeout);
		} catch (UnsupportedEncodingException e) {
			logger.warn(e.getMessage());
			HttpResult result = new HttpResult();
			result.setSuccess(false);
			result.setContent(e.getMessage());
			return result;
		}
	}
	
	@Override
	public HttpResult post(String uri, String content){
		return this.post(uri, 30000, 10000, 30000, content);
	}
	
	@Override
	public HttpResult post(String uri, int connectionRequestTimeout, int connectTimeout, int socketTimeout, String content){
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setEntity(new StringEntity(content, "UTF-8"));
		return this.execute(httpPost, connectionRequestTimeout, connectTimeout, socketTimeout);
	}
	
	@Override
	public HttpResult post(HttpPost httpPost){
		return this.execute(httpPost, 30000, 10000, 30000);
	}
	
	@Override
	public HttpResult get(String uri){
		return this.get(uri, 30000, 10000, 30000);
	}
	
	@Override
	public byte[] getBody(String uri){
		HttpGet httpGet = new HttpGet(uri);
		return this.executeBody(httpGet, 30000, 10000, 30000);
	}
	
	@Override
	public HttpResult get(String uri, int connectionRequestTimeout, int connectTimeout, int socketTimeout){
		HttpGet httpGet = new HttpGet(uri);
		return this.execute(httpGet, connectionRequestTimeout, connectTimeout, socketTimeout);
	}
	
	@Override
	public HttpResult get(HttpGet httpGet) {
		return this.execute(httpGet, 30000, 10000, 30000);
	}
	
	@Override
	public byte[] executeBody(HttpRequestBase httpRequest, int connectionRequestTimeout, int connectTimeout,
			int socketTimeout) {
		CloseableHttpResponse httpResponse = null;
		try{
			RequestConfig.Builder builder = RequestConfig.custom();
			if(connectionRequestTimeout>0){
				builder.setConnectionRequestTimeout(connectionRequestTimeout);
			}
			if(connectTimeout>0){
				builder.setConnectTimeout(connectTimeout);
			}
			if(socketTimeout>0){
				builder.setSocketTimeout(socketTimeout);
			}
			httpRequest.setConfig(builder.build());
			httpResponse = httpClient.execute(httpRequest);
			return EntityUtils.toByteArray(httpResponse.getEntity());
		} catch (Exception e){
		}finally{
			HttpClientUtils.closeQuietly(httpResponse);
		}
		return null;
	}
	
	@Override
	public HttpResult execute(HttpRequestBase httpRequest, int connectionRequestTimeout, int connectTimeout,
			int socketTimeout) {
		CloseableHttpResponse httpResponse = null;
		HttpResult result = new HttpResult();
		try{
			RequestConfig.Builder builder = RequestConfig.custom();
			if(connectionRequestTimeout>0){
				builder.setConnectionRequestTimeout(connectionRequestTimeout);
			}
			if(connectTimeout>0){
				builder.setConnectTimeout(connectTimeout);
			}
			if(socketTimeout>0){
				builder.setSocketTimeout(socketTimeout);
			}
			httpRequest.setConfig(builder.build());
			httpResponse = httpClient.execute(httpRequest);
			
			result.setSuccess(true);
			result.setStatusCode(httpResponse.getStatusLine().getStatusCode());
			result.setContent(EntityUtils.toString(httpResponse.getEntity(), Consts.UTF_8));
		} catch (Exception e){
			result.setSuccess(false);
			result.setStatusCode(-1);
			result.setContent(e.getMessage());
		}finally{
			HttpClientUtils.closeQuietly(httpResponse);
		}
		return result;
	}
	
	@Override
	public String builderGetUri(String scheme, String host, int port, String path, List <NameValuePair> nvps){
		URIBuilder uriBuilder = new URIBuilder();
		uriBuilder.setScheme(scheme);
		uriBuilder.setHost(host);
		uriBuilder.setPort(port);
		uriBuilder.setPath(path);
		uriBuilder.setParameters(nvps);
		return uriBuilder.toString();
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
		
		httpClient = HttpClients.custom().setSSLContext(sslcontext).setMaxConnPerRoute(50).setMaxConnTotal(500).build();
	}

}
