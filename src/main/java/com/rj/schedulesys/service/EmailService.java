package com.rj.schedulesys.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rj.schedulesys.dao.ScheduleSysUserDao;
import com.rj.schedulesys.data.EmailTypeConstants;
import com.rj.schedulesys.domain.ScheduleSysUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {
	
	@Produce(uri = "direct:account-email")
	private ProducerTemplate template;
	
	@Autowired
	private ScheduleSysUserDao userDao;
	
	public void sendAccountActivationEmail(String recipient, String hostServerName, int port){
		ScheduleSysUser createdUser = userDao.findByEmailAddress(recipient);
		String activationUrl = new StringBuilder().append("http://")
				.append(hostServerName)
				.append(":").append(port)
				.append("/#/activate?key=")
				.append(createdUser.getToken())
				.toString();
		Map<String, Object> headers = buildHeadersMap(recipient, createdUser.getUsername(),
				activationUrl, EmailTypeConstants.ACCOUNT_ACTIVATION);
		template.sendBodyAndHeaders(null, headers);
	}
	
	public void sendPasswordResetmail(String recipient, String hostServerName, int port){
		ScheduleSysUser scheduleSysUser = userDao.findByEmailAddress(recipient);
		if(scheduleSysUser == null){
			scheduleSysUser = userDao.findByUsername(recipient);
			if(scheduleSysUser == null){
				log.error("Attempted to reset password for invalid username or email address : {}", recipient);
				throw new RuntimeException("No user found with username or email address : {}" + recipient);
			}
		}
		String passwordResetUrl = new StringBuilder().append("http://")
				.append(hostServerName)
				.append(":").append(port)
				.append("/#/activate?token=")
				.append(scheduleSysUser.getToken())
				.toString();
		Map<String, Object> headers = buildHeadersMap(scheduleSysUser.getEmailAddress(), scheduleSysUser.getUsername(),
				passwordResetUrl, EmailTypeConstants.PASSWORD_RESET);
		template.sendBodyAndHeaders(null, headers);
	}
	
	private Map<String, Object> buildHeadersMap(String recipient, String username, String url, String emailType){
		Map<String, Object> headers = new HashMap<>();
		headers.put("emailRecipient", recipient);
		headers.put("url", url);
		headers.put("username", username);
		headers.put("emailType", emailType);
		return headers;
	}
	
}