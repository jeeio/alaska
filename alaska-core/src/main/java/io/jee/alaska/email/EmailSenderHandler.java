package io.jee.alaska.email;

import io.jee.alaska.util.Result;

public interface EmailSenderHandler {
	
	Result<?> send(String subject, String text, String nickname, boolean html, String... to);
	
	Result<?> sendHtml(String subject, String html, String nickname, String to);

	Result<?> send(String subject, String text, String nickname, String from, boolean html, String... to);
	
}