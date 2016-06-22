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

import com.rj.schedulesys.service.PhoneNumberTypeService;
import com.rj.schedulesys.view.model.PhoneNumberTypeViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/phone-number-types")
public class PhoneNumberTypeController {

	private @Autowired PhoneNumberTypeService phoneNumberTypeService;
	
	/**
	 * @param idOrName
	 * @return
	 */
	@RequestMapping(value = "/{idOrName}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable String idOrName){
		
		PhoneNumberTypeViewModel viewModel = null;
		
		if(StringUtils.isNumeric(idOrName)){
			log.info("Fetching phone number type with id : {}", idOrName);
			viewModel = phoneNumberTypeService.findOne(Long.valueOf(idOrName));
		}else{
			log.info("Finding phone number type with name : {}", idOrName);
			viewModel = phoneNumberTypeService.findByName(idOrName);
		}
		
		if(viewModel == null){
			log.error("No phone number type found with name or id : {}", idOrName);
			return new ResponseEntity<String>(
					"No phone number type found with either id or name : " + idOrName, HttpStatus.NOT_FOUND
					);
		}
		
		log.info("Phone number type found : {}", viewModel);
		
		return new ResponseEntity<PhoneNumberTypeViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAll(){
		
		log.info("Fetching all the phone number types");
		
		List<PhoneNumberTypeViewModel> viewModels = phoneNumberTypeService.findAll();
		
		if(viewModels.isEmpty()){
			log.warn("No phone number type found");
			return new ResponseEntity<String>("No phone number type found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Phone number types found : {}", viewModels);
		
		return new ResponseEntity<List<PhoneNumberTypeViewModel>>(viewModels, HttpStatus.OK);
	}
	
	
}
