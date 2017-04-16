package com.ss.schedulesys.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.Test;
import com.ss.schedulesys.domain.TestSubcategory;
import com.ss.schedulesys.repository.TestRepository;
import com.ss.schedulesys.repository.TestSubcategoryRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation for managing TestSubCategory.
 */
@Slf4j
@Service
@Transactional
public class TestSubCategoryService {

	private TestRepository testRepository;
    private TestSubcategoryRepository testSubcategoryRepository;
    
    @Autowired
    public TestSubCategoryService(TestSubcategoryRepository testSubcategoryRepository,
    		TestRepository testRepository) {
    	this.testRepository = testRepository;
    	this.testSubcategoryRepository = testSubcategoryRepository;
	}

    /**
     * Save a testSubCategory.
     *
     * @param testSubCategory the entity to save
     * @return the persisted entity
     */
    public TestSubcategory save(TestSubcategory testSubCategory) {
        log.debug("Request to save TestSubCategory : {}", testSubCategory);
        
        Test test = Optional.ofNullable(testSubCategory.getTest())
        		.map(result -> testRepository.findOne(result.getId()))
        		.orElseThrow(() -> new ScheduleSysException("A valid test is required to create a test subcategory"));
        
        if(testSubcategoryRepository.findByName(testSubCategory.getName()) != null){
        	throw new ScheduleSysException(
        			String.format("Test subcategory name '%s' is already in use", testSubCategory.getName()));
        }
        
        testSubCategory.setTest(test);
        TestSubcategory result = testSubcategoryRepository.save(testSubCategory);
        return result;
    }

    /**
     *  Get all the testSubCategories.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<TestSubcategory> findAll(Pageable pageable) {
        log.debug("Request to get all TestSubCategories");
        Page<TestSubcategory> result = testSubcategoryRepository.findAll(pageable);
        return result;
    }
    
    @Transactional(readOnly = true) 
    public List<TestSubcategory> findAllByTest(Long testId) {
        log.debug("Request to get all TestSubCategories");
        List<TestSubcategory> result = testSubcategoryRepository.findAllByTest(testId);
        return result;
    }

    /**
     *  Get one testSubCategory by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TestSubcategory findOne(Long id) {
        log.debug("Request to get TestSubCategory : {}", id);
        TestSubcategory testSubCategory = testSubcategoryRepository.findOne(id);
        return testSubCategory;
    }

    /**
     *  Delete the  testSubCategory by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TestSubCategory : {}", id);
        if(!testSubcategoryRepository.findOne(id).getTestOccurrences().isEmpty()){
        	throw new ScheduleSysException("Could not delete Test subcategory, it has been taken by employees");
        }
        testSubcategoryRepository.delete(id);
    }

}