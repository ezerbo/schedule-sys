package com.rj.schedulesys.view.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.schedulesys.service.UserRoleService;
import com.rj.schedulesys.view.model.UserRoleViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/user-roles")
public class UserRoleController {
	
	private @Autowired UserRoleService userRoleService;
	
	@RequestMapping(value = "/{idOrRole}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findByIdOrRoleName(@PathVariable String idOrRole){
		
		UserRoleViewModel viewModel = null;
		
		if(StringUtils.isNumeric(idOrRole)){
			viewModel = userRoleService.findById(Long.valueOf(idOrRole));
		}else{
			viewModel = userRoleService.findByRole(idOrRole);
		}
		
		if(viewModel == null){
			return new ResponseEntity<String>(
					"No user role found with id or role : " + idOrRole, HttpStatus.NOT_FOUND
					);
		}
		
		log.info("User role found : {}", viewModel);
		
		return new ResponseEntity<UserRoleViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAllRoles(){
		
		List<UserRoleViewModel> viewModels = userRoleService.findAll();
		
		if(viewModels.isEmpty()){
			log.info("No user role found");
			return new ResponseEntity<String>(
					"No user role found", HttpStatus.NOT_FOUND
					);
		}
		
		log.info("User roles found : {}", viewModels);
		
		return new ResponseEntity<List<UserRoleViewModel>>(viewModels, HttpStatus.OK);
	}
}