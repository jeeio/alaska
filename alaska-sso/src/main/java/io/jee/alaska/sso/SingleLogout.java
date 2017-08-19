package io.jee.alaska.sso;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface SingleLogout {

	String INPUT = "logout-input";

	@Input(SingleLogout.INPUT)
	SubscribableChannel input();

}