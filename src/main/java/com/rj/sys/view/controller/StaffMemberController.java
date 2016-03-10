package com.rj.sys.view.controller;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.sys.service.FacilityService;
import com.rj.sys.service.StaffMemberService;
import com.rj.sys.view.model.StaffMemberViewModel;

@Slf4j
@Controller
@RequestMapping("/staff-members")
public class StaffMemberController {
	
	private @Autowired StaffMemberService staffMemberService;
	private @Autowired FacilityService facilityService;
	
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAllStaffMembers(){
		log.info("Finding all staff members ");
		List<StaffMemberViewModel> viewModels = staffMemberService.findAll();
		
		if(viewModels.isEmpty()){
			log.info("No staff members found");
			return new ResponseEntity<String>("No staff member found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Staff members found : {}", viewModels);
		return new ResponseEntity<List<StaffMemberViewModel>>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findStaffMemberById(@PathVariable Long id){
		log.info("Finding staff member by id {}", id);
		StaffMemberViewModel viewModel = staffMemberService.findById(id);
		if(viewModel == null){
			log.info("No staff member found with id : {}", id);
			return new ResponseEntity<String>("No staff member found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		log.info("Staff member found : {}", viewModel);
		return new ResponseEntity<StaffMemberViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<String> createStaffMember(@RequestBody StaffMemberViewModel viewModel){
		log.info("Creating new staff member : {}", viewModel);
		if(facilityService.findByName(viewModel.getFacility()) == null){
			log.info("No facility found with name : {}", viewModel.getFacility());
			return new ResponseEntity<String>(
					"No facility found with name : " + viewModel.getFacility(), HttpStatus.NOT_FOUND
					);
		}
		
		if(staffMemberService.find(viewModel.getFirstName()
				, viewModel.getLastName(), viewModel.getTitle()) != null){
			log.info("A staff member with firstname : {}, lastname : {}, title : {} already exist"
					,viewModel.getFirstName(), viewModel.getLastName(), viewModel.getTitle() );
			
			return new ResponseEntity<String>("A staff member with firstname : " + viewModel.getFirstName()
					+ " lastname : " + viewModel.getLastName()
					+ " title : " + viewModel.getTitle()
					+ " already exist", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		viewModel = staffMemberService.createOrUpdateStaffMember(viewModel);
		
		log.info("Successfuly created : {}", viewModel);
		return new ResponseEntity<String>("Successfully created staff member", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody ResponseEntity<String> updateStaffMember(@PathVariable Long id, @RequestBody StaffMemberViewModel viewModel){
		log.info("Updating staff member : {}", viewModel);
		
		if(staffMemberService.findById(id) == null){
			log.info("No staff member found with id : {}", id);
			return new ResponseEntity<String>("No staff member found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		if(facilityService.findByName(viewModel.getFacility()) == null){
			log.info("No facility found with name : {}", viewModel.getFacility());
			return new ResponseEntity<String>(
					"No facility found with name : " + viewModel.getFacility(), HttpStatus.NOT_FOUND
					);
		}
		
		if(staffMemberService.find(viewModel.getFirstName()
				, viewModel.getLastName(), viewModel.getTitle()) != null){
			log.info("A staff member with firstname : {}, lastname : {}, title : {} already exist"
					,viewModel.getFirstName(), viewModel.getLastName(), viewModel.getTitle() );
			
			return new ResponseEntity<String>("A staff member with firstname : " + viewModel.getFirstName()
					+ " lastname : " + viewModel.getLastName()
					+ " title : " + viewModel.getTitle()
					+ " already exist", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		viewModel.setId(id);//Override the id
		viewModel = staffMemberService.createOrUpdateStaffMember(viewModel);
		
		log.info("Successfuly updated : {}", viewModel);
		return new ResponseEntity<String>("Successfully updated staff member", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<String> deleteStaffMember(@PathVariable Long id){
		
		log.info("Deleting staff member with id : {}", id);
		if(staffMemberService.findById(id) == null){
			log.info("No staff member found with id : {}", id);
			return new ResponseEntity<String>("No staff member found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		staffMemberService.deleteStaffMember(id);
		log.info("Staff member with id : {} successfully deleted");
		
		return new ResponseEntity<String>("Staff member successfully deleted", HttpStatus.OK);
	}
}