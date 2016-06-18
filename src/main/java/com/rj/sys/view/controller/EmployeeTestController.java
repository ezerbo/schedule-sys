//package com.rj.sys.view.controller;
//
//import java.util.List;
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import com.rj.sys.service.TestTypeService;
//import com.rj.sys.service.UserService;
//import com.rj.sys.service.UserTestService;
//import com.rj.sys.view.model.EmployeeViewModel;
//import com.rj.sys.view.model.TestTypeViewModel;
//import com.rj.sys.view.model.UserTestUpdateViewModel;
//import com.rj.sys.view.model.UserTestViewModel;
//
//@Slf4j
//@Controller
//@RequestMapping(value = "/users/employees")
//public class EmployeeTestController {
//
//	private @Autowired UserService userService;
//	private @Autowired UserTestService userTestService;
//	private @Autowired TestTypeService testTypeService;
//	
//	@RequestMapping(value = "/{id}/tests", method = RequestMethod.POST, consumes = "application/json")
//	public ResponseEntity<?> addTest(@PathVariable Long id, @RequestBody UserTestViewModel viewModel){
//		log.info("Adding new test for employee with id {}", id);
//		
//		EmployeeViewModel employeeViewModel = userService.findEmployeeById(id);
//		
//		if(employeeViewModel == null){
//			log.info("No employee found with id : {}", id);
//			return new ResponseEntity<String>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		TestTypeViewModel testTypeViewModel = testTypeService.findByName(viewModel.getTestTypeName());
//		
//		if(testTypeViewModel == null){
//			log.info("No test type fond with name : {}", viewModel.getTestTypeName());
//			return new ResponseEntity<String>(
//					"No test type found with name : " + viewModel.getTestTypeName(), HttpStatus.NOT_FOUND
//					);
//		}
//		
//		if(userTestService.findById(employeeViewModel.getId(), testTypeViewModel.getId()) != null){
//			log.info("Test type with id : {} found for user with id : {}"
//					, testTypeViewModel.getId(), employeeViewModel.getId()
//					);
//			
//			return new ResponseEntity<>(
//					"Test type with id : " + testTypeViewModel.getId() 
//					+ " found for user with id : " + employeeViewModel.getId(), HttpStatus.INTERNAL_SERVER_ERROR
//					);
//		}
//		viewModel.setUserId(id);
//		viewModel = userTestService.addOrUpdateTestForUser(viewModel);
//		
//		return new ResponseEntity<String>("Test type added successfully", HttpStatus.CREATED);
//		
//	}
//	
//	@RequestMapping(value = "/{id}/tests", method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity<?> findAll(@PathVariable Long id){
//		log.info("Finding all test for user with id : {}", id);
//		
//		if(userService.findEmployeeById(id) == null){
//			log.info("No employee found with id : {}", id);
//			return new ResponseEntity<>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		List<UserTestViewModel> viewModels = userTestService.findAllByUserId(id);
//		
//		if(viewModels.isEmpty()){
//			log.info("No tests found for user with id : {}", id);
//			return new ResponseEntity<>("No tests found for employee with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		log.info("Tests found : {}", viewModels);
//		
//		return new ResponseEntity<>(viewModels, HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "/{id}/tests/{testTypeName}", method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity<?> findBidUserIdAndTestTypeName(@PathVariable Long id, @PathVariable String testTypeName){
//		log.info("Finding test with name : {} for user with id : {}", testTypeName, id);
//		
//		EmployeeViewModel employeeViewModel = userService.findEmployeeById(id);
//		if(employeeViewModel == null){
//			log.info("No employee found with id : {}", id);
//			return new ResponseEntity<>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		TestTypeViewModel testTypeViewModel = testTypeService.findByName(testTypeName);
//		if(testTypeViewModel == null){
//			log.info("No such test type : {}", testTypeName);
//			return new ResponseEntity<>("No such test type : " + testTypeName, HttpStatus.NOT_FOUND);
//		}
//		
//		UserTestViewModel viewModel = userTestService.findById(
//				employeeViewModel.getId(), testTypeViewModel.getId()
//				);
//		if(viewModel == null){
//			log.info("No test type with name : {} found for user with id : {}", testTypeName, id);
//			return new ResponseEntity<>(
//					"No test type with name : " + testTypeName + " found for user with id : " + id, HttpStatus.NOT_FOUND
//					);
//		}
//		log.info("User test found : {}", viewModel);
//		return new ResponseEntity<>(viewModel, HttpStatus.OK); 
//		
//	}
//	
//	@RequestMapping(value = "/{id}/tests/{testTypeName}", method = RequestMethod.PUT, produces = "application/json")
//	public ResponseEntity<?> updateTest(@PathVariable Long id
//			, @PathVariable String testTypeName
//			, @RequestBody UserTestUpdateViewModel viewModel){
//		log.info("Updating test for user with id : {}", id);
//		
//		TestTypeViewModel testTypeViewModel = testTypeService.findByName(testTypeName);
//		
//		if(userTestService.findById(id, testTypeViewModel.getId()) == null){
//			log.info("No test type with name : {} found for user with id : {}", testTypeName, id);
//			return new ResponseEntity<>(
//					"No test type with name : " + testTypeName + " found for user with id : " + id, HttpStatus.NOT_FOUND
//					);
//		}
//		
//		UserTestViewModel userTestViewModel = UserTestViewModel.builder()
//				.completedDate(viewModel.getCompletedDate())
//				.expirationDate(viewModel.getExpirationDate())
//				.testTypeName(testTypeName)
//				.userId(id)
//				.build();
//		
//		userTestViewModel = userTestService.addOrUpdateTestForUser(userTestViewModel);
//		
//		log.info("Test updated successfully");
//		
//		return new ResponseEntity<>("Test updated successfully", HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "/{id}/tests/{testTypeName}", method = RequestMethod.DELETE)
//	public ResponseEntity<?> removeTest(@PathVariable Long id, @PathVariable String testTypeName){
//		log.info("Removing test with name : {} for user with id : {}", testTypeName, id);
//		
//		TestTypeViewModel testTypeViewModel = testTypeService.findByName(testTypeName);
//		
//		if(userTestService.findById(id, testTypeViewModel.getId()) == null){
//			log.info("No test type with name : {} found for user with id : {}", testTypeName, id);
//			return new ResponseEntity<>(
//					"No test type with name : " + testTypeName + " found for user with id : " + id, HttpStatus.NOT_FOUND
//					);
//		}
//		
//		userTestService.removeTest(id, testTypeViewModel.getId());
//		log.info("Test with name : {} successfull deleted for user with id : {}", testTypeName, id);
//		
//		return new ResponseEntity<>("Test successfully deleted", HttpStatus.OK);
//	}
//	
//}