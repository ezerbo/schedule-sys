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
import com.rj.schedulesys.view.model.PhoneNumberLabelViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class PhoneNumberLabelServiceTest {
	
	private @Autowired PhoneNumberLabelService phoneNumberLabelService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void test_findAll(){
		assertEquals(3, phoneNumberLabelService.findAll().size());
	}
	
	@Test
	public void test_findOne_WithNonExistingId(){
		assertNull(phoneNumberLabelService.findOne(0L));
	}
	
	@Test
	public void test_findOne_WithExistingId(){
		PhoneNumberLabelViewModel viewModel = phoneNumberLabelService.findOne(1L);
		assertEquals("PRIMARY", viewModel.getName());
	}
	
	@Test
	public void test_findByName_WithNonExistingName(){
		assertNull(phoneNumberLabelService.findByName("This name does not exists"));
	}
	
	@Test
	public void test_findByName_WithExistingName(){
		PhoneNumberLabelViewModel viewModel = phoneNumberLabelService.findByName("SECONDARY");
		assertEquals(Long.valueOf(2), viewModel.getId());
	}

}