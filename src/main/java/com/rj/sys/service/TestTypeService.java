package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.NoResultException;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.TestDao;
import com.rj.sys.dao.TestTypeDao;
import com.rj.sys.domain.Test;
import com.rj.sys.domain.TestType;
import com.rj.sys.view.model.TestTypeViewModel;

@Slf4j
@Service
public class TestTypeService {

	private @Autowired TestDao testDao;
	private @Autowired TestTypeDao testTypeDao;
	
	private @Autowired DozerBeanMapper dozerMapper;
	
	@Transactional
	public TestTypeViewModel createOrUpdate(TestTypeViewModel viewModel, Long testId){
		log.info("Creating or updating test type : {}", viewModel);
		
		if(isTestTypeExistant(viewModel.getTypeName(), testId)){
			log.info("There is already a test with name : {}", viewModel.getTypeName());
			throw new RuntimeException("There is already a test type with name : " + viewModel.getTypeName() + " ");
		}
		
		Test test = testDao.findOne(testId);
		if(test == null){
			log.info("No test found with Id : {}", testId);
			throw new RuntimeException("No test found with id : " + testId);
		}
		
		TestType testType = dozerMapper.map(viewModel, TestType.class);
		testType.setTest(test);
		
		testType = testTypeDao.merge(testType);
		
		viewModel = dozerMapper.map(testType, TestTypeViewModel.class);
		log.info("created test type : {}", viewModel);
		return viewModel;
	}
	
	@Transactional
	public List<TestTypeViewModel> findAll(){
		log.info("Finding all test types");
		List<TestType> testTypes = testTypeDao.findAll();
		List<TestTypeViewModel> viewModels = new LinkedList<>();
		for(TestType testType : testTypes){
			viewModels.add(dozerMapper.map(testType, TestTypeViewModel.class));
		}
		log.info("Test types found : {}", viewModels);
		return viewModels;
	}
	
	@Transactional
	public TestTypeViewModel findById(Long id){
		log.info("Finding test type by id : {}", id);
		TestType testType = testTypeDao.findOne(id);
		TestTypeViewModel viewModel = null;
		if(testType != null){
			viewModel = dozerMapper.map(testType, TestTypeViewModel.class);
		}
		log.info("Test type found : {}", viewModel);
		return viewModel;
	}
	
	@Transactional
	public TestTypeViewModel findByName(String name){
		log.info("Finding test type by name : {}", name);
		TestTypeViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(testTypeDao.findByName(name), TestTypeViewModel.class);
		}catch(NoResultException e){
			log.info("No test type found with name : {}", name);
		}
		log.info("Test type found : {}", viewModel);
		return viewModel;
	}
	
	@Transactional
	public void delete(Long id){
		log.info("Deleting test type with id : {}", id);
		TestType testType = testTypeDao.findOne(id);
		
		if(testType == null){
			log.info("No test type found with id : {}", id);
			throw new RuntimeException("No test found with id : " + id);
		}
		
		testTypeDao.delete(testType);
	}
	
	@Transactional
	public List<TestTypeViewModel> findAllByTestId(Long testId){
		log.info("Finding all types for test with id : {}", testId);
		List<TestType> testTypes = testTypeDao.findAllByTestId(testId);
		List<TestTypeViewModel> viewModels = new LinkedList<>();
		for(TestType testType : testTypes){
			viewModels.add(dozerMapper.map(testType, TestTypeViewModel.class));
		}
		return viewModels;
	}
	
	@Transactional
	public TestTypeViewModel findByIdAndTestId(Long id, Long testId){
		TestTypeViewModel viewModel = null;
		
		try{
			viewModel = dozerMapper.map(
					testTypeDao.findByIdAndTestId(id, testId), TestTypeViewModel.class
					);
		}catch(Exception e){
			log.info("No type with id : {} found for test with id : {}", id, testId);
		}
		
		return viewModel;
	}
	
	@Transactional
	public boolean isTestTypeExistant(String testTypeName, Long testId){
		try{
			TestType testType = testTypeDao.findByNameAndTestId(testTypeName, testId);
			log.info("test type : {} found, name : {}, test id : {}", testType, testTypeName, testId);
			return true;
		}catch(Exception e){
			log.info("No test type found with name : {} and test id : {}", testTypeName, testId);
		}
		return false;
	}
	
}