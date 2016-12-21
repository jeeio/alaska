package io.jee.alaska.alibaba.alidayu;

import java.util.Map;

import io.jee.alaska.util.Result;

public interface ShortMessagingHandler {

	Result<?> send(String signName, Map<String, String> param, String template, String... mobiles);
	
	Result<?> send(Map<String, String> param, String template, String... mobiles);
	
}
