package com.es.codinghub.api;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public class Mailing {

	private static String from = "coding.hub.es@gmail.com";
	private static String username = "coding.hub.es";
	private static String password = "hubbingcode";

	public static void send(String to, String subject, String content) throws MessagingException {

		Properties props = new Properties();

		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		message.setSubject(subject, "UTF-8");
		message.setContent(content, "text/html; charset=UTF-8");

		Transport.send(message);
	}

	public static Thread sendParallel(String to, String subject, String content) {

		return new Thread() {
			@Override
			public void run() {
				int retries = 5;
				while (retries>0) try {
					send(to, subject, content);
					break;
				}

				catch (MessagingException e) {
					retries -= 1;
				}
			}
		};
	}
}
