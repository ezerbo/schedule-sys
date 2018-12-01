package com.ss.schedulesys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.Test;
import com.ss.schedulesys.repository.TestRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation for managing Test.
 */
@Slf4j
@Service
@Transactional
public class TestService {

    
    private TestRepository testRepository;

    @Autowired
    public TestService(TestRepository testRepository) {
    	this.testRepository = testRepository;
	}

    /**
     * Save a test.
     *
     * @param test the entity to save
     * @return the persisted entity
     */
    public Test save(Test test) {
        log.debug("Request to save Test : {}", test);
        Test byNameIgnoreCase = testRepository.findByNameIgnoreCase(test.getName());
        if(byNameIgnoreCase != null && byNameIgnoreCase.getId() != test.getId()){
        	throw new ScheduleSysException(String.format("Test name '%s' is already in use", test.getName()));
        }
        Test result = testRepository.save(test);
        return result;
    }

    /**
     *  Get all the tests.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Test> findAll(Pageable pageable) {
        log.debug("Request to get all Tests");
        Page<Test> result = testRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one test by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Test findOne(Long id) {
        log.debug("Request to get Test : {}", id);
        Test test = testRepository.findOne(id);
        return test;
    }
    
    /**
     *  Get one test by name.
     *
     *  @param name the name of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Test findOne(String name) {
        log.debug("Request to get Test : {}", name);
        Test test = testRepository.findByNameIgnoreCase(name);
        return test;
    }

    /**
     *  Delete the  test by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Test : {}", id);
        if(!testRepository.findOne(id).getTestOccurrences().isEmpty()){
        	throw new ScheduleSysException("Could not delete test, it has already been taken by employees");
        }
        testRepository.delete(id);
    }
    
    @Transactional(readOnly = true) 
    public List<Test> search(String query) {
        log.debug("Request to get Test : {}", query);
        return testRepository.searchByName(query);
    }

}