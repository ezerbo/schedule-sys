package com.ss.schedulesys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.CareCompany;
import com.ss.schedulesys.domain.CareCompanyType;
import com.ss.schedulesys.repository.CareCompanyRepository;
import com.ss.schedulesys.repository.CareCompanyTypeRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation for managing CareCompany.
 */
@Slf4j
@Service
@Transactional
public class CareCompanyService {

    private CareCompanyRepository careCompanyRepository;
    private CareCompanyTypeRepository careCompanyTypeRepository;

    @Autowired
    public CareCompanyService(CareCompanyRepository careCompanyRepository,
    		CareCompanyTypeRepository careCompanyTypeRepository) {
    	this.careCompanyRepository = careCompanyRepository;
    	this.careCompanyTypeRepository = careCompanyTypeRepository;
    }
    
    /**
     * Save a careCompany.
     *
     * @param careCompany the entity to save
     * @return the persisted entity
     */
    public CareCompany save(CareCompany careCompany) {
        log.debug("Request to save CareCompany : {}", careCompany);
        CareCompany oldCareCompany = careCompanyRepository.findByNameIgnoreCase(careCompany.getName());
        if(oldCareCompany != null && (oldCareCompany.getId() != careCompany.getId())){
        	log.error("Care company name : {} is already is use", careCompany.getName());
        	throw new ScheduleSysException(String.format("Care company name %s is already in use", careCompany.getName()));
        }
        CareCompanyType careCompanyType =  careCompanyTypeRepository.findOneByName(careCompany.getCareCompanyType().getName()) ;
        if(careCompanyType == null){
        	log.error("No Care Company type provided");
        	throw new ScheduleSysException("A company type is required");
        }
        log.info("Care company type : {}", careCompanyType);
        careCompany.setCareCompanyType(careCompanyType);
        CareCompany result = careCompanyRepository.save(careCompany);
        return result;
    }

    /**
     *  Get all the careCompanies.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CareCompany> findAll(Pageable pageable) {
        log.debug("Request to get all CareCompanies");
        Page<CareCompany> result = careCompanyRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one careCompany by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CareCompany findOne(Long id) {
        log.debug("Request to get CareCompany : {}", id);
        CareCompany careCompany = careCompanyRepository.findOne(id);
        return careCompany;
    }
    
    /**
     *  Get one careCompany by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CareCompany findOneByName(String name) {
        log.debug("Request to get CareCompany : {}", name);
        CareCompany careCompany = careCompanyRepository.findByNameIgnoreCase(name);
        return careCompany;
    }

    /**
     *  Delete the  careCompany by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CareCompany : {}", id);
        CareCompany careCompany = careCompanyRepository.getOne(id);
        if(careCompany == null)
        	throw new ScheduleSysException(String.format("No care company found with id : ", id));
        if(careCompany.getCompanyContacts().isEmpty() && careCompany.getSchedules().isEmpty())
        	careCompanyRepository.delete(id);
        else
        	throw new ScheduleSysException(
        			String.format("Please remove all contacts and schedules for '%s' before deleting", careCompany.getName()));
    }
    
}