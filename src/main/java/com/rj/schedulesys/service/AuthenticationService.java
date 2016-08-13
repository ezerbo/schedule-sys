package com.rj.schedulesys.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        
        String userName = null;
        
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }
        
		ScheduleSysUser authenticatedUser = userDao.findByUsername("ezerbo");//TODO pass the authenticated user 
		
		if(authenticatedUser == null){
			log.error("No user found");
			throw new RuntimeException("Something unexpected happened");
		}
		
		log.info("User found : {}, roles : {}", authenticatedUser.getUsername(), authenticatedUser.getUserRole().getUserRole());
		
		return authenticatedUser;
	}
}
