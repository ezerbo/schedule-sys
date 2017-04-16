package com.ss.schedulesys.service;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.Employee;
import com.ss.schedulesys.domain.PhoneNumber;
import com.ss.schedulesys.domain.enumeration.PhoneNumberLabel;
import com.ss.schedulesys.domain.enumeration.PhoneNumberType;
import com.ss.schedulesys.repository.EmployeeRepository;
import com.ss.schedulesys.repository.PhoneNumberRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

/**
 * Service Implementation for managing PhoneNumber.
 */
@Service
@Transactional
public class PhoneNumberService {

    private final Logger log = LoggerFactory.getLogger(PhoneNumberService.class);
    
    private final static int phoneNumbersPerEmployeeLimit = 3;
    
    private EmployeeRepository employeeRepository;
    private PhoneNumberRepository phoneNumberRepository;


    @Autowired
    public PhoneNumberService(PhoneNumberRepository phoneNumberRepository,
    		EmployeeRepository employeeRepository) {
    	this.employeeRepository = employeeRepository;
    	this.phoneNumberRepository = phoneNumberRepository;
	}
    
    /**
     * Save a phoneNumber.
     *
     * @param phoneNumber the entity to save
     * @return the persisted entity
     */
    public PhoneNumber create(PhoneNumber phoneNumber) {
        log.debug("Request to save PhoneNumber : {}", phoneNumber);
        Employee employee = Optional.ofNullable(phoneNumber.getEmployee())
        		.map(result -> employeeRepository.findOne(result.getId()))
        		.orElseThrow(()-> new ScheduleSysException("An employee is required for a phone number creation"));
        
        if(phoneNumberRepository.findAllByEmployee(employee.getId()).size() == phoneNumbersPerEmployeeLimit){
        	log.error("Could not create new phone number, employee : {} has already 03 phone numbers", employee);
        	throw new ScheduleSysException("Could not create new phone number, employee has already 03 phone numbers");
        }
        //TODO Prevent users from creating duplicate phone_number_labels
        PhoneNumberType.validate(phoneNumber.getType());
        PhoneNumberLabel.validate(phoneNumber.getLabel());
        if(phoneNumberRepository.findByNumber(phoneNumber.getNumber()) != null){
        	log.error("Phone number : {} is already taken", phoneNumber.getNumber());
        	throw new ScheduleSysException(String.format("Phone number '%s' is already in use", phoneNumber.getNumber()));
        }
        
        if(phoneNumberRepository.findByEmployeeAndLabel(employee.getId(), phoneNumber.getLabel()) != null){
        	log.error("Could not create new phone number, employee already has a number with label : {}", phoneNumber.getLabel());
        	throw new ScheduleSysException(
        			String.format("Could not create new phone number, employee already has a number with label : %s", phoneNumber.getLabel()));
        }
        phoneNumber.setEmployee(employee);
        PhoneNumber result = phoneNumberRepository.save(phoneNumber);
        return result;
    }

    /**
     *  Get all the phoneNumbers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<PhoneNumber> findAll(Pageable pageable) {
        log.debug("Request to get all PhoneNumbers");
        Page<PhoneNumber> result = phoneNumberRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one phoneNumber by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PhoneNumber findOne(Long id) {
        log.debug("Request to get PhoneNumber : {}", id);
        PhoneNumber phoneNumber = phoneNumberRepository.findOne(id);
        return phoneNumber;
    }
    
    @Transactional(readOnly = true)
    public PhoneNumber findByNumber(String number){
    	log.debug("Request to get PhoneNumber using number : {}", number);
    	PhoneNumber phoneNumber = phoneNumberRepository.findByNumber(number);
    	return phoneNumber;
    }
    
    /**
     * 	Get all the phoneNumbers for an employee
     * @param employeeId
     * @return phone numbers
     */
    @Transactional(readOnly = true)
    public List<PhoneNumber> findAllByEmployee(Long employeeId){
    	log.debug("Request to get all PhoneNumbers for employee : {}", employeeId);
    	List<PhoneNumber> phoneNumbers = phoneNumberRepository.findAllByEmployee(employeeId);
    	return phoneNumbers;
    }

    /**
     *  Delete the  phoneNumber by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete PhoneNumber : {}", id);
        phoneNumberRepository.delete(id);
    }

//    /**
//     * Search for the phoneNumber corresponding to the query.
//     *
//     *  @param query the query of the search
//     *  @return the list of entities
//     */
//    @Transactional(readOnly = true)
//    public Page<PhoneNumber> search(String query, Pageable pageable) {
//        log.debug("Request to search for a page of PhoneNumbers for query {}", query);
//        Page<PhoneNumber> result = phoneNumberSearchRepository.search(queryStringQuery(query), pageable);
//        return result;
//    }
}