package com.rj.schedulesys.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import com.rj.schedulesys.dao.ScheduleSysUserDao;
import com.rj.schedulesys.domain.ScheduleSysUser;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthenticationService {
	
	private @Autowired ScheduleSysUserDao userDao;
	
	@Transactional
	public ScheduleSysUser getAuthenticatedUser(){
		log.info("Getting authenticated user");
		
		OAuth2Authentication user = 
				(OAuth2Authentication) SecurityContextHolder.getContext()
									.getAuthentication();
		
		ScheduleSysUser authenticatedUser = userDao.findByUsername(user.getName());
		log.info("Authenticated user : {} with authorities : {}", authenticatedUser, user.getAuthorities());
		return authenticatedUser;
	}
}
