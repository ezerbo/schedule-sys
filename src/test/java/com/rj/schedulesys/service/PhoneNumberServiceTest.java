package com.rj.schedulesys.service;

import static org.junit.Assert.*;

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
import com.rj.schedulesys.view.model.PhoneNumberViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class PhoneNumberServiceTest {
	
	@Autowired
	private PhoneNumberService phoneNumberService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	
	@Test
	public void test_delete_WithNonExisitngPhoneNumberId(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No phone number found with id : 0 for employee with id : 0");
		phoneNumberService.delete(0L, 0L);
	}
	
	@Test
	public void test_delete_WithPrimaryPhoneNumber(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Primary phone number can not be deleted");
		phoneNumberService.delete(1L, 1L);
	}
	
	@Test
	public void test_delete_WithSecondaryPhoneNumber(){
		assertNotNull(phoneNumberService.findByNurseAndNumberId(1L, 2L));
		phoneNumberService.delete(1L, 2L);
		assertNull(phoneNumberService.findByNurseAndNumberId(1L, 2L));
	}
	
	@Test
	public void test_create_WithMalFormatted_PhoneNumber(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("number (718)--784-9658 is not a valid phone number");
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(
				null, "(718)--784-9658", "PRIMARY", "HOME");
		phoneNumberService.create(1L, aNewPhoneNumberViewModel);
	}
	
	@Test
	public void test_create_WithNonExistingPhoneNumberLabel(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No such phone number label : This label does not exist");
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(
				null, "(710)-784-9658", "This label does not exist", "HOME");
		phoneNumberService.create(1L, aNewPhoneNumberViewModel);
	}
	
	@Test
	public void test_create_WithNonExistingPhoneNumberType(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No such phone number type : This type does not exist");
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(
				null, "(710)-784-9658", "SECONDARY", "This type does not exist");
		phoneNumberService.create(10L, aNewPhoneNumberViewModel);
	}
	
	@Test
	public void test_create_WithNonExisistingEmployee(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No employee found with id : 0");
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(
				null, "(710)-784-9658", "SECONDARY", "This type does not exist");
		phoneNumberService.create(0L, aNewPhoneNumberViewModel);
	}
	
	@Test
	public void test_create_WithValidData(){
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(
				null, "(710)-784-9658", "PRIMARY", "HOME");
		PhoneNumberViewModel viewModel = phoneNumberService.create(10L, aNewPhoneNumberViewModel);
		assertNotNull(viewModel.getId());
	}
	
	@Test
	public void test_update_WithNonExistingEmployee(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No employee found with id : 0");
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(
				null, "(710)-784-9658", "SECONDARY", "This type does not exist");
		phoneNumberService.update(0L, aNewPhoneNumberViewModel);
	}
	
	@Test
	public void test_update_WithNonExistingPhoneNumberId(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No phone number found with id : 0 for employee with id : 1");
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(
				0L, "(710)-784-9658", "SECONDARY", "HOME");
		phoneNumberService.update(1L, aNewPhoneNumberViewModel);
	}
	
	@Test
	public void test_update_WithExistingPhoneNumberLabel(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Employee with id : 1 already has a SECONDARY phone number");
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(
				1L, "(710)-784-9658", "SECONDARY", "HOME");
		phoneNumberService.update(1L, aNewPhoneNumberViewModel);
	}
	
	@Test
	public void test_update_WithWithPhoneNumberBelongingToAnotherEmployee(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Phone number : (718)-790-9836 is already in use");
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(
				10L, "(718)-790-9836", "SECONDARY", "HOME");
		phoneNumberService.update(4L, aNewPhoneNumberViewModel);
	}
	
}