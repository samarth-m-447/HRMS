package com.scii.serviceimpl;

import java.io.IOException;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.scii.service.EmployeeService;
import com.scii.mapper.EmployeeMapper;
import com.scii.model.Employee;
import com.scii.service.MailService;
import com.scii.util.CommonUtil;
import com.scii.util.MailUtil;
import jakarta.mail.Session;

@Service
public class MailServiceImpl implements MailService {
	
	@Autowired
	MailUtil mailUtil;
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Autowired
	private CommonUtil commonUtil;
	
	String host = new String();
	String path = new String();
	String port = new String();
	
	/**
	 * Method to send Password reset link in mail
	 */
	@Override
	public boolean sendForgotPasswordLinkMail(Employee employeeModel)throws Exception{
		boolean mailSendStatus = false;
		Properties environment = commonUtil.loadPropertyFile("mail.properties");
		Session sessionMail = mailUtil.getMailSession();
		String subject = (String) employeeService.getMessage("employee.label.passwordreset");
		String body = mailUtil.getEmailTemplate();
		getApplicationUrl();
		
		String enCryptUserId = CommonUtil.encrypt(employeeModel.getEmp_id());
		String enUserName = CommonUtil.encrypt(employeeModel.getEmp_name());
		String enEmail = CommonUtil.encrypt(employeeModel.getOffice_email());
		
		body = body.replace("$userName$", employeeModel.getEmp_name() != null ?  employeeModel.getEmp_name() : "")
				.replace("$url$", "http://" + host + ":" + port + path + "/userPasswordChange")
				.replace("$resetPasswordLink$", "http://" + host + ":" + port + path + 
						"/userPasswordChange?" + "ayMuiDcT=" + enCryptUserId + "&byUnetE="
						+ enUserName + "&jubEMikid=" + enEmail);
		
		if (StringUtils.isNotBlank(body)) {
			mailSendStatus = mailUtil.sendEmail(sessionMail, employeeModel.getOffice_email(), subject, body,
					environment.getProperty("in.scii.mail.noreply"));
			if(mailSendStatus) {
				employeeModel.setPw_reset_flg("1");
				employeeModel.setUpdated_by(employeeModel.getEmp_id());
				employeeMapper.updateMailSentTimeAndFlag(employeeModel);
			}
			//return mailSendStatus;
		}
		return mailSendStatus;
	}
	
	/**
	 * method to set system Address(host & port) 
	 * @throws MalformedObjectNameException
	 * @throws IOException 
	 */
	public void getApplicationUrl() throws MalformedObjectNameException, IOException {
		MBeanServer beanServer = ManagementFactory.getPlatformMBeanServer();
		
		Properties properties = commonUtil.loadPropertyFile("application.properties");
		
		Set<ObjectName> objectNames = null;
			objectNames = beanServer.queryNames(new ObjectName("*:type=Connector,*"),
							Query.match(Query.attr("protocol"), Query.value("HTTP/1.1")));
			host = InetAddress.getLocalHost().getHostAddress();
		for(ObjectName objectName: objectNames) {
			port = objectName.getKeyProperty("port");
			if(!port.equals("8443")) {
				break;
			}
		}
		path = properties.getProperty("server.servlet.context-path");
		//port = properties.getProperty("server.port");
	}

}