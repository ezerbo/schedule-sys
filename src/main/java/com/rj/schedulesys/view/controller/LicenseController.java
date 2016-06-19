package com.rj.schedulesys.view.controller;
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
//import com.rj.sys.service.LicenseService;
//import com.rj.sys.service.UserService;
//import com.rj.sys.view.model.LicenseViewModel;
//
//@Slf4j
//@Controller
//@RequestMapping("/employees")
//public class LicenseController {
//	
//	private @Autowired UserService userService;
//	private @Autowired LicenseService licenseService;
//	
//	
//	@RequestMapping(value = "/{id}/licenses", method = RequestMethod.POST, consumes = "application/json")
//	public ResponseEntity<?> addLicense(@PathVariable Long id, @RequestBody LicenseViewModel viewModel){
//		log.info("Adding license : {} for user with id : {}", viewModel, id);
//		
//		if(userService.findEmployeeById(id) == null){
//			log.info("No employee found with id : {}", id);
//			return new ResponseEntity<>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		if(licenseService.findByLicenseNumber(viewModel.getLicenseNumber()) != null){
//			log.info("A license with number : {} already exists", viewModel.getLicenseNumber());
//			return new ResponseEntity<>(
//					"A license with number : " + viewModel.getLicenseNumber() + " already exists", HttpStatus.INTERNAL_SERVER_ERROR
//					);
//		}
//		
//		viewModel.setUserId(id);//overriding value in the view model if any
//		viewModel.setId(null);
//		
//		 viewModel = licenseService.createOrUpdateLicense(viewModel);
//		 log.info("Added license : {} for user with id : {}", viewModel, id);
//		 return new ResponseEntity<>("License added successfully for user with id : " + id, HttpStatus.CREATED);
//	}
//	
//	@RequestMapping(value = "/{id}/licenses/{licenseId}", method = RequestMethod.PUT, consumes = "application/json")
//	public ResponseEntity<?> updateLicense(@PathVariable Long id, @PathVariable Long licenseId, @RequestBody LicenseViewModel viewModel){
//		log.info("Updating license : {} for user with id : {}", viewModel, id);
//		
//		if(userService.findEmployeeById(id) == null){
//			log.info("No employee found with id : {}", id);
//			return new ResponseEntity<>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		LicenseViewModel licenseViewModel = licenseService.findBydIdAndUserId(licenseId, id);
//		if(licenseViewModel == null){
//			log.info("No license with id : {} found for user with id : {}", licenseId, id);
//			return new ResponseEntity<>(
//					"No license with id : " + viewModel.getId() + " found for user with id : " + id, HttpStatus.NOT_FOUND
//					);
//		}
//		
//		if(licenseService.findByLicenseNumber(viewModel.getLicenseNumber()) != null){
//			log.info("A license with number : {} already exists", viewModel.getLicenseNumber());
//			return new ResponseEntity<>(
//					"A license with number : " + viewModel.getLicenseNumber() + " already exists", HttpStatus.INTERNAL_SERVER_ERROR
//					);
//		}
//		
//		viewModel.setId(licenseId);
//		viewModel.setUserId(id);
//		
//		viewModel = licenseService.createOrUpdateLicense(viewModel);
//		log.info("Updated license : {} for user with id : {}", viewModel, id);
//		return new ResponseEntity<>("License successfully updated for user with id : " + id, HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "/{id}/licenses/{licenseId}", method = RequestMethod.DELETE)
//	public ResponseEntity<?> removeLicense(@PathVariable Long id, @PathVariable Long licenseId){
//		
//		log.info("Removing license with id : {} for user with id : {}", id, licenseId);
//		if(userService.findEmployeeById(id) == null){
//			log.info("No employee found with id : {}", id);
//			return new ResponseEntity<>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		LicenseViewModel licenseViewModel = licenseService.findBydIdAndUserId(licenseId, id);
//		if(licenseViewModel == null){
//			log.info("No license with id : {} found for user with id : {}", licenseId, id);
//			return new ResponseEntity<>(
//					"No license with id : " + licenseId + " found for user with id : " + id, HttpStatus.NOT_FOUND
//					);
//		}
//		
//		licenseService.remove(licenseId);
//		
//		log.info("License successfully deleted", HttpStatus.OK);
//		return new ResponseEntity<>("License successfully deleted", HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "/{id}/licenses", method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity<?> findAllByUserId(@PathVariable Long id){
//		log.info("Finding all licenses for user with id : {}", id);
//		
//		if(userService.findEmployeeById(id) == null){
//			log.info("No employee found with id : {}", id);
//			return new ResponseEntity<>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		List<LicenseViewModel> viewModels = licenseService.findAllByUserId(id);
//		
//		if(viewModels.isEmpty()){
//			log.info("No licenses found for user with id : ", id);
//			return new ResponseEntity<>("No licenses found for employee with id : " + id, HttpStatus.NOT_FOUND);
//		}
//		
//		log.info("Licenses : {} found for user with id : {}", viewModels, id);
//		
//		return new ResponseEntity<>(viewModels, HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "/{id}/licenses/{licenseId}", method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity<?> findOneByUserId(@PathVariable Long id, @PathVariable Long licenseId){
//		
//		log.info("Finding license with id : {} for user with id : {}", licenseId, id);
//		
//		if(userService.findEmployeeById(id) == null){
//			log.info("No employee found with id : {}", id);
//			return new ResponseEntity<>("No employee found with id : " + id, HttpStatus.NOT_FOUND);
//		} 
//		
//		LicenseViewModel viewModel = licenseService.findBydIdAndUserId(licenseId, id);
//		if(viewModel == null){
//			log.info("No license with id : {} found for user with id : {}", licenseId, id);
//			return new ResponseEntity<>(
//					"No license with id : " + licenseId + " found for user with id : " + id, HttpStatus.NOT_FOUND
//					);
//		}
//		
//		log.info("License found : {}", viewModel);
//		return new ResponseEntity<>(viewModel, HttpStatus.OK);
//	}
//}