package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.sys.dao.TestDao;
import com.rj.sys.dao.UserDao;
import com.rj.sys.domain.Test;
import com.rj.sys.view.model.TestViewModel;

@Slf4j
@Service
public class TestService {
 
	private @Autowired TestDao testDao;
	private @Autowired UserDao userDao;
	private @Autowired DozerBeanMapper dozerMapper;
	
	@Transactional
	public TestViewModel createOrUpdateTest(TestViewModel viewModel){
		log.info("Creating test with name : {}", viewModel.getTestName());
		Test test = dozerMapper.map(viewModel, Test.class);
		test = testDao.merge(test);
		return dozerMapper.map(test, TestViewModel.class);
	}
	
	@Transactional
	public void deleteTest(Long testId){
		log.info("Deleting test with id : {}", testId);
		Test test = testDao.findOne(testId);
		if(test.getTestTypes().isEmpty()){
			testDao.delete(test);
		}else{
			log.info("Test wiht id : {} cannot be deleted because it still has types");
			throw new RuntimeException("Test with id : " + testId + " cannot be deleted");
		}
	}
	
	@Transactional
	public boolean hasTypes(Long id){
		Test test = testDao.findOne(id);
		Assert.notNull(test, "No test found with id : " + id);
		return !test.getTestTypes().isEmpty();
	}
	
	@Transactional
	public TestViewModel findByName(String name){
		log.info("Finding test by name : {}", name);
		TestViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(testDao.findByName(name), TestViewModel.class);
		}catch(NoResultException nre){
			log.info("No test found with name : {}", name);
		}
		return viewModel;
	}

	@Transactional
	public TestViewModel findById(Long id){
		log.info("Finding test by id : {}", id);
		Test test = testDao.findOne(id);
		TestViewModel viewModel = null;
		if(test != null){
			viewModel = dozerMapper.map(test, TestViewModel.class);
		}
		return viewModel;
	}
	
	@Transactional
	public List<TestViewModel> findAll(){
		log.info("Finding all tests");
		List<Test> tests = testDao.findAll();
		List<TestViewModel> viewModels = new LinkedList<>();
		for(Test test : tests){
			viewModels.add(dozerMapper.map(test, TestViewModel.class));
		}
		return viewModels;
	}
}