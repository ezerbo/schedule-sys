package com.rj.schedulesys.service;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rj.schedulesys.config.TestConfiguration;
import com.rj.schedulesys.view.model.PhoneNumberTypeViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class PhoneNumberServiceTest {

	private @Autowired PhoneNumberTypeService phoneNumberTypeService;
	
	@Test
	public void test_findAll(){
		assertEquals(2, phoneNumberTypeService.findAll().size());
	}
	
	@Test
	public void test_findOne_WithNonExistingId(){
		assertNull(phoneNumberTypeService.findOne(0L));
	}
	
	@Test
	public void test_findOne_WithExistingId(){
		PhoneNumberTypeViewModel viewModel = phoneNumberTypeService.findOne(2L);
		assertEquals("MOBILE", viewModel.getName());
	}
	
	@Test
	public void test_findByName_WithNonExistingName(){
		assertNull(phoneNumberTypeService.findByName("This name does not exist"));
	}
	
	@Test
	public void test_findByName_WithExistingName(){
		PhoneNumberTypeViewModel viewModel = phoneNumberTypeService.findByName("HOME");
		assertEquals(Long.valueOf(1), viewModel.getId());
	}
}