package com.rj.sys.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import com.rj.sys.dao.UserDao;
import com.rj.sys.domain.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthenticationService {
	
	private @Autowired UserDao userDao;
	
	@Transactional
	public User getAuthenticatedUser(){
		log.info("Getting authenticated user");
		
		OAuth2Authentication user = 
				(OAuth2Authentication) SecurityContextHolder.getContext()
									.getAuthentication();
		User authenticatedUser = userDao.findByUsername(user.getName());
		log.info("Authenticated user : {}", authenticatedUser);
		return authenticatedUser;
	}
}
