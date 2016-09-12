package io.jee.alaska.sms;

import java.util.Map;

import io.jee.alaska.util.Result;

public interface ShortMessagingHandler {

	Result<?> send(String signName, Map<String, String> param, String template, String... mobiles);
	
}
