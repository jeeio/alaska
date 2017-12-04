package io.jee.alaska.email;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import io.jee.alaska.util.Result;

public class SimpleEmailSenderHandler implements EmailSenderHandler {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private JavaMailSender sender;
	private String username;
	

	@Override
	public Result<?> send(String subject, String text, String nickname, boolean html, String... to) {
		try {
			MimeMessage mimeMessage = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setTo(to);
			helper.setFrom(username, nickname);
			helper.setSubject(subject);
			helper.setText(text, html);
			sender.send(mimeMessage);
			return Result.success();
		} catch (Exception e) {
			logger.warn("邮件发送失败："+e.getMessage());
			return Result.error(e.getMessage());
		}
	}

	@Override
	public Result<?> sendHtml(String subject, String html, String personal, String to) {
		return this.send(subject, html, personal, true, to);
	}

	@Resource
	public void setSender(JavaMailSender sender) {
		this.sender = sender;
		username = ((JavaMailSenderImpl)sender).getUsername();
	}
	
}
