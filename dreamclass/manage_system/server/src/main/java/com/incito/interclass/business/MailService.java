package com.incito.interclass.business;

import java.io.File;
import java.text.SimpleDateFormat;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.incito.interclass.common.MailConfig;
import com.incito.interclass.entity.Log;

@Service
public class MailService {

	private JavaMailSender sender;
	private MailConfig mailConfig;
	//采用配置文件注入
	public void setSender(JavaMailSender sender) {
		this.sender = sender;
	}

	public void setMailConfig(MailConfig mailConfig) {
		this.mailConfig = mailConfig;
	}

	public void sendMessage(String from, String[] to, String subject,
			String text, File attachment) throws MessagingException {
		MimeMessage msg = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true, "GB2312");

		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);
		FileSystemResource logFile = new FileSystemResource(attachment);
		helper.addAttachment(attachment.getName(), logFile);

		sender.send(msg);
	}

	public void sendErrorLogMail(Log log) throws MessagingException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String from = mailConfig.getSender();
		String[] to = mailConfig.getReceivers();
		String title = "错误日志——" + sdf.format(log.getCtime());
		String content = "";
		sendMessage(from, to, title, content, new File(log.getUrl()));
	}
}
