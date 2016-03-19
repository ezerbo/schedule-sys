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
//http://stackoverflow.com/questions/30126754/how-to-implement-basic-spring-security-session-management-for-single-page-angu
@Slf4j
@Controller
public class StaffMemberController {
	
	private @Autowired FacilityService facilityService;
	private @Autowired StaffMemberService staffMemberService;
	
	
	@RequestMapping(value ="/facilities/{id}/staff-members", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAllStaffMembers(@PathVariable Long id){
		log.info("Finding all staff members ");
		
		if(facilityService.findActiveById(id) == null){
			log.info("No ficility found with id : {}", id);
			return new ResponseEntity<String>("No facility found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		List<StaffMemberViewModel> viewModels = staffMemberService.findAllByFacilityId(id);
		
		if(viewModels.isEmpty()){
			log.info("No staff members found");
			return new ResponseEntity<String>("No staff member found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Staff members found : {}", viewModels);
		return new ResponseEntity<List<StaffMemberViewModel>>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/facilities/{id}/staff-members/{smId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findStaffMemberById(@PathVariable("id") Long facilityId
			, @PathVariable("smId") Long staffMemberId){
		log.info("Finding staff member by id {} in facility with id : {}", staffMemberId, facilityId);
		
		if(facilityService.findActiveById(facilityId) == null){
			log.info("No ficility found with id : {}", facilityId);
			return new ResponseEntity<String>("No facility found with id : " + facilityId, HttpStatus.NOT_FOUND);
		}
		
		StaffMemberViewModel viewModel = staffMemberService.findByFacilityIdAndStaffMemberId(facilityId, staffMemberId);
		
		if(viewModel == null){
			log.info("No staff member found with id : {}", staffMemberId);
			return new ResponseEntity<String>(
					"No staff member found with id : " + staffMemberId + "in facility with id : " + facilityId
					, HttpStatus.NOT_FOUND
					);
		}
		
		log.info("Staff member found : {}", viewModel);
		return new ResponseEntity<StaffMemberViewModel>(viewModel, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/facilities/{id}/staff-members", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<String> createStaffMember(@RequestBody StaffMemberViewModel viewModel
			, @PathVariable("id") Long facilityId){
		log.info("Creating new staff member : {}", viewModel);
		
		if(facilityService.findActiveById(facilityId) == null){
			log.info("No ficility found with id : {}", facilityId);
			return new ResponseEntity<String>("No facility found with id : " + facilityId, HttpStatus.NOT_FOUND);
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
		
		viewModel = staffMemberService.createOrUpdateStaffMember(viewModel, facilityId);
		
		log.info("Successfuly created : {}", viewModel);
		return new ResponseEntity<String>("Successfully created staff member", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/facilities/{id}/staff-members/{smId}", method = RequestMethod.PUT, consumes = "application/json")
	public @ResponseBody ResponseEntity<String> updateStaffMember(@PathVariable("id") Long facilityId
			, @PathVariable("smId") Long staffMemberId
			, @RequestBody StaffMemberViewModel viewModel){
		log.info("Updating staff member : {}", viewModel);
		
		if(facilityService.findActiveById(facilityId) == null){
			log.info("No ficility found with id : {}", facilityId);
			return new ResponseEntity<String>("No facility found with id : " + facilityId, HttpStatus.NOT_FOUND);
		}
		
		if(staffMemberService.findByFacilityIdAndStaffMemberId(facilityId, staffMemberId) == null){
			log.info("No staff member found with id : {}", staffMemberId);
			return new ResponseEntity<String>("No staff member found with id : " + staffMemberId, HttpStatus.NOT_FOUND);
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
		
		viewModel.setId(staffMemberId);//Override the id
		viewModel = staffMemberService.createOrUpdateStaffMember(viewModel, facilityId);
		
		log.info("Successfuly updated : {}", viewModel);
		return new ResponseEntity<String>("Successfully updated staff member", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/facilities/{id}/staff-members/{smId}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<String> deleteStaffMember(@PathVariable("id") Long facilityId
			, @PathVariable("smId") Long staffMemberId){
		
		if(facilityService.findActiveById(facilityId) == null){
			log.info("No ficility found with id : {}", facilityId);
			return new ResponseEntity<String>("No facility found with id : " + facilityId, HttpStatus.NOT_FOUND);
		}
		
		log.info("Deleting staff member with id : {}", staffMemberId);
		if(staffMemberService.findByFacilityIdAndStaffMemberId(facilityId, staffMemberId) == null){
			log.info("No staff member found with id : {}", facilityId);
			return new ResponseEntity<String>("No staff member found with id : " + facilityId, HttpStatus.NOT_FOUND);
		}
		
		staffMemberService.deleteStaffMember(staffMemberId);
		log.info("Staff member with id : {} successfully deleted");
		
		return new ResponseEntity<String>("Staff member successfully deleted", HttpStatus.OK);
	}
}