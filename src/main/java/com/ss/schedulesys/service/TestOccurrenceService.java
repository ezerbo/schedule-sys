package com.ss.schedulesys.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.Employee;
import com.ss.schedulesys.domain.Test;
import com.ss.schedulesys.domain.TestOccurrence;
import com.ss.schedulesys.domain.TestSubcategory;
import com.ss.schedulesys.repository.EmployeeRepository;
import com.ss.schedulesys.repository.TestOccurrenceRepository;
import com.ss.schedulesys.repository.TestRepository;
import com.ss.schedulesys.repository.TestSubcategoryRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author ezerbo
 * Service Implementation for managing TestOccurrence.
 */
@Slf4j
@Service
@Transactional
public class TestOccurrenceService {

    
	private TestRepository testRepository;
	private EmployeeRepository employeeRepository;
	private TestSubcategoryRepository testSubcategoryRepository;
    private TestOccurrenceRepository testOcurrenceRepository;
    
    @Autowired
    public TestOccurrenceService(TestOccurrenceRepository testOcurrenceRepository,
    		EmployeeRepository employeeRepository, TestRepository testRepository,
    		TestSubcategoryRepository testSubcategoryRepository) {
		this.testOcurrenceRepository = testOcurrenceRepository;
		this.testRepository = testRepository;
		this.employeeRepository = employeeRepository;
		this.testSubcategoryRepository = testSubcategoryRepository;
	}

    /**
     * Save a testOcurrence.
     *
     * @param testOcurrence the entity to save
     * @return the persisted entity
     */
    public TestOccurrence save(TestOccurrence testOcurrence) {
        log.debug("Request to save TestOccurrence : {}", testOcurrence);
        //TODO Should each employee have at most one test occurrence per test?
        //In which case, record of past tests occurrence are not kept.
        Employee employee = Optional.ofNullable(testOcurrence.getEmployee())
        		.map(result -> employeeRepository.findOne(result.getId()))
        		.orElseThrow(() -> new ScheduleSysException("A valid employee is required to add a test"));
        
        Test test = Optional.ofNullable(testOcurrence.getTest())
        		.map(result -> testRepository.findOne(result.getId()))
        		.orElseThrow(() -> new ScheduleSysException("A valid test is required to add a test"));
        
        TestSubcategory testSubcategory = Optional.ofNullable(testOcurrence.getTestSubcategory())
        		.map(result -> testSubcategoryRepository.findOne(result.getId()))
        		.orElse(null);
        
        //The current test can have a completion date, is applicable but no completion date provided
    	if(test.isHasCompletionDate() && testOcurrence.isApplicable() 
				&& (testOcurrence.getCompletionDate() == null)){
			throw new ScheduleSysException("A completion date is required");
		}
    	
    	//The current test has expiration date, is applicable but no expiration date provided
    	if(test.isHasExpiryDate() && testOcurrence.isApplicable()
				&& (testOcurrence.getExpiryDate() == null)){
			throw new ScheduleSysException("An expiration date is required");
		}
    	
    	//Test completion date must be in the past
    	if(testOcurrence.getCompletionDate() != null && testOcurrence.getCompletionDate().after(new Date())){
			log.error("Completed date must be in the past");
			throw new ScheduleSysException("Test Completion date must be in the past");
		}
    	//TODO expiry date must be after completed date
    	if(testOcurrenceRepository.findDuplicate(test.getId(), employee.getId(),
    			testOcurrence.getCompletionDate(), testOcurrence.getExpiryDate()) != null){
    		log.error("Duplicate record detected for : {}", testOcurrence);
    		throw new ScheduleSysException(String.format("%s as already taken test '%s' on %s and expiring on %s",
    				employee.getFirstName().concat(" ").concat(employee.getLastName()), test.getName(),
    				testOcurrence.getCompletionDate(), testOcurrence.getExpiryDate()));
    	}
    	
    	//Current test is not applicable to current employee, the no expiry and no completion date 
    	if(!testOcurrence.isApplicable()){
    		testOcurrence.expiryDate(null).completionDate(null);
    	}
    	
        testOcurrence.employee(employee).test(test).testSubcategory(testSubcategory);
        TestOccurrence result = testOcurrenceRepository.save(testOcurrence);
        return result;
    }

    /**
     *  Get all the testOcurrences.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<TestOccurrence> findAllByEmployee(Long employeeId) {
        log.debug("Request to get all TestOccurrences");
        List<TestOccurrence> result = testOcurrenceRepository.findAllByEmployee(employeeId);
        return result;
    }

    /**
     *  Get one testOcurrence by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public TestOccurrence findOne(Long id) {
        log.debug("Request to get TestOccurrence : {}", id);
        TestOccurrence testOcurrence = testOcurrenceRepository.findOne(id);
        return testOcurrence;
    }
    
    /**
     *  Get testOccurrences page using page metadata 
     * @param pageable page metadata
     * @return page requested
     */
    @Transactional(readOnly = true)
    public Page<TestOccurrence> findAll(Pageable pageable){
    	log.debug("Request to get all TestOccurrences");
    	Page<TestOccurrence> testOccurrences = testOcurrenceRepository.findAll(pageable);
    	return testOccurrences;
    }

    /**
     *  Delete the  testOcurrence by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete TestOccurrence : {}", id);
        testOcurrenceRepository.delete(id);
    }

}