package com.ss.schedulesys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.UserRole;
import com.ss.schedulesys.repository.UserRoleRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class UserRoleService {
	
	private UserRoleRepository userRoleRepository;
	
	@Autowired
	public UserRoleService(UserRoleRepository userRoleRepository) {
		this.userRoleRepository = userRoleRepository;
	}
	
	@Transactional(readOnly = true)
	public List<UserRole> findAll(){
		log.debug("Request to get all user roles");
		List<UserRole> userRoles = userRoleRepository.findAll();
		return userRoles;
	}
}
