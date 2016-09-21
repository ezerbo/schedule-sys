package com.rj.schedulesys.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.dao.TestDao;
import com.rj.schedulesys.domain.Test;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.view.model.GetTestViewModel;
import com.rj.schedulesys.view.model.TestSubCategoryViewModel;
import com.rj.schedulesys.view.model.TestViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestService {
 
	private @Autowired TestDao testDao;
	
	private @Autowired ObjectValidator<TestViewModel> validator;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	
	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public TestViewModel create(TestViewModel viewModel){
		log.debug("Creating a new test : {}", viewModel);
		Test test = testDao.findByName(viewModel.getName());
		if(test != null){
			log.error("A test with name : {} already exists");
			throw new RuntimeException("A test with name : " + viewModel.getName() + " already exists");
		}
		viewModel = this.createOrUpdateTest(viewModel);
		log.debug("Created test : {}", viewModel);
		return viewModel;
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public TestViewModel update(TestViewModel viewModel){
		log.debug("Updating test : {}", viewModel);
		Test test = testDao.findOne(viewModel.getId());
		if(test == null){
			log.error("No test found with id : {}", viewModel.getId());
			throw new RuntimeException("No test found with id : " + viewModel.getId());
		}
		if(!StringUtils.equalsIgnoreCase(test.getName(), viewModel.getName())){
			log.warn("Test name updated, checking its uniqueness");
			if(testDao.findByName(viewModel.getName()) != null){
				log.error("A test with name : {} already exists", viewModel.getName());
				throw new RuntimeException("A test with name : " + viewModel.getName() + " already exists");
			}
		}
		viewModel = this.createOrUpdateTest(viewModel);
		log.debug("Updated test : {}", viewModel);
		return viewModel;
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public TestViewModel createOrUpdateTest(TestViewModel viewModel){
		validator.validate(viewModel);
		Test test = dozerMapper.map(viewModel, Test.class);
		test.setName(StringUtils.upperCase(test.getName()));
		test = testDao.merge(test);
		return dozerMapper.map(test, TestViewModel.class);
	}
	
	
	/**
	 * @param id
	 */
	@Transactional
	public void delete(Long id){
		log.debug("Deleting test with id : {}", id);
		Test test = testDao.findOne(id);
		if(test == null){
			log.debug("No test found with id : {}", id);
			throw new RuntimeException("No test found with id : " + id);
		}
		if(!test.getTestSubCategories().isEmpty()){
			log.error("Test with id : {} can not be deleted, it has sub categories");
			throw new RuntimeException("Test with id : " + id
				+ " can not be deleted, delete all its sub categories first");
		}
		if(!test.getEmployeeTests().isEmpty()){
			log.error("Test with id : {} can not be deleted, it has been taken by nurses", id);
			throw new RuntimeException("Test with id : " + id + " can not be deleted, it has been taken by nurses");
		}
		testDao.delete(test);
		log.debug("Test with id : {} successfully deleted", id);
	}
	
	/**
	 * @param name
	 * @return
	 */
	@Transactional
	public GetTestViewModel findByName(String name){
		log.debug("Fetching test with name : {}", name);
		GetTestViewModel viewModel = null;
		Test test = testDao.findByName(name);
		if(test != null){
			List<TestSubCategoryViewModel> subcategories = new ArrayList<>();
			test.getTestSubCategories().stream()
			.forEach(subcategory -> {
				subcategories.add(dozerMapper.map(subcategory, TestSubCategoryViewModel.class));
			});
			viewModel = new GetTestViewModel(test.getId(), test.getName(), test.getAllowNotApplicable(),
					test.getHasCompletedDate(),test.getHasExpirationDate(), subcategories);
		}
		log.debug("Test found : {}", viewModel);
		return viewModel;
	}

	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public GetTestViewModel findOne(Long id){
		log.info("Fetching test with id : {}", id);
		Test test = testDao.findOne(id);
		GetTestViewModel viewModel = null;
		if(test != null){
			List<TestSubCategoryViewModel> subcategories = new ArrayList<>();
			test.getTestSubCategories().stream()
			.forEach(subcategory -> {
				subcategories.add(dozerMapper.map(subcategory, TestSubCategoryViewModel.class));
			});
			viewModel = new GetTestViewModel(test.getId(), test.getName(), test.getAllowNotApplicable(),
					test.getHasCompletedDate(),test.getHasExpirationDate(), subcategories);
		}
		return viewModel;
	}
	
	/**
	 * @return
	 */
	@Transactional
	public List<GetTestViewModel> findAll(){
		log.info("Fetching all tests");
		List<Test> tests = testDao.findAll();
		List<GetTestViewModel> viewModels = new LinkedList<>();
		tests.stream()
		.forEach(test -> {
			List<TestSubCategoryViewModel> subcategories = new ArrayList<>();
			test.getTestSubCategories().stream()
			.forEach(subcategory -> {
				subcategories.add(dozerMapper.map(subcategory, TestSubCategoryViewModel.class));
			});
			GetTestViewModel viewModel = new GetTestViewModel(test.getId(), test.getName(), test.getAllowNotApplicable(),
					test.getHasCompletedDate(),test.getHasExpirationDate(), subcategories);
			viewModels.add(viewModel);
		});
		
		return viewModels;
	}
	
}