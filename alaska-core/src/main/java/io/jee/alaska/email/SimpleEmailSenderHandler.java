package io.jee.alaska.email;

import javax.annotation.Resource;
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
	private String personal;
	
	public SimpleEmailSenderHandler(String personal) {
		super();
		this.personal = personal;
	}

	@Override
	public Result<?> send(String to, String subject, String text, boolean html, String personal) {
		try {
			MimeMessage mimeMessage = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
			helper.setTo(to);
			if(StringUtils.hasText(personal)){
				helper.setFrom(username, personal);
			}else{
				helper.setFrom(username, this.personal);
			}
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
	public Result<?> sendHtml(String to, String subject, String html) {
		return this.send(to, subject, html, true, null);
	}
	
	@Override
	public Result<?> sendHtml(String to, String subject, String html, String personal) {
		return this.send(to, subject, html, true, personal);
	}

	@Resource
	public void setSender(JavaMailSender sender) {
		this.sender = sender;
		username = ((JavaMailSenderImpl)sender).getUsername();
	}
	
}
