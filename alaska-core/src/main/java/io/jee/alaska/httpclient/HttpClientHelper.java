package io.jee.alaska.httpclient;

import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

public interface HttpClientHelper {
	
	HttpResult post(String uri, List <NameValuePair> nvps);
	
	HttpResult post(String uri, int connectionRequestTimeout, int connectTimeout, int socketTimeout, List <NameValuePair> nvps);
	
	HttpResult post(String uri, String content);
	
	HttpResult post(String uri, int connectionRequestTimeout, int connectTimeout, int socketTimeout, String content);
	
	HttpResult post(HttpPost httpPost);
	
	HttpResult get(String uri);
	
	HttpResult get(String uri, int connectionRequestTimeout, int connectTimeout, int socketTimeout);
	
	HttpResult get(HttpGet httpGet);
	
	HttpResult execute(HttpRequestBase httpRequest, int connectionRequestTimeout, int connectTimeout, int socketTimeout);
	
	String builderGetUri(String scheme, String host, int port, String path, List <NameValuePair> nvps);

}
