package com.ss.schedulesys.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.Employee;
import com.ss.schedulesys.domain.License;
import com.ss.schedulesys.domain.LicenseType;
import com.ss.schedulesys.repository.EmployeeRepository;
import com.ss.schedulesys.repository.LicenseRepository;
import com.ss.schedulesys.repository.LicenseTypeRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation for managing License.
 */
@Slf4j
@Service
@Transactional
public class LicenseService {

    private LicenseRepository licenseRepository;
    private EmployeeRepository employeeRepository;
    private LicenseTypeRepository licenseTypeRepository;
    
    @Autowired
    public LicenseService(LicenseRepository licenseRepository,
    		EmployeeRepository employeeRepository, LicenseTypeRepository licenseTypeRepository) {
    	this.licenseRepository = licenseRepository;
    	this.employeeRepository = employeeRepository;
    	this.licenseTypeRepository = licenseTypeRepository;
	}

    /**
     * Save a license.
     *
     * @param license the entity to save
     * @return the persisted entity
     */
    public License save(License license) {
        log.debug("Request to save License : {}", license);
        Employee employee = Optional.ofNullable(license.getEmployee())
        		.map(result -> employeeRepository.findOne(result.getId()))
        		.orElseThrow(() -> new ScheduleSysException("A valid employee is required to create a new license"));
        
        LicenseType licenseType = Optional.ofNullable(license.getLicenseType())
        		.map(result -> licenseTypeRepository.findByName(result.getName()))
        		.orElseThrow(() -> new ScheduleSysException("A valid license type is required to create a new license"));
        
        License byNumberAndType = licenseRepository.findByNumberAndType(license.getNumber(), licenseType.getName());
        if(byNumberAndType != null && byNumberAndType.getId() != license.getId()){
        	throw new ScheduleSysException(String.format("License number %s is already in use", license.getNumber()));
        }
        license.setEmployee(employee);
        license.setLicenseType(licenseType);
        License result = licenseRepository.save(license);
        return result;
    }

    /**
     *  Get all the licenses.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<License> findAll(Pageable pageable) {
        log.debug("Request to get all Licenses");
        Page<License> result = licenseRepository.findAll(pageable);
        return result;
    }
    
    /**
     * @param employeeId
     * @return list of licenses for employee with given ID
     */
    @Transactional(readOnly = true)
    public List<License> findAllByEmployee(Long employeeId){
    	log.debug("Request to get all Licenses for employee with id : {}", employeeId);
    	List<License> licenses = licenseRepository.findByEmployeeId(employeeId);
    	return licenses;
    }

    /**
     *  Get one license by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public License findOne(Long id) {
        log.debug("Request to get License : {}", id);
        License license = licenseRepository.findOne(id);
        return license;
    }

    /**
     *  Delete the  license by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete License : {}", id);
        licenseRepository.delete(id);
    }

}