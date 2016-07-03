package com.rj.schedulesys.service;

import static org.junit.Assert.*;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rj.schedulesys.config.TestConfiguration;
import com.rj.schedulesys.dao.NurseTestDao;
import com.rj.schedulesys.data.NurseTestStatusConstants;
import com.rj.schedulesys.domain.NurseTestPK;
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.NurseTestViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class NurseTestServiceTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Autowired
	private NurseTestService nurseTestService;
	
	@Autowired
	private NurseTestDao nurseTestDao;
	
	@Test
	public void test_create_WithNonExistingNurse(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No nurse found with id : 0");
		
		
		NurseTestViewModel aNewNurseTestViewModel = TestUtil.aNewNurseTestViewModel(
				1L, 0L, 1L, NurseTestStatusConstants.APPLICABLE_STATUS, new Date(), new Date()
				);
		
		nurseTestService.createOrUpate(aNewNurseTestViewModel);
	}
	
	@Test
	public void test_create_WithNonExistingTest(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No test found with id : 0");
		
		
		NurseTestViewModel aNewNurseTestViewModel = TestUtil.aNewNurseTestViewModel(
				0L, 1L, 1L, NurseTestStatusConstants.APPLICABLE_STATUS, new Date(), new Date()
				);
		
		nurseTestService.createOrUpate(aNewNurseTestViewModel);
	}
	
	@Test
	public void test_create_WithNonExisingTestSubCategory(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No test sub category found with id : 0");
		
		
		NurseTestViewModel aNewNurseTestViewModel = TestUtil.aNewNurseTestViewModel(
				1L, 1L, 0L, NurseTestStatusConstants.APPLICABLE_STATUS, new Date(), new Date()
				);
		
		nurseTestService.createOrUpate(aNewNurseTestViewModel);
	}
	
	@Test
	public void test_create_WithNoCompletionDateForTestThatRequiresCompletionDate(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A complete date must be provided");
		
		
		NurseTestViewModel aNewNurseTestViewModel = TestUtil.aNewNurseTestViewModel(
				1L, 1L, null, NurseTestStatusConstants.APPLICABLE_STATUS, null, new Date()
				);
		
		nurseTestService.createOrUpate(aNewNurseTestViewModel);
	}
	
	@Test
	public void test_create_WithNoExpirationDateForTestThatRequiresExpirationDate(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("An expiration date must be provided");
		
		
		NurseTestViewModel aNewNurseTestViewModel = TestUtil.aNewNurseTestViewModel(
				2L, 1L, null, NurseTestStatusConstants.APPLICABLE_STATUS, new Date(), null
				);
		
		nurseTestService.createOrUpate(aNewNurseTestViewModel);
	}
	
	@Test
	public void test_create_WithValidData(){
		
		NurseTestViewModel aNewNurseTestViewModel = TestUtil.aNewNurseTestViewModel(
				1L, 1L, null, NurseTestStatusConstants.APPLICABLE_STATUS
				, new Date(System.currentTimeMillis() - (24 * 60 * 60 *1000)), new Date(System.currentTimeMillis() + (24 * 60 * 60 *1000))
				);
		
		NurseTestViewModel viewModel = nurseTestService.createOrUpate(aNewNurseTestViewModel);
		
		assertNotNull(viewModel);
	}
	
	@Test
	public void test_delete_WithNonExistingNurseTest(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No data found with nurseId : 0 and testId : 0");
		NurseTestPK nurseTestPK = NurseTestPK.builder()
				.testId(0L)
				.nurseId(0L)
				.build();
		nurseTestService.delete(nurseTestPK);
	}
	
	@Test
	public void test_delete_WithExistingNurseTest(){
		NurseTestPK nurseTestPK = NurseTestPK.builder()
				.testId(4L)
				.nurseId(5L)
				.build();
		
		assertNotNull(nurseTestDao.findOne(nurseTestPK));
		
		nurseTestService.delete(nurseTestPK);
		
		assertNull(nurseTestDao.findOne(nurseTestPK));
	}
	
	@Test
	public void test_findAllByNurse(){
		assertEquals(2, nurseTestService.findAllByNurse(4L).size());
	}
	
}