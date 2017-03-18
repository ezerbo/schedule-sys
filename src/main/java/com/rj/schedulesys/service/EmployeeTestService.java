package com.rj.schedulesys.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.schedulesys.dao.EmployeeDao;
import com.rj.schedulesys.dao.EmployeeTestDao;
import com.rj.schedulesys.dao.TestDao;
import com.rj.schedulesys.dao.TestSubCategoryDao;
import com.rj.schedulesys.data.EmployeeTestStatusConstants;
import com.rj.schedulesys.domain.EmployeTestPK;
import com.rj.schedulesys.domain.Employee;
import com.rj.schedulesys.domain.EmployeeTest;
import com.rj.schedulesys.domain.Test;
import com.rj.schedulesys.domain.TestSubCategory;
import com.rj.schedulesys.view.model.GetNurseTestViewModel;
import com.rj.schedulesys.view.model.EmployeeTestViewModel;
import com.rj.schedulesys.view.model.NurseViewModel;
import com.rj.schedulesys.view.model.TestSubCategoryViewModel;
import com.rj.schedulesys.view.model.TestViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeTestService {
	
	private TestDao testDao;
	private EmployeeDao employeeDao;
	private EmployeeTestDao employeeTestDao;
	private TestSubCategoryDao testSubCategoryDao;
	
	private DozerBeanMapper dozerMapper;
	
	@Autowired
	public EmployeeTestService(TestDao testDao, EmployeeDao employeeDao, EmployeeTestDao nurseTestDao,
			TestSubCategoryDao testSubCategoryDao, DozerBeanMapper dozerMapper) {
		this.testDao = testDao;
		this.employeeDao = employeeDao;
		this.employeeTestDao = nurseTestDao;
		this.testSubCategoryDao = testSubCategoryDao;
		this.dozerMapper = dozerMapper;
	}

	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public EmployeeTestViewModel createOrUpate(EmployeeTestViewModel viewModel) {
		log.info("Adding new test for employee with id : {}", viewModel.getEmployeeId());
		Assert.notNull(viewModel, "No employee_test details provided");
		Test test = validateTest(viewModel.getTestId());
		Employee employee = validateEmployee(viewModel.getEmployeeId());
		TestSubCategory testSubCategory = null;
		if(viewModel.getTestSubCategoryId() != null){
			testSubCategory = validateTestSubCategory(viewModel.getTestSubCategoryId());
		}
		EmployeTestPK nurseTestPK = EmployeTestPK.builder()
				.employeeId(viewModel.getEmployeeId())
				.testId(viewModel.getTestId())
				.build();
		
		EmployeeTest nurseTest = employeeTestDao.findOne(nurseTestPK);
		
		if(nurseTest == null){
			nurseTest = EmployeeTest.builder()
					.id(nurseTestPK)
					.employee(employee)
					.test(test)
					.testSubCategory(testSubCategory)
					.build();
		}
		
		if(test.getHasCompletedDate() && StringUtils.equals(viewModel.getStatus(), EmployeeTestStatusConstants.APPLICABLE_STATUS) 
				&& (viewModel.getCompletedDate() == null)){
			log.error("A completion date must be provided");
			throw new RuntimeException("A complete date must be provided");
		}
		if(test.getHasExpirationDate() && StringUtils.equals(viewModel.getStatus(), EmployeeTestStatusConstants.APPLICABLE_STATUS) 
				&& (viewModel.getExpirationDate() == null)){
			log.error("An expiration date must be provided");
			throw new RuntimeException("An expiration date must be provided");
		}
		if(!test.getAllowNotApplicable() 
				&& (StringUtils.equalsIgnoreCase(viewModel.getStatus(), EmployeeTestStatusConstants.NOT_APPLICABLE_STATUS))){
			log.warn("Test : {} does not allow 'NOT APPLICABLE' status, falling back to 'APPLICABLE'", test.getName());
			viewModel.setStatus(EmployeeTestStatusConstants.NOT_APPLICABLE_STATUS);
		}
		Date today = new Date();
		if(viewModel.getCompletedDate() != null && viewModel.getCompletedDate().after(today)){
			log.error("Completed date must be in the past");
			throw new RuntimeException("Completed date must be in the past");
		}
		nurseTest.setStatus(viewModel.getStatus());
		nurseTest.setCompletedDate(viewModel.getCompletedDate());
		nurseTest.setExpirationDate(viewModel.getExpirationDate());
		log.info("Created test : {}", viewModel);
		nurseTest = employeeTestDao.merge(nurseTest);
		return viewModel = dozerMapper.map(nurseTest, EmployeeTestViewModel.class);
	}

	/**
	 * @param nurseTestPK
	 */
	@Transactional
	public void delete(EmployeTestPK nurseTestPK) {
		log.debug("Deleting NurseTest with nurseId : {} and testId: {}", nurseTestPK.getEmployeeId(), nurseTestPK.getTestId());
		EmployeeTest nurseTest = employeeTestDao.findOne(nurseTestPK);
		if(nurseTest == null){
			log.error("No NurseTest found with nurseId : {} and testId : {}"
					, nurseTestPK.getEmployeeId(), nurseTestPK.getTestId());
			throw new RuntimeException("No data found with nurseId : " 
					+ nurseTestPK.getEmployeeId() + " and testId : " + nurseTestPK.getTestId());
		}
		
		employeeTestDao.delete(nurseTest);
	}

	/**
	 * @param employeeId
	 * @return
	 */
	@Transactional
	public List<GetNurseTestViewModel> findAllByEmployee(Long employeeId) {
		log.info("Finding all tests for employee with id : {}", employeeId);
		List<GetNurseTestViewModel> viewModels = new LinkedList<>();
		List<EmployeeTest> employeeTests = employeeTestDao.findAllByNurse(employeeId);
		employeeTests.stream()
		.forEach(employeeTest -> {
			GetNurseTestViewModel viewModel = GetNurseTestViewModel.builder()
					.employee(dozerMapper.map(employeeTest.getEmployee(), NurseViewModel.class))
					.test(dozerMapper.map(employeeTest.getTest(), TestViewModel.class))
					//.testSubCategory(dozerMapper.map(employeeTest.getTestSubCategory(), TestSubCategoryViewModel.class))
					.expirationDate(employeeTest.getExpirationDate())
					.completedDate(employeeTest.getCompletedDate())
					.status(employeeTest.getStatus())
					.build();
			if(employeeTest.getTestSubCategory() != null){
				viewModel.setTestSubCategory(dozerMapper.map(employeeTest.getTestSubCategory(), TestSubCategoryViewModel.class));
			}
			viewModels.add(viewModel);
		});
		log.info("EmployeeTests found : {}", viewModels);
		return viewModels;
	}
	
	/**
	 * @param employeeId
	 * @return
	 */
	public Employee validateEmployee(Long employeeId){
		Employee employee = employeeDao.findOne(employeeId);
		if(employee == null){
			log.error("No employee found with id : {}", employeeId);
			throw new RuntimeException("No employee found with id : " + employeeId);
		}
		return employee;
	}
	
	/**
	 * @param testId
	 * @return
	 */
	public Test validateTest(Long testId){
		Test test = testDao.findOne(testId);
		if(test == null){
			log.error("No test found with id : {}", testId);
			throw new RuntimeException("No test found with id : " + testId);
		}
		
		return test;
	}
	
	/**
	 * @param testSubCategoryId
	 * @return
	 */
	public TestSubCategory validateTestSubCategory(Long testSubCategoryId){
		TestSubCategory testSubCategory = testSubCategoryDao.findOne(testSubCategoryId);
		if(testSubCategory == null){
			log.error("No test sub category found with id : {}", testSubCategoryId);
			throw new RuntimeException("No test sub category found with id : " + testSubCategoryId);
		}
		return testSubCategory;
	}
}