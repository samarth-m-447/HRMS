package com.scii.service;

import com.scii.model.Employee;

public interface MailService {
	
	/* public void sendCreateUserMail(Employee userModel)throws Exception; */
	
	public boolean sendForgotPasswordLinkMail(Employee userModel)throws Exception;

	/*
	 * void sendMailToContactPerson(TopicModel topicModel,String mails,String
	 * username) throws MalformedObjectNameException, UnknownHostException;
	 * 
	 * void sendAnswerMailToUser(QaHistoryModel qaHistoryModel) throws
	 * MalformedObjectNameException, UnknownHostException;
	 */
}
