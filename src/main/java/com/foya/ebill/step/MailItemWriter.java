package com.foya.ebill.step;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;


public class MailItemWriter implements ItemWriter<Map<String, Object>> {
	@Autowired
	private JavaMailSender javaMailSender;
	private Log log = LogFactory.getLog(MailItemWriter.class);
	@Autowired
	private JdbcTemplate jdbcTemplate; 
	@Override
	public void write(List<? extends Map<String, Object>> items) throws Exception {
		
	}
	
	
	public void sendEMail(EmailDetail detail) throws Exception {
		MimeMessage msg = javaMailSender.createMimeMessage();
		// true = multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		if(!StringUtils.isEmpty(detail.getTo())) {
			if(detail.getTo().contains(",")) {
				helper.setTo(detail.getTo().split(","));
			}else {
				helper.setTo(detail.getTo());
			}
		}
		if(!StringUtils.isEmpty(detail.getCc())) {
			if(detail.getCc().contains(",")) {
				helper.setCc(detail.getCc().split(","));
			}else {
				helper.setCc(detail.getCc());
			}
		}
		if(!StringUtils.isEmpty(detail.getSubject())) {
			helper.setSubject(detail.getSubject());
		}
		if(!StringUtils.isEmpty(detail.getText())) {
			helper.setText(detail.getText(),true);
		}
		if(!StringUtils.isEmpty(detail.getAttachmentPath())) {
			FileSystemResource file = new FileSystemResource(detail.getAttachmentPath());
			helper.addAttachment(file.getFilename(), file);
		}
		log.info("email to:"+detail.getTo());
		log.info("email cc:"+detail.getCc());
		log.info("email attachmentPath:"+detail.getAttachmentPath());
		javaMailSender.send(msg);
	}
	
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}


 class EmailDetail {
	 String to;
	 String cc;
	 String subject;
	 String text;
	 String attachmentPath;
	 String allMail;
	 public EmailDetail() {
		 
	 }
	 
	 public EmailDetail(String to ,String cc ,String subject,String text,String attachmentPath) {
		 this.to = to;
		 this.cc = cc;
		 this.subject = subject;
		 this.text = text;
		 this.attachmentPath = attachmentPath;
	 }
	 
	public String getAllMail() {
		if(!StringUtils.isEmpty(to) && !StringUtils.isEmpty(cc)) {
			allMail = to.concat(",").concat(cc);
		}else if(!StringUtils.isEmpty(to) && StringUtils.isEmpty(cc)){
			allMail = to;
		}else if(StringUtils.isEmpty(to) && !StringUtils.isEmpty(cc)){
			allMail = cc;
		}
		return allMail;
	}

	public void setAllMail(String allMail) {
		this.allMail = allMail;
	}

	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAttachmentPath() {
		return attachmentPath;
	}
	public void setAttachmentPath(String attachmentPath) {
		this.attachmentPath = attachmentPath;
	}
	 
 }
