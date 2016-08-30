package com.rj.schedulesys.view.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.rj.schedulesys.service.TestService;
import com.rj.schedulesys.service.TestSubCategoryService;
import com.rj.schedulesys.view.model.TestSubCategoryViewModel;
import com.rj.schedulesys.view.model.TestViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/tests")
public class TestController {
	
	@Autowired
	private TestService testService;
	@Autowired
	private TestSubCategoryService testSubCategoryService;
	
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findOne(@PathVariable Long id){
		log.info("Fetching test with id {}", id);
		TestViewModel viewModel = testService.findOne(id);
		if(viewModel == null){
			log.info("No test found with id : {}", id);
			return new ResponseEntity<String>("No test found with id : " + id, HttpStatus.NOT_FOUND);
		}
		log.info("Test found : {}", viewModel);
		return new ResponseEntity<TestViewModel>(viewModel, HttpStatus.OK);
	}
	
	/**
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findAll(){
		log.info("Fetching all tests");
		List<TestViewModel> viewModels = testService.findAll();
		if(viewModels.isEmpty()){
			log.info("No test found");
			return new ResponseEntity<String>("No test found", HttpStatus.NOT_FOUND);
		}
		log.info("Tests found : {}", viewModels);
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> create(@RequestBody TestViewModel viewModel){
		log.info("Creating test : {}", viewModel);
		viewModel.setId(null);//Overrides id provided in view model
		try{
			viewModel = testService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Test created : {}", viewModel);
		return new ResponseEntity<String>("Test created successfully", HttpStatus.CREATED);
	}
	
	/**
	 * @param id
	 * @param viewModel
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<String> update(@PathVariable Long id, @RequestBody TestViewModel viewModel){
		log.info("Updating test with id : {}", id);
		if(testService.findOne(id) == null){
			log.info("No test found with id : {}", id);
			return new ResponseEntity<String>("No test found with id : " + id, HttpStatus.NOT_FOUND);
		}
		viewModel.setId(id);//Overriding the id received in the view model
		try{
		viewModel = testService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Test updated : {}", viewModel);
		return new ResponseEntity<String>("Test updated successfully", HttpStatus.OK);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id){
		log.info("Deleting test with id : {}", id);
		if(testService.findOne(id) == null){
			log.info("No test found with id : {}", id);
			return new ResponseEntity<String>("No test found with id : " + id, HttpStatus.NOT_FOUND);
		}
		try{
			testService.delete(id);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Test successfully deleted");
		return new ResponseEntity<String>("Test successfully deleted", HttpStatus.OK);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}/sub-categories", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findSubCategories(@PathVariable Long id){
		log.info("Fetching all sub categories of test with id : {}", id);
		if(testService.findOne(id) == null){
			log.warn("No test found with id : {}", id);
			return new ResponseEntity<>("No test found with id : " + id, HttpStatus.NOT_FOUND);
		}
		List<TestSubCategoryViewModel> viewModels = testSubCategoryService.findAllByTest(id);
		if(viewModels.isEmpty()){
			log.warn("No test sub catrgories found for test with id : {}", id);
			return new ResponseEntity<>("No test sub category found for test with id : " + id, HttpStatus.NOT_FOUND);
		}
		log.info("Subcategories found : {}", viewModels);
		return new ResponseEntity<List<TestSubCategoryViewModel>>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/sub-categories", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<String> addSubCategory(@PathVariable Long id, @RequestBody TestSubCategoryViewModel viewModel){
		log.info("Creating new test sub category: {}", viewModel);
		if(testService.findOne(id) == null){
			log.warn("No test found with id : {}", id);
			return new ResponseEntity<>("No test found with id : " + id, HttpStatus.NOT_FOUND);
		}
		viewModel.setTestId(id);
		try{
			testSubCategoryService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Test sub category successfully created");
		return new ResponseEntity<String>("Test sub category successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/sub-categories/{subCategoryId}", method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<String> updateSubCategory(@PathVariable Long id, @PathVariable Long subCategoryId
			, @RequestBody TestSubCategoryViewModel viewModel){
		log.info("Updating test sub category: {}", viewModel);
		TestViewModel test = testService.findOne(id);
		if(test == null){
			log.warn("No test found with id : {}", id);
			return new ResponseEntity<>("No test found with id : " + id, HttpStatus.NOT_FOUND);
		}
		TestSubCategoryViewModel testSubCategory = testSubCategoryService.findOne(subCategoryId);
		if(testSubCategory == null){
			log.warn("No test sub category found with id : {}", subCategoryId);
			return new ResponseEntity<>("No test sub category found with id : " + subCategoryId, HttpStatus.NOT_FOUND);
		}
		if(testSubCategory.getTestId() != test.getId()){
			log.error("No test sub category with id : {} found for test with id : {}", subCategoryId, id);
			return new ResponseEntity<>("No test sub category with id : " 
					+ subCategoryId + " found for test with id : {}" + id, HttpStatus.NOT_FOUND);
		}
		viewModel.setTestId(id);
		viewModel.setId(subCategoryId);
		try{
			testSubCategoryService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Test sub category successfully updated");
		return new ResponseEntity<String>("Test sub category successfully created", HttpStatus.OK);
	}
}