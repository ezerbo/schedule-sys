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
import com.rj.schedulesys.dao.EmployeeTestDao;
import com.rj.schedulesys.data.EmployeeTestStatusConstants;
import com.rj.schedulesys.domain.EmployeTestPK;
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.EmployeeTestViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class EmployeeTestServiceTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Autowired
	private EmployeeTestService nurseTestService;
	
	@Autowired
	private EmployeeTestDao nurseTestDao;
	
	@Test
	public void test_create_WithNonExistingNurse(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No employee found with id : 0");
		
		
		EmployeeTestViewModel aNewNurseTestViewModel = TestUtil.aNewNurseTestViewModel(
				1L, 0L, 1L, EmployeeTestStatusConstants.APPLICABLE_STATUS, new Date(), new Date()
				);
		
		nurseTestService.createOrUpate(aNewNurseTestViewModel);
	}
	
	@Test
	public void test_create_WithNonExistingTest(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No test found with id : 0");
		
		
		EmployeeTestViewModel aNewNurseTestViewModel = TestUtil.aNewNurseTestViewModel(
				0L, 1L, 1L, EmployeeTestStatusConstants.APPLICABLE_STATUS, new Date(), new Date()
				);
		
		nurseTestService.createOrUpate(aNewNurseTestViewModel);
	}
	
	@Test
	public void test_create_WithNonExisingTestSubCategory(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No test sub category found with id : 0");
		
		
		EmployeeTestViewModel aNewNurseTestViewModel = TestUtil.aNewNurseTestViewModel(
				1L, 1L, 0L, EmployeeTestStatusConstants.APPLICABLE_STATUS, new Date(), new Date()
				);
		
		nurseTestService.createOrUpate(aNewNurseTestViewModel);
	}
	
	@Test
	public void test_create_WithNoCompletionDateForTestThatRequiresCompletionDate(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A complete date must be provided");
		
		
		EmployeeTestViewModel aNewNurseTestViewModel = TestUtil.aNewNurseTestViewModel(
				1L, 1L, null, EmployeeTestStatusConstants.APPLICABLE_STATUS, null, new Date()
				);
		
		nurseTestService.createOrUpate(aNewNurseTestViewModel);
	}
	
	@Test
	public void test_create_WithNoExpirationDateForTestThatRequiresExpirationDate(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("An expiration date must be provided");
		
		
		EmployeeTestViewModel aNewNurseTestViewModel = TestUtil.aNewNurseTestViewModel(
				2L, 1L, null, EmployeeTestStatusConstants.APPLICABLE_STATUS, new Date(), null
				);
		
		nurseTestService.createOrUpate(aNewNurseTestViewModel);
	}
	
	@Test
	public void test_create_WithValidData(){
		
		EmployeeTestViewModel aNewNurseTestViewModel = TestUtil.aNewNurseTestViewModel(
				1L, 1L, null, EmployeeTestStatusConstants.APPLICABLE_STATUS
				, new Date(System.currentTimeMillis() - (24 * 60 * 60 *1000)), new Date(System.currentTimeMillis() + (24 * 60 * 60 *1000))
				);
		
		EmployeeTestViewModel viewModel = nurseTestService.createOrUpate(aNewNurseTestViewModel);
		
		assertNotNull(viewModel);
	}
	
	@Test
	public void test_delete_WithNonExistingNurseTest(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No data found with nurseId : 0 and testId : 0");
		EmployeTestPK nurseTestPK = EmployeTestPK.builder()
				.testId(0L)
				.employeeId(0L)
				.build();
		nurseTestService.delete(nurseTestPK);
	}
	
	@Test
	public void test_delete_WithExistingNurseTest(){
		EmployeTestPK nurseTestPK = EmployeTestPK.builder()
				.testId(4L)
				.employeeId(5L)
				.build();
		
		assertNotNull(nurseTestDao.findOne(nurseTestPK));
		
		nurseTestService.delete(nurseTestPK);
		
		assertNull(nurseTestDao.findOne(nurseTestPK));
	}
	
	@Test
	public void test_findAllByNurse(){
		assertEquals(2, nurseTestService.findAllByEmployee(4L).size());
	}
	
}