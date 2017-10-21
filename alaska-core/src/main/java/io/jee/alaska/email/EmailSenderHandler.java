package io.jee.alaska.email;

import io.jee.alaska.util.Result;

public interface EmailSenderHandler {
	
	Result<?> send(String to, String subject, String text, boolean html, String nickname);
	
	Result<?> send(String to[], String subject, String text, boolean html, String nickname);
	
	Result<?> sendHtml(String to, String subject, String html);
	
	Result<?> sendHtml(String to, String subject, String html, String nickname);

}