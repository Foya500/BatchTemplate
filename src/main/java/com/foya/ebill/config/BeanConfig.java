package com.foya.ebill.config;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration

public class BeanConfig {
	@Value("${spring.mail.host}")
	private String host;
	@Value("${spring.mail.port}")
	private int port;
	@Value("${spring.mail.username}")
	private String username;
	@Value("${spring.mail.password}")
	private String password;
	@Value("${spring.mail.properties.mail.smtp.auth}")
	private Boolean auth;
	@Value("${spring.mail.properties.mail.smtp.starttls.enable}")
	private Boolean starttls;
	
	@Bean
	public JavaMailSender javaMailService() throws UnsupportedEncodingException {
	    JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
	    if (this.auth) {
	        javaMailSender.setUsername(this.username);
	        javaMailSender.setPassword(new String(Base64.getDecoder().decode(this.password), "UTF-8"));
	    }
	    Properties properties = new Properties();
//	    properties.setProperty("mail.transport.protocol", this.protocol);
	    properties.setProperty("mail.smtp.auth", Boolean.toString(this.auth));
	    properties.setProperty("mail.smtp.starttls.enable", Boolean.toString(this.starttls));
//	    properties.setProperty("mail.debug", Boolean.toString(this.debug));
	    properties.setProperty("mail.smtp.host", this.host);
	    properties.setProperty("mail.smtp.port", Integer.toString(this.port));
//	    properties.setProperty("mail.smtp.ssl.trust", this.trust);
	    javaMailSender.setJavaMailProperties(properties);

	    return javaMailSender;
	}
	
}
