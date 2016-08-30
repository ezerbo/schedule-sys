package com.rj.schedulesys.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rj.schedulesys.dao.ScheduleSysUserDao;
import com.rj.schedulesys.domain.ScheduleSysUser;

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
				.append("/#/home/activate?key=")
				.append(createdUser.getActivationToken())
				.toString();
		Map<String, Object> headers = buildHeadersMap(recipient, createdUser.getUsername(), activationUrl);
		template.sendBodyAndHeaders(null, headers);
	}
	
	private Map<String, Object> buildHeadersMap(String recipient, String username, String activationUrl){
		Map<String, Object> headers = new HashMap<>();
		headers.put("emailRecipient", recipient);
		headers.put("activationUrl", activationUrl);
		headers.put("username", username);
		return headers;
	}
	
}