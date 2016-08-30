package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.schedulesys.dao.TestDao;
import com.rj.schedulesys.dao.TestSubCategoryDao;
import com.rj.schedulesys.domain.Test;
import com.rj.schedulesys.domain.TestSubCategory;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.view.model.TestSubCategoryViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class TestSubCategoryService {

	private @Autowired TestDao testDao;
	private @Autowired TestSubCategoryDao testSubCategoryDao;
	
	private @Autowired ObjectValidator<TestSubCategoryViewModel> validator;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	
	public TestSubCategoryViewModel create(TestSubCategoryViewModel viewModel){
		Assert.notNull(viewModel, "No test sub category provided");
		log.debug("Creating new sub category for test with name : {}", viewModel.getName());
		TestSubCategory testSubCategory = testSubCategoryDao.findByNameAndTestId(
				viewModel.getName(), viewModel.getTestId());
		if(testSubCategory != null){
			log.error("A sub category with name : {} already exists for test with id : {}"
					, viewModel.getName(), viewModel.getTestId());
			throw new RuntimeException("A sub category with name : " + viewModel.getName() 
				+ " already exists for test with id: " + viewModel.getTestId());
		}
		viewModel = this.createOrUpdate(viewModel);
		log.debug("Created sub category : {}", viewModel);
		return viewModel;
	}
	
	public TestSubCategoryViewModel update(TestSubCategoryViewModel viewModel){
		Assert.notNull(viewModel, "No test sub category provided");
		log.debug("Updating test sub category : {}", viewModel);
		if(testSubCategoryDao.findOne(viewModel.getId()) == null){
			log.error("No test sub category found with id : {}", viewModel.getId());
			throw new RuntimeException("No test sub category found with id : " + viewModel.getId());
		}
		TestSubCategory testSubCategory = testSubCategoryDao.findOne(viewModel.getId());
		if(!StringUtils.equalsIgnoreCase(testSubCategory.getName(), viewModel.getName())){
			log.warn("Test sub category name updated, checking its uniqueness");
			if(testSubCategoryDao.findByNameAndTestId(viewModel.getName(), viewModel.getTestId()) != null){
				log.error("A sub category with name : {} already exists for test with id : {}"
						, viewModel.getName(), viewModel.getTestId());
				
				throw new RuntimeException("A sub category with name : " + viewModel.getName() 
				+ " already exists for test with id : " + viewModel.getTestId());
			}
		}
		viewModel = this.createOrUpdate(viewModel);
		log.debug("Updated test sub category : {}", viewModel.getName());
		return viewModel;
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	public TestSubCategoryViewModel createOrUpdate(TestSubCategoryViewModel viewModel){
		validator.validate(viewModel);
		Test test = testDao.findOne(viewModel.getTestId());
		if(test == null){
			log.error("No test found with id : {}", viewModel.getTestId());
			throw new RuntimeException("No test found with name : " + viewModel.getTestId());
		}
		TestSubCategory testSubCategory = dozerMapper.map(viewModel, TestSubCategory.class);
		testSubCategory.setTest(test);
		testSubCategory = testSubCategoryDao.merge(testSubCategory);
		return dozerMapper.map(testSubCategory, TestSubCategoryViewModel.class);
	}
	
	/**
	 * @param id
	 */
	public void delete(Long id){
		log.debug("Deleting test sub category with id : {}", id);
		TestSubCategory testSubCategory = testSubCategoryDao.findOne(id);
		if(testSubCategory == null){
			log.error("No test sub category found with id : {}", id);
			throw new RuntimeException("No test sub category found with id : " + id);
		}
		if(!testSubCategory.getNurseTests().isEmpty()){
			log.error("Test sub category with id : {} can not be deleted, it has been taken by nurses", id);
			throw new RuntimeException("Test sub category with id : " + id + " can not be deleted, it has been taken by nurses");
		}
		testSubCategoryDao.delete(testSubCategory);
		log.debug("Test sub category with id successfully deleted ");
	}
	
	/**
	 * @param name
	 * @return
	 */
	public TestSubCategoryViewModel findByName(String name){
		log.debug("Fetching test sub category with name : {}", name);
		TestSubCategory testSubCategory = testSubCategoryDao.findByName(name);
		TestSubCategoryViewModel viewModel = null;
		if(testSubCategory != null){
			viewModel = dozerMapper.map(testSubCategory, TestSubCategoryViewModel.class);
			viewModel.setTestId(testSubCategory.getTest().getId());
		}
		log.debug("Test sub category found : {}", viewModel);
		return viewModel;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public TestSubCategoryViewModel findOne(Long id){
		log.debug("Fetching test sub category with id : {}", id);
		TestSubCategory testSubCategory = testSubCategoryDao.findOne(id);
		TestSubCategoryViewModel viewModel = null;
		if(testSubCategory == null){
			log.warn("No test sub category found with id : {}", id);
		}else{
			viewModel = dozerMapper.map(testSubCategory, TestSubCategoryViewModel.class);
			viewModel.setTestId(testSubCategory.getTest().getId());
		}
		return viewModel;
	}
	
	/**
	 * @param testId
	 * @return
	 */
	public List<TestSubCategoryViewModel> findAllByTest(Long testId){
		log.debug("Fetching all sub categories for test with id : {}", testId);
		if(testDao.findOne(testId) == null){
			log.debug("No test found with id : {}", testId);
			throw new RuntimeException("No test found with id : " + testId);
		}
		List<TestSubCategory> testSubCategories = testSubCategoryDao.findAllByTest(testId);
		List<TestSubCategoryViewModel> viewModels = new LinkedList<>();
		for(TestSubCategory testSubCategory : testSubCategories){
			TestSubCategoryViewModel viewModel = dozerMapper.map(
					testSubCategory, TestSubCategoryViewModel.class);
			viewModel.setTestId(testSubCategory.getTest().getId());
			viewModels.add(viewModel);
		}
		return viewModels;
	}
}