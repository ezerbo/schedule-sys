package com.rj.schedulesys.util;

import java.io.IOException;
import java.util.Date;

import org.joda.time.LocalTime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.schedulesys.view.model.CreateScheduleViewModel;
import com.rj.schedulesys.view.model.EmployeeViewModel;
import com.rj.schedulesys.view.model.FacilityViewModel;
import com.rj.schedulesys.view.model.LicenseTypeViewModel;
import com.rj.schedulesys.view.model.LicenseViewModel;
import com.rj.schedulesys.view.model.EmployeeTestViewModel;
import com.rj.schedulesys.view.model.NurseViewModel;
import com.rj.schedulesys.view.model.PhoneNumberViewModel;
import com.rj.schedulesys.view.model.PositionViewModel;
import com.rj.schedulesys.view.model.SchedulePostStatusViewModel;
import com.rj.schedulesys.view.model.ScheduleStatusViewModel;
import com.rj.schedulesys.view.model.ScheduleSysUserViewModel;
import com.rj.schedulesys.view.model.ShiftViewModel;
import com.rj.schedulesys.view.model.StaffMemberViewModel;
import com.rj.schedulesys.view.model.TestSubCategoryViewModel;
import com.rj.schedulesys.view.model.TestViewModel;
import com.rj.schedulesys.view.model.UpdateScheduleViewModel;

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
			, String emailAddress, String userRole){
		ScheduleSysUserViewModel viewModel = ScheduleSysUserViewModel.builder()
				.id(id)
				.username(username)
				.userRole(userRole)
				.emailAddress(emailAddress)
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
			, String lastName, String title, String phoneNumber, String fax, Long facilityId){
		StaffMemberViewModel viewModel = StaffMemberViewModel.builder()
				.id(id)
				.firstName(firstName)
				.lastName(lastName)
				.title(title)
				.phoneNumber(phoneNumber)
				.fax(fax)
				.facilityId(facilityId)
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
	
	public static TestViewModel aNewTestViewModel(Long id, String name, Boolean allowNotApplicable
			, Boolean hasCompletedDate, Boolean hasExpirationDate){
		TestViewModel viewModel = TestViewModel.builder()
				.id(id)
				.name(name)
				.allowNotApplicable(allowNotApplicable)
				.hasCompletedDate(hasCompletedDate)
				.hasExpirationDate(hasExpirationDate)
				.build();
		return viewModel;
	}
	
	public static TestSubCategoryViewModel aNewTestSubCategoryViewModel(Long id, String name, Long testId){
		TestSubCategoryViewModel viewModel = TestSubCategoryViewModel.builder()
				.id(id)
				.name(name)
				.testId(testId)
				.build();
		return viewModel;
	}
	
	public static PositionViewModel aNewPositionViewModel(Long id, String name, String positionName){
		PositionViewModel viewModel = PositionViewModel.builder()
				.id(id)
				.name(name)
				.positionTypeName(positionName)
				.build();
		return viewModel;
	}
	
	
	public static NurseViewModel aNewNurseViewModel(Long id, String firstName, String lastName, String positionName
			,Boolean ebc, Date dateOfHire, Date rehireDate, Date lastDayOfWork, String comment){
		NurseViewModel viewModel = NurseViewModel.builder()
				.id(id)
				.firstName(firstName)
				.lastName(lastName)
				.positionName(positionName)
				.ebc(ebc)
				.dateOfHire(dateOfHire)
				.rehireDate(rehireDate)
				.lastDayOfWork(lastDayOfWork)
				.comment(comment)
				.build();
		return viewModel;
	}
	
	public static EmployeeViewModel aNewEmployeeViewModel(Long id, String firstName, String lastName, String positionName
			,Boolean ebc, Date dateOfHire, Date rehireDate, Date lastDayOfWork, String comment){
		EmployeeViewModel viewModel = EmployeeViewModel.builder()
				.id(id)
				.firstName(firstName)
				.lastName(lastName)
				.positionName(positionName)
				.ebc(ebc)
				.dateOfHire(dateOfHire)
				.rehireDate(rehireDate)
				.lastDayOfWork(lastDayOfWork)
				.comment(comment)
				.build();
		return viewModel;
		
	}
	
	
	public static PhoneNumberViewModel aNewPhoneNumberViewModel(Long id, String number, String numberLabel, String numberType){
		PhoneNumberViewModel viewModel = PhoneNumberViewModel.builder()
				.id(id)
				.number(number)
				.numberLabel(numberLabel)
				.numberType(numberType)
				.build();
		return viewModel;
	}
	
	public static LicenseViewModel aNewLicenseViewModel(Long id, Long licenseTypeId, Long nurseId, String number, Date expirationDate){
		LicenseViewModel viewModel = LicenseViewModel.builder()
				.id(id)
				.licenseTypeId(licenseTypeId)
				.nurseId(nurseId)
				.number(number)
				.expirationDate(expirationDate)
				.build();
		return viewModel;
	}
	
	public static EmployeeTestViewModel aNewNurseTestViewModel(Long testId, Long employeeId, Long testSubCategoryId, String status
			, Date completedDate, Date expirationDate){
		EmployeeTestViewModel viewModel = EmployeeTestViewModel.builder()
				.employeeId(employeeId)
				.testId(testId)
				.testSubCategoryId(testSubCategoryId)
				.status(status)
				.completedDate(completedDate)
				.expirationDate(expirationDate)
				.build();
		return viewModel;
	}
	
	public static CreateScheduleViewModel aNewCreateScheduleViewModel(Long employeeId, Long facilityId, Long shiftId
			, Long scheduleStatusId, Long schedulePostStatusId, Date scheduleDate, String comment){
		CreateScheduleViewModel viewModel = CreateScheduleViewModel.builder()
				.employeeId(employeeId)
				.facilityId(facilityId)
				.shiftId(shiftId)
				.scheduleStatusId(scheduleStatusId)
				.schedulePostStatusId(schedulePostStatusId)
				.scheduleDate(scheduleDate)
				.comment(comment)
				.build();
		return viewModel;
	}
	
	public static UpdateScheduleViewModel aNewUpdateScheduleViewModel(Long id, Long employeeId, Long facilityId
			, Long shiftId, Long scheduleStatusId, Long schedulePostStatusId, Double hours
			, Double overtime, Boolean timesheetReceived, Date scheduleDate, String comment){
		UpdateScheduleViewModel viewModel = UpdateScheduleViewModel.builder()
				.id(id)
				.employeeId(employeeId)
				.facilityId(facilityId)
				.shiftId(shiftId)
				.scheduleStatusId(scheduleStatusId)
				.schedulePostStatusId(schedulePostStatusId)
				.hours(hours)
				.overtime(overtime)
				.timesheetReceived(timesheetReceived)
				.scheduleDate(scheduleDate)
				.comment(comment)
				.build();
		return viewModel;
	}
	
	public static LicenseTypeViewModel aNewLicenseTypeViewModel(Long id, String typeName){
		LicenseTypeViewModel viewModel = LicenseTypeViewModel.builder()
				.id(id)
				.typeName(typeName)
				.build();
		return viewModel;
	}
	
}