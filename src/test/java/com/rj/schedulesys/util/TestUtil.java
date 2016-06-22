package com.rj.schedulesys.util;

import java.io.IOException;

import org.joda.time.LocalTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.schedulesys.view.model.FacilityViewModel;
import com.rj.schedulesys.view.model.SchedulePostStatusViewModel;
import com.rj.schedulesys.view.model.ScheduleStatusViewModel;
import com.rj.schedulesys.view.model.ScheduleSysUserViewModel;
import com.rj.schedulesys.view.model.ShiftViewModel;
import com.rj.schedulesys.view.model.StaffMemberViewModel;

public class TestUtil {
	
	private TestUtil(){
		//Utility class, not to be instantiated  
	}
	
	public static byte [] convertObjectToJsonBytes(Object object) throws IOException{
		if(object == null){
			throw new IllegalArgumentException("Please provided object to be converted to Json");
		}
		return new ObjectMapper().writeValueAsBytes(object);
	}
	
	public static ScheduleSysUserViewModel aNewScheduleSysUserViewModel(Long id, String username
			, String password, String userRole){
		ScheduleSysUserViewModel viewModel = ScheduleSysUserViewModel.builder()
				.id(id)
				.username(username)
				.password(password)
				.userRole(userRole)
				.build();
		return viewModel;
	}
	
	public static FacilityViewModel aNewFacilityViewModel(Long id, String name, String address
			, String phoneNumber, String fax){
		FacilityViewModel viewModel = FacilityViewModel.builder()
				.id(id)
				.name(name)
				.address(address)
				.phoneNumber(phoneNumber)
				.fax(fax)
				.build();
		return viewModel;
		
	}
	
	public static StaffMemberViewModel aNewStaffMemberViewModel(Long id, String firstName
			, String lastName, String title, String facilityName){
		StaffMemberViewModel viewModel = StaffMemberViewModel.builder()
				.id(id)
				.firstName(firstName)
				.lastName(lastName)
				.title(title)
				.facilityName(facilityName)
				.build();
		return viewModel;
	}
	
	public static ShiftViewModel aNewShiftViewModel(Long id, String name, LocalTime startTime, LocalTime endTime){
		ShiftViewModel viewModel = ShiftViewModel.builder()
				.id(id)
				.name(name)
				.startTime(startTime)
				.endTime(endTime)
				.build(); 
		return viewModel;
	}
	
	
	public static ScheduleStatusViewModel aNewScheduleStatusViewModel(Long id, String status){
		ScheduleStatusViewModel viewModel = ScheduleStatusViewModel.builder()
				.id(id)
				.status(status)
				.build();
		return viewModel;
	}

	public static SchedulePostStatusViewModel aNewSchedulePostStatusViewModel(Long id, String status) {
		SchedulePostStatusViewModel viewModel = SchedulePostStatusViewModel.builder()
				.id(id)
				.status(status)
				.build();
		return viewModel;
	}

}
