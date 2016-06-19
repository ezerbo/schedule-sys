package com.rj.schedulesys.view.controller;
//package com.rj.sys.view.controller;
//
//import java.util.List;
//
//import lombok.extern.slf4j.Slf4j;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import com.rj.sys.service.TestService;
//import com.rj.sys.service.TestTypeService;
//import com.rj.sys.view.model.TestTypeViewModel;
//import com.rj.sys.view.model.TestViewModel;
//
//@Slf4j
//@Controller
//@RequestMapping("/tests")
//public class TestController {
//	
//	private @Autowired TestService testService;
//	private @Autowired TestTypeService testTypeService;
//	
//	@RequestMapping(value = "/{id}/test-types", method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity<?> findTypes(@PathVariable Long id){
//		log.info("finding all types for test with id : {}", id);
//		
//		if(testService.findById(id) == null){
//			log.info("No test found with id : {}", id);
//			return new ResponseEntity<>("No test found id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		List<TestTypeViewModel> viewModels = testTypeService.findAllByTestId(id);
//		if(viewModels.isEmpty()){
//			log.info("No test types found for test with id : {}", id);
//			return new ResponseEntity<>("No type found for test with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		log.info("Types found");
//		return new ResponseEntity<>(viewModels, HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "/{id}/test-types", method = RequestMethod.POST, consumes = "application/json")
//	public ResponseEntity<String> createType(@PathVariable Long id, @RequestBody TestTypeViewModel viewModel){
//		log.info("Adding test type to test with id : {}", id);
//		
//		if(testService.findById(id) == null){
//			log.info("No test found with id : {}", id);
//			return new ResponseEntity<String>("No test found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		if(testTypeService.findByName(viewModel.getTypeName()) != null){
//			log.info("A type with name : {} already exist for test with id : {}", viewModel.getTypeName(), id);
//			return new ResponseEntity<String>(
//					"A type with name : " + viewModel.getTypeName() + " already exists for test with id : " + id, HttpStatus.INTERNAL_SERVER_ERROR
//					);
//		}
//		viewModel.setId(null);
//		viewModel = testTypeService.createOrUpdate(viewModel, id);
//		log.info("Type : {} added succesfully");
//		return new ResponseEntity<String>("Type added successfully", HttpStatus.CREATED);
//	}
//	
//	@RequestMapping(value = "/{id}/test-types/{typeId}", method = RequestMethod.DELETE)
//	public ResponseEntity<String> deleteType(@PathVariable Long id, @PathVariable Long typeId){
//		log.info("Deleting type with id : {}", typeId);
//		
//		if(testService.findById(id) == null){
//			log.info("No test found with id : {}", id);
//			return new ResponseEntity<String>("No test found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		if(testTypeService.findByIdAndTestId(typeId, id) == null){
//			log.info("No test type with id : {} found for test with id : {}", typeId, id);
//			return new ResponseEntity<String>(
//					"No test type with id : " + typeId + " found for test with id : " + id, HttpStatus.NOT_FOUND
//					);
//		}
//		
//		testTypeService.delete(typeId);
//		log.info("Test type successfully deleted");
//		return new ResponseEntity<String>("Test type sucessfully deleted", HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "/{id}/test-types/{typeId}", method = RequestMethod.PUT, consumes = "application/json")
//	public ResponseEntity<String> updateType(@PathVariable Long id, @PathVariable Long typeId, @RequestBody TestTypeViewModel viewModel){
//		log.info("updating test type : {}", viewModel);
//		
//		if(testService.findById(id) == null){
//			log.info("No test found with id : {}", id);
//			return new ResponseEntity<String>("No test found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		TestTypeViewModel testType = testTypeService.findById(typeId);
//		if(!StringUtils.equals(testType.getTypeName(), viewModel.getTypeName())){
//			
//			if(testTypeService.findByName(viewModel.getTypeName()) != null){
//				log.info("A type with name : {} already exists", viewModel.getTypeName(), id);
//				return new ResponseEntity<String>(
//						"A type with name : " + viewModel.getTypeName() + " already exists : " + id, HttpStatus.INTERNAL_SERVER_ERROR
//						);
//			}
//			
//		}
//		
//		viewModel.setId(typeId);//Overriding the one sent in the DTO 
//		
//		viewModel = testTypeService.createOrUpdate(viewModel, id);
//		log.info("Type : {} added succesfully");
//		return new ResponseEntity<String>("Type added successfully", HttpStatus.CREATED);
//	}
//	
//	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity<?> findById(@PathVariable Long id){
//		log.info("Finding test by id {}", id);
//		
//		TestViewModel viewModel = testService.findById(id);
//		if(viewModel == null){
//			log.info("No test found with id : {}", id);
//			return new ResponseEntity<String>("No test found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		log.info("Test found : {}", viewModel);
//		
//		return new ResponseEntity<TestViewModel>(viewModel, HttpStatus.OK);
//	}
//	
//	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity<?> findAll(){
//		log.info("Finding all tests");
//		List<TestViewModel> viewModels = testService.findAll();
//		
//		if(viewModels.isEmpty()){
//			log.info("No test found");
//			return new ResponseEntity<String>("No test found", HttpStatus.NOT_FOUND);
//		}
//		
//		log.info("Tests found : {}", viewModels);
//		return new ResponseEntity<List<TestViewModel>>(viewModels, HttpStatus.OK);
//	}
//	
//	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
//	public ResponseEntity<String> createTest(@RequestBody TestViewModel viewModel){
//		
//		log.info("Creating test : {}", viewModel);
//		
//		if(testService.findByName(viewModel.getTestName()) != null){
//			log.info("A test with name : {} already exists", viewModel.getTestName());
//			return new ResponseEntity<String>(
//					"A test with name : " + viewModel.getTestName() + " already exists", HttpStatus.INTERNAL_SERVER_ERROR
//					);
//		}
//		viewModel.setId(null);
//		viewModel = testService.createOrUpdateTest(viewModel);
//		log.info("Test created : {}", viewModel);
//		return new ResponseEntity<String>("Test created successfully", HttpStatus.CREATED);
//	}
//	
//	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
//	public ResponseEntity<String> updateTest(@PathVariable Long id, @RequestBody TestViewModel viewModel){
//		log.info("Updating test with id : {}", id);
//		
//		if(testService.findById(id) == null){
//			log.info("No test found by id : {}", id);
//			return new ResponseEntity<String>("No test found by id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		viewModel.setId(id);//Overriding the id received in the DTO
//		viewModel = testService.createOrUpdateTest(viewModel);
//		log.info("Test updated : {}", viewModel);
//		return new ResponseEntity<String>("Test updated successfully", HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//	public ResponseEntity<String> deleteTest(@PathVariable Long id){
//		log.info("Deleting test with id : {}", id);
//		
//		if(testService.findById(id) == null){
//			log.info("No test found with id : {}", id);
//			return new ResponseEntity<String>("No test found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		if(testService.hasTypes(id)){
//			log.info("Test with id : {} cannot be deleted because it still has some types");
//			return new ResponseEntity<String>(
//					"Test with id : " + id + " cannot be deleted because it still has some types", HttpStatus.INTERNAL_SERVER_ERROR
//					);
//		}
//		
//		testService.deleteTest(id);
//		log.info("Test successfully deleted");
//		
//		return new ResponseEntity<String>("Test successfully deleted", HttpStatus.OK);
//	}
//	
//}