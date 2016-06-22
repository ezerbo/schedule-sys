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

import com.rj.schedulesys.service.PhoneNumberLabelService;
import com.rj.schedulesys.view.model.PhoneNumberLabelViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/phone-number-labels")
public class PhoneNumberLabelController {

	private @Autowired PhoneNumberLabelService phoneNumberLabelService;
	
	/**
	 * @param idOrName
	 * @return
	 */
	@RequestMapping(value = "/{idOrName}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable String idOrName){
		
		PhoneNumberLabelViewModel viewModel = null;
		
		if(StringUtils.isNumeric(idOrName)){
			log.info("Fetching phone number label with id : {}", idOrName);
			viewModel = phoneNumberLabelService.findOne(Long.valueOf(idOrName));
		}else{
			log.info("Finding phone number label with name : {}", idOrName);
			viewModel = phoneNumberLabelService.findByName(idOrName);
		}
		
		if(viewModel == null){
			log.error("No phone number label found with name or id : {}", idOrName);
			return new ResponseEntity<String>(
					"No phone number label found with either id or name : " + idOrName, HttpStatus.NOT_FOUND
					);
		}
		
		log.info("Phone number label found : {}", viewModel);
		
		return new ResponseEntity<PhoneNumberLabelViewModel>(viewModel, HttpStatus.OK);
	}
	
	/**
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAll(){
		
		log.debug("Fetching all phone number labels");
		
		List<PhoneNumberLabelViewModel> viewModels = phoneNumberLabelService.findAll();
		
		if(viewModels.isEmpty()){
			log.warn("No phone number label found");
			return new ResponseEntity<String>("No phone number label found : {}", HttpStatus.NOT_FOUND);
		}
		
		log.info("Phone number labels found : {}", viewModels);
		
		return new ResponseEntity<List<PhoneNumberLabelViewModel>>(viewModels, HttpStatus.OK);
	}
}
