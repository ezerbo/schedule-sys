package com.rj.sys.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rj.sys.config.TestConfiguration;
import com.rj.sys.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
@TestPropertySource("classpath:test-application.properties")
@WithMockUser(username = "username2", roles={"SUPERVISORS"})
public class AuthenticationServiceTest {
	
	private @Autowired AuthenticationService authenticationService;
	
	@Test
	public void testAuth(){
		User user = authenticationService.getAuthenticationService();
		assertEquals("test2@test.com", user.getEmailAddress());
	}
}
