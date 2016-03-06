package com.rj.sys.view.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.sys.service.UserTypeService;
import com.rj.sys.view.model.UserTypeViewModel;

@Slf4j
@Controller
@RequestMapping("/user-types")
public class UserTypeController {
	
	private @Autowired UserTypeService userTypeService;
	
	@RequestMapping(value = "/{idOrType}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findUserType(@PathVariable String idOrType){
		UserTypeViewModel viewModel = null;
		if(StringUtils.isNumeric(idOrType)){
			log.info("Finding userType with id : {}", idOrType);
			viewModel = userTypeService.findById(Long.valueOf(idOrType));
		}else{
			log.info("Finding userType : {}", idOrType);
			viewModel = userTypeService.findByType(idOrType);
		}
		
		if(viewModel == null){
			return new ResponseEntity<String>(
					"No user type found with id or type : " + idOrType, HttpStatus.NOT_FOUND
					);
		}
		return new ResponseEntity<UserTypeViewModel>(viewModel, HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAllTypes(){
		log.info("Finding all user types");
		List<UserTypeViewModel> viewModels = userTypeService.findAll();
		if(viewModels.isEmpty()){
			log.info("No user type found");
			return new ResponseEntity<String>(
					"No user type was found", HttpStatus.NOT_FOUND
					);
		}
		return new ResponseEntity<List<UserTypeViewModel>>(viewModels, HttpStatus.OK);
	}
}