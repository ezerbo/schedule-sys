package com.rj.schedulesys.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rj.schedulesys.config.TestConfiguration;
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.EmployeeViewModel;
import com.rj.schedulesys.view.model.NurseViewModel;
import com.rj.schedulesys.view.model.PhoneNumberViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class NurseServiceTest {

	@Autowired
	private NurseService nurseService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void test_findAllByPositon(){
		assertEquals(3, nurseService.findAllByPosition("RN").size());
	}
	
	@Test
	public void test_findAllByPosition_WithNonExisitingPosition(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No position found with name : This position does not exist");
		
		nurseService.findAllByPosition("This position does not exist");
	}
	
	@Test
	public void test_findAll(){
		assertEquals(5, nurseService.findAll().size());
	}
	
	@Test
	public void test_findOne_WithNonExistingNurseId(){
		assertNull(nurseService.findOne(0L));
	}
	
	@Test
	public void test_findOne_WithExistingNurseId(){
		NurseViewModel viewModel = nurseService.findOne(1L);
		
		assertEquals("ZERBO", viewModel.getLastName());
		assertEquals("RN", viewModel.getPositionName());
		
		List<PhoneNumberViewModel> phoneNumbers = viewModel.getPhoneNumbers();
		
		assertEquals(3, phoneNumbers.size());
		PhoneNumberViewModel phoneNumber = phoneNumbers.get(0);
		assertEquals("(718)-790-9836", phoneNumber.getNumber());
		assertEquals("PRIMARY", phoneNumber.getNumberLabel());
		assertEquals("HOME", phoneNumber.getNumberType());
	}
	
	@Test
	public void test_delete_WithNonExisitngId(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No nurse found with id : 0");
		nurseService.delete(0L);
	}
	
	@Test
	public void test_delete_WithExistinNurseThatHasNoSchedulesAndHasTakenNoTest(){
		nurseService.delete(3L);
		assertNull(nurseService.findOne(3L));
	}
	
	@Test
	public void test_create_WithNonExisitngPositionName(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No position found with name : This position does not exist");
		
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(null, "(718)-720-9836", "PRIMARY", "MOBILE");
		EmployeeViewModel aNewEmployeeViewModel = TestUtil.aNewEmployeeViewModel(0L, "firstName", "lastName", "This position does not exist"
				, true, new Date(), null, null, null);
		aNewEmployeeViewModel.setPhoneNumbers(Arrays.asList(aNewPhoneNumberViewModel));
		employeeService.create(aNewEmployeeViewModel);
	}
	
	@Test
	public void test_create_WithExisingPosition(){
		EmployeeViewModel aNewEmployeeViewModel = TestUtil.aNewEmployeeViewModel(null, "firstName", "lastName", "LNP"
				, true, new Date(), null, null, null);
		
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(null, "(718)-720-9836", "PRIMARY", "MOBILE");
		
		aNewEmployeeViewModel.setPhoneNumbers(Arrays.asList(aNewPhoneNumberViewModel));
		
		EmployeeViewModel createdEmployee = employeeService.create(aNewEmployeeViewModel);
		assertNotNull(createdEmployee.getId());
		
		nurseService.create(createdEmployee);
	}
	
	@Test
	public void test_create_WithDuplicatePhoneNumberLabels(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Duplicate phone number label : PRIMARY");
		
		EmployeeViewModel aNewEmployeeViewModel = TestUtil.aNewEmployeeViewModel(0L, "firstName", "lastName", "LNP"
				, true, new Date(), null, null, null);
		
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(null, "(718)-720-9836", "PRIMARY", "MOBILE");
		PhoneNumberViewModel aNewPhoneNumberViewModel2 = TestUtil.aNewPhoneNumberViewModel(null, "(717)-720-9836", "PRIMARY", "MOBILE");
		PhoneNumberViewModel aNewPhoneNumberViewModel3 = TestUtil.aNewPhoneNumberViewModel(null, "(728)-720-9836", "SECONDARY", "MOBILE");
		
		aNewEmployeeViewModel.setPhoneNumbers(Arrays.asList(aNewPhoneNumberViewModel, aNewPhoneNumberViewModel2, aNewPhoneNumberViewModel3));
		
		employeeService.create(aNewEmployeeViewModel);
	}
	
	@Test
	public void test_update_WithNonExisitingNurseId(){
		EmployeeViewModel aNewNurseViewModel = TestUtil.aNewEmployeeViewModel(2L, "firstName", "lastName", "LNP"
				, true, new Date(), null, null, null);
		
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(null, "(718)-100-9836", "PRIMARY", "MOBILE");
		
		aNewNurseViewModel.setPhoneNumbers(Arrays.asList(aNewPhoneNumberViewModel));
		
		aNewNurseViewModel = employeeService.update(aNewNurseViewModel);
		
		assertEquals(aNewNurseViewModel.getLastName(), nurseService.findOne(2L).getLastName());
	}
}