package com.ss.schedulesys.web.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.schedulesys.domain.UserRole;
import com.ss.schedulesys.service.UserRoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserRoleResource {

	private UserRoleService userRoleService;
	
	public UserRoleResource(UserRoleService userRoleService) {
		this.userRoleService = userRoleService;
	}
	
	@GetMapping("user-roles")
	public ResponseEntity<List<UserRole>> getAllUserRoles(){
		log.debug("REST request to get all user roles");
		List<UserRole> userRoles = userRoleService.findAll();
		return (!userRoles.isEmpty())
				? new ResponseEntity<>(userRoles, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
