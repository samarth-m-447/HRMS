package com.scii.util;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

//import javax.swing.text.Document;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import com.scii.model.MailTemplate;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailUtil {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private CommonUtil commonUtil;
	
	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	public Session getMailSession() throws IOException {
		Properties environment = commonUtil.loadPropertyFile("mail.properties");
		Properties sessionProperties = new Properties();
		sessionProperties.put("mail.smtp.host", environment.getProperty("in.scii.mail.smtphost")); //SMTP Host
		sessionProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
		sessionProperties.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
		sessionProperties.put("mail.smtp.port", environment.getProperty("in.scii.mail.smtpport")); //SMTP Port
		
		final String fromEmail = environment.getProperty("in.scii.mail.frommail");
		final String password = environment.getProperty("in.scii.mail.password");
		
		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		return Session.getInstance(sessionProperties, auth);
	}
	
	/**
	 * 
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 * @param noreplay
	 * @return
	 */
	public boolean sendEmail(Session session, String toEmail, String subject, String body, String noreply){
		boolean mailStatus = false;
		try {
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.addHeader("Content-type", "text/html; charset=UTF-8");
			mimeMessage.addHeader("format", "flowed");
			mimeMessage.addHeader("Content-Transfer-Encoding", "8bit");
			
			mimeMessage.setFrom(new InternetAddress(noreply, ""));
			mimeMessage.setReplyTo(InternetAddress.parse(noreply, false));
			
			mimeMessage.setSubject(subject, "UTF-8");
			mimeMessage.setContent(body, "text/html; charset=utf-8");
			mimeMessage.setSentDate(new Date());
			mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
			Transport.send(mimeMessage);
			mailStatus = true;
		} catch (Exception e) {
			logger.error(e);
			return mailStatus;
		}
		return mailStatus;
	}
	
	/**
	 * 
	 * @param mailType
	 * @return
	 */
	public String getEmailTemplate() {

		String body = StringUtils.EMPTY;
		try{
			body = "Hello $userName$ ,<br><br>\r\n"
					+ "	You have requested to change the Login Password.<br>\r\n"
					+ "	Please click the below link to change your Login Password.<br><br>\r\n"
					+ "	<a href=\"$resetPasswordLink$\">$url$</a><br><br>\r\n"
					+ "	<b>Note :</b> Above Link is valid once and is valid for 24 hours.<br><br>\r\n"
					+ "	If you have not requested for Password change, please ignore this mail.<br><br>\r\n"
					+ "	<p style=\"font-size:14px;color:red;\">This is a system generated mail. Please do not reply to this mail.</p><br>\r\n"
					+ "	Thanks & Regards,<br>\r\n"
					+ "	Human Resource Management System";
		} catch (Exception e) {
			logger.error(e);
		}
		return body;
	}
}
