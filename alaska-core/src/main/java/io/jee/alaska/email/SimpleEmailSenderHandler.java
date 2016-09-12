package io.jee.alaska.email;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import io.jee.alaska.util.Result;

public class SimpleEmailSenderHandler implements EmailSenderHandler {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private JavaMailSender sender;
	private String username;
	private String nickname;
	
	public SimpleEmailSenderHandler(String nickname) {
		super();
		this.nickname = nickname;
	}

	@Override
	public Result<?> send(String to, String subject, String text, boolean html, String nickname) {
		MimeMessage mimeMessage = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		try {
			helper.setTo(to);
			if(StringUtils.hasText(nickname)){
				helper.setFrom(username, nickname);
			}else{
				helper.setFrom(username, this.nickname);
			}
			helper.setSubject(subject);
			helper.setText(text, html);
			sender.send(mimeMessage);
			return Result.success();
		} catch (UnsupportedEncodingException | MessagingException e) {
			logger.warn("邮件发送失败："+e.getMessage());
			return Result.error(e.getMessage());
		}
	}

	@Override
	public Result<?> sendHtml(String to, String subject, String html) {
		return this.send(to, subject, html, true, null);
	}
	
	@Override
	public Result<?> sendHtml(String to, String subject, String html, String nickname) {
		return this.send(to, subject, html, true, nickname);
	}

	@Resource
	public void setSender(JavaMailSender sender) {
		this.sender = sender;
		username = ((JavaMailSenderImpl)sender).getUsername();
	}
	
}
