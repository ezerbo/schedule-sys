package com.rj.schedulesys.view.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rj.schedulesys.service.FacilityService;
import com.rj.schedulesys.service.FacilityScheduleService;
import com.rj.schedulesys.service.StaffMemberService;
import com.rj.schedulesys.view.model.FacilityViewModel;
import com.rj.schedulesys.view.model.GetScheduleViewModel;
import com.rj.schedulesys.view.model.StaffMemberViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/facilities")
public class FacilityController {
	
	private @Autowired FacilityService facilityService;
	private @Autowired FacilityScheduleService scheduleService;
	private @Autowired StaffMemberService staffMemberService;

	/**
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAll(){
		
		log.info("Finding all facilities");
		
		List<FacilityViewModel> viewModels = facilityService.findAll();
		if(viewModels.isEmpty()){
			return new ResponseEntity<String>("No facility found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Facilities found : {}", viewModels);
		
		return new ResponseEntity<List<FacilityViewModel>>(viewModels, HttpStatus.OK);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findOne(@PathVariable Long id){
		
		log.info("Fetching facility with id : {}", id);
		
		FacilityViewModel viewModel = facilityService.findOne(id);
		
		if(viewModel == null){
			return new ResponseEntity<String>(
					"No facility found with either id : " + id, HttpStatus.NOT_FOUND
					);
		}
		
		log.info("Facility found : {}", viewModel);
		
		return new ResponseEntity<FacilityViewModel>(viewModel, HttpStatus.OK);
		
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody ResponseEntity<?> create(@RequestBody FacilityViewModel viewModel){
		
		log.info("Creating new facility : {}", viewModel);
		
		try{
			viewModel = facilityService.create(viewModel);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Successfully created facility : {}", viewModel);
		
		return new ResponseEntity<String>("Facility successfully created", HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public @ResponseBody ResponseEntity<?> update(@PathVariable Long id, @RequestBody FacilityViewModel viewModel){
		
		log.info("Updating facility with id: {}", id);
		
		if(facilityService.findOne(id) == null){
			log.info("No facility found with id : {}", id);
			return new ResponseEntity<String>("No facility found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		viewModel.setId(id);//overriding the id
		
		try{
			viewModel = facilityService.update(viewModel);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Facility successfully updated : {}", viewModel);
		
		return new ResponseEntity<String>("Facility successfully updated", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public @ResponseBody ResponseEntity<?> delete(@PathVariable Long id){
		
		log.info("Deleting facility with id : {}", id);
		
		if(facilityService.findOne(id) == null){
			return new ResponseEntity<String>("No facility found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		try{
			facilityService.delete(id);
		}catch(Exception e){
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		log.info("Facility with id successfully deleted : {}", id);
		
		return new ResponseEntity<String>("Facility successfully deleted", HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/schedules", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> findSchedules(@PathVariable Long id, @RequestParam(required = false) Date startDate
			, @RequestParam(required = false) Date endDate){
		log.info("Finding schedules between startDate : {} and endDate : {} for facility with id : {}", startDate, endDate);
		
		if(facilityService.findOne(id) == null){
			log.info("No facility found with id : {}", id);
			return new ResponseEntity<String>("No facility found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		List<GetScheduleViewModel> viewModels = null;
		
		if(startDate == null || endDate == null){
			viewModels = scheduleService.findAllByFacility(id);
		}else{
			viewModels = scheduleService.findAllBetweenDatesByFacility(startDate, endDate, id);
		}
		
		if(viewModels.isEmpty()){
			log.info("No schedules found between : {} and : {} for facility with id : {}", startDate, endDate, id);
			return new ResponseEntity<>(
					"No schedules found between : " + startDate + " and : " + endDate + " for facility with id : " + id, HttpStatus.NOT_FOUND
					);
		}
	
		log.info("Schedules found : {}", viewModels);
		
		return new ResponseEntity<>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value ="/{id}/staff-members", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody ResponseEntity<?> findAllStaffMembers(@PathVariable Long id){
		
		log.info("Finding all staff members ");
		
		if(facilityService.findOne(id) == null){
			log.warn("No ficility found with id : {}", id);
			return new ResponseEntity<String>("No facility found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		List<StaffMemberViewModel> viewModels = staffMemberService.findAllByFacility(id);
		
		if(viewModels.isEmpty()){
			log.info("No staff members found");
			return new ResponseEntity<String>("No staff member found", HttpStatus.NOT_FOUND);
		}
		
		log.info("Staff members found : {}", viewModels);
		
		return new ResponseEntity<List<StaffMemberViewModel>>(viewModels, HttpStatus.OK);
	}
	
	@RequestMapping(value ="/{id}/staff-members", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody ResponseEntity<?> addStaffMember(@PathVariable Long id, @RequestBody StaffMemberViewModel viewModel){
		log.info("Creating staff member : {} ", viewModel);
		if(facilityService.findOne(id) == null){
			log.warn("No ficility found with id : {}", id);
			return new ResponseEntity<String>("No facility found with id : " + id, HttpStatus.NOT_FOUND);
		}
		viewModel.setFacilityId(id);
		try{
			staffMemberService.create(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Staff member successfully created");
		return new ResponseEntity<>("Staff member successfully created", HttpStatus.OK);
	}
	
	@RequestMapping(value ="/{id}/staff-members/{staffMemberId}", method = RequestMethod.PUT, produces = "application/json")
	public @ResponseBody ResponseEntity<?> updateStaffMember(@PathVariable Long id, @PathVariable Long staffMemberId,
			@RequestBody StaffMemberViewModel viewModel){
		log.info("Updating staff member : {}", viewModel);
		FacilityViewModel facility = facilityService.findOne(id);
		if(facility == null){
			log.warn("No ficility found with id : {}", id);
			return new ResponseEntity<String>("No facility found with id : " + id, HttpStatus.NOT_FOUND);
		}
		
		StaffMemberViewModel staffMember = staffMemberService.findOne(staffMemberId);
		if(staffMember == null){
			log.error("No staff member found with id : {}", staffMemberId);
			return new ResponseEntity<String>("No staff member found with id : " + staffMemberId, HttpStatus.NOT_FOUND);
		}
		
		if(facility.getId() != staffMember.getFacilityId()){
			log.warn("No staff member with id : {} found for facility with id : {}", staffMemberId, id);
			return new ResponseEntity<String>("No staff member with id : " + staffMemberId 
					+ " found for facility with id : " + id, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		viewModel.setFacilityId(id);
		viewModel.setId(staffMemberId);
		try{
			staffMemberService.update(viewModel);
		}catch(Exception e){
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Staff member successfully created");
		return new ResponseEntity<>("Staff member successfully created", HttpStatus.OK);
	}
}