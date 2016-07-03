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

import com.rj.schedulesys.dao.NurseDao;
import com.rj.schedulesys.dao.NurseTestDao;
import com.rj.schedulesys.dao.TestDao;
import com.rj.schedulesys.dao.TestSubCategoryDao;
import com.rj.schedulesys.data.NurseTestStatusConstants;
import com.rj.schedulesys.domain.Nurse;
import com.rj.schedulesys.domain.NurseTest;
import com.rj.schedulesys.domain.NurseTestPK;
import com.rj.schedulesys.domain.Test;
import com.rj.schedulesys.domain.TestSubCategory;
import com.rj.schedulesys.view.model.NurseTestViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NurseTestService {
	
	@Autowired
	private TestDao testDao;
	
	@Autowired
	private NurseDao nurseDao;
	
	@Autowired
	private NurseTestDao nurseTestDao;
	
	@Autowired
	private TestSubCategoryDao testSubCategoryDao;
	
	@Autowired
	private DozerBeanMapper dozerMapper;

	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public NurseTestViewModel createOrUpate(NurseTestViewModel viewModel) {
		
		log.info("Adding new test for urse with id : {}", viewModel.getNurseId());
		
		Assert.notNull(viewModel, "No nurse_test details provided");

		Test test = validateTest(viewModel.getTestId());
		
		Nurse nurse = validateNurse(viewModel.getNurseId());
		
		TestSubCategory testSubCategory = null;
		
		if(viewModel.getTestSubCategoryId() != null){
			testSubCategory = validateTestSubCategory(viewModel.getTestSubCategoryId());
		}
		
		NurseTestPK nurseTestPK = NurseTestPK.builder()
				.nurseId(viewModel.getNurseId())
				.testId(viewModel.getTestId())
				.build();
		
		NurseTest nurseTest = nurseTestDao.findOne(nurseTestPK);
		
		if(nurseTest == null){
			nurseTest = NurseTest.builder()
					.id(nurseTestPK)
					.nurse(nurse)
					.test(test)
					.testSubCategory(testSubCategory)
					.build();
		}
		
		if(test.getHasCompletedDate() && (viewModel.getCompletedDate() == null)){
			log.error("A completion date must be provided");
			throw new RuntimeException("A complete date must be provided");
		}
		
		if(test.getHasExpirationDate() && (viewModel.getExpirationDate() == null)){
			log.error("An expiration date must be provided");
			throw new RuntimeException("An expiration date must be provided");
		}
		
		if(!test.getAllowNotApplicable() 
				&& (StringUtils.equalsIgnoreCase(viewModel.getStatus(), NurseTestStatusConstants.NOT_APPLICABLE_STATUS))){
			log.warn("Test : {} does not allow 'NOT APPLICABLE' status, falling back to 'APPLICABLE'", test.getName());
			viewModel.setStatus(NurseTestStatusConstants.NOT_APPLICABLE_STATUS);
		}
		
		Date today = new Date();
		
		if(viewModel.getCompletedDate() != null && viewModel.getCompletedDate().after(today)){
			log.error("Completed date must be in the past");
			throw new RuntimeException("Completed date must be in the past");
		}
		
		if(viewModel.getExpirationDate() != null && viewModel.getExpirationDate().before(today)){
			log.error("Expiration date must be in the future");
			throw new RuntimeException("Expiration date must be in the future");
		}
		
		nurseTest.setStatus(viewModel.getStatus());
		nurseTest.setCompletedDate(viewModel.getCompletedDate());
		nurseTest.setExpirationDate(viewModel.getExpirationDate());
		
		log.info("Created test : {}", viewModel);
		
		nurseTest = nurseTestDao.merge(nurseTest);
		
		return viewModel = dozerMapper.map(nurseTest, NurseTestViewModel.class);
	}

	/**
	 * @param nurseTestPK
	 */
	@Transactional
	public void delete(NurseTestPK nurseTestPK) {
		
		log.debug("Deleting NurseTest with nurseId : {} and testId: {}", nurseTestPK.getNurseId(), nurseTestPK.getTestId());

		NurseTest nurseTest = nurseTestDao.findOne(nurseTestPK);
		
		if(nurseTest == null){
			log.error("No NurseTest found with nurseId : {} and testId : {}"
					, nurseTestPK.getNurseId(), nurseTestPK.getTestId());
			throw new RuntimeException("No data found with nurseId : " 
					+ nurseTestPK.getNurseId() + " and testId : " + nurseTestPK.getTestId());
		}
		
		nurseTestDao.delete(nurseTest);
	}

	/**
	 * @param nurseId
	 * @return
	 */
	@Transactional
	public List<NurseTestViewModel> findAllByNurse(Long nurseId) {

		log.info("Finding all tests for nurse with id : {}", nurseId);

		List<NurseTestViewModel> viewModels = new LinkedList<>();
		
		List<NurseTest> nurseTests = nurseTestDao.findAllByNurse(nurseId);
		
		for (NurseTest userTest : nurseTests) {
			viewModels.add(dozerMapper.map(userTest, NurseTestViewModel.class));
		}
		
		log.info("NurseTests found : {}", viewModels);
		
		return viewModels;
	}
	
	/**
	 * @param nurseId
	 * @return
	 */
	public Nurse validateNurse(Long nurseId){
		Nurse nurse = nurseDao.findOne(nurseId);
		if(nurse == null){
			log.error("No nurse found with id : {}", nurseId);
			throw new RuntimeException("No nurse found with id : " + nurseId);
		}
		return nurse;
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