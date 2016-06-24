package com.rj.schedulesys.view.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.schedulesys.service.TestSubCategoryService;
import com.rj.schedulesys.view.model.TestSubCategoryViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/test-sub-categories")
public class TestSubCategoryController {

	private @Autowired TestSubCategoryService testSubCategoryService;
	
	@RequestMapping(value = "/{idOrName}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable  String idOrName){
		
		TestSubCategoryViewModel viewModel = null;
		
		if(StringUtils.isNumeric(idOrName)){
			log.info("Fetching test sub category with id : {}", idOrName);
			viewModel = testSubCategoryService.findOne(Long.valueOf(idOrName));
		}else{
			log.info("Fetching test sub category with name : {}", idOrName);
			viewModel = testSubCategoryService.findByName(idOrName);
		}
		
		if(viewModel == null){
			log.warn("No test sub category found with id or name : {}", idOrName);
			return new ResponseEntity<String>("No test sub category found with id or name : " + idOrName, HttpStatus.NOT_FOUND);
		}
		
		log.info("Test sub category found : {}", viewModel);
		
		return new ResponseEntity<>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> create(@RequestBody TestSubCategoryViewModel viewModel){
		
		log.info("Creating new test sub category: {}", viewModel);
		
		viewModel.setId(null);
		
		try{
			testSubCategoryService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Test sub category successfully created");
		
		return new ResponseEntity<String>("Test sub category successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<String> update(@PathVariable Long id, @RequestBody TestSubCategoryViewModel viewModel){
		
		log.info("updating test sub category : {}", viewModel);
		
		if(testSubCategoryService.findOne(id) == null){
			log.info("No test sub category found with id : {}", id);
			return new ResponseEntity<String>("No test sub category found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		viewModel.setId(id);//Overriding the one sent in the DTO 
		
		try{
			testSubCategoryService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Test sub category succesfully successfully updated");
		
		return new ResponseEntity<String>("Test sub category successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id){
		
		log.info("Deleting test sub category with id : {}", id);
		
		if(testSubCategoryService.findOne(id) == null){
			log.info("No test sub category  found with id : {}", id);
			return new ResponseEntity<String>("No test sub category found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		try{
			testSubCategoryService.delete(id);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Test sub category successfully deleted");
		
		return new ResponseEntity<String>("Test sub category sucessfully deleted", HttpStatus.OK);
	}

}
