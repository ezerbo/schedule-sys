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

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class CareGiverServiceTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Autowired
	private CareGiverService careGiverService;
	
	@Test
	public void test_findOne_WithNonExistingCareGiver(){
		assertNull(careGiverService.findOne(0L));
	}
	
	@Test
	public void test_findOne_WithExistingCareGiver(){
		assertEquals("SANOGO", careGiverService.findOne(6L).getLastName());
	}
	
	@Test
	public void test_findAllByPosition_WithNonExistingPositionName(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No position found with name : This position does not exist");
		careGiverService.findAllByPosition("This position does not exist");
	}
	
	@Test
	public void test_findAllByPosition_WithExistingPositionName(){
		assertEquals(4, careGiverService.findAllByPosition("COMPANION").size());
	}
	
	
}
