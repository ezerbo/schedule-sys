package com.rj.schedulesys.service;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rj.schedulesys.config.TestConfiguration;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class PositionTypeServiceTest {

	private @Autowired PositionTypeService positionTypeService;
	
	@Test
	public void test_findAll(){
		assertEquals(2, positionTypeService.findAll().size());
	}
	
	@Test
	public void test_findByName_WithNonExistingTestName(){
		assertNull(positionTypeService.findByName("This test type does not exist"));
	}
	
	@Test
	public void test_findByName_WithExistingTestName(){
		assertEquals(Long.valueOf(1), positionTypeService.findByName("NURSE").getId());
	}
	
	@Test
	public void test_findOne_WithNonExistingTestId(){
		assertNull(positionTypeService.findOne(0L));
	}
	
	@Test
	public void test_findOne_WithExistingTestId(){
		assertEquals("CAREGIVER", positionTypeService.findOne(2L).getName());
	}
}
