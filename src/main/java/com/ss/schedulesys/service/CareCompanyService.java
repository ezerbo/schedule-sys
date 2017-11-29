package com.ss.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.CareCompany;
import com.ss.schedulesys.domain.CareCompanyType;
import com.ss.schedulesys.domain.InsuranceCompany;
import com.ss.schedulesys.domain.SearchCriteria;
import com.ss.schedulesys.domain.specification.CareCompanySpecification;
import com.ss.schedulesys.repository.CareCompanyRepository;
import com.ss.schedulesys.repository.CareCompanyTypeRepository;
import com.ss.schedulesys.repository.InsuranceCompanyRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;
import com.ss.schedulesys.web.vm.CareCompanyFilterModel;

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
    private InsuranceCompanyRepository insuranceCompanyRepository;

    public CareCompanyService(CareCompanyRepository careCompanyRepository,
    		CareCompanyTypeRepository careCompanyTypeRepository, InsuranceCompanyRepository insuranceCompanyRepository) {
    	this.careCompanyRepository = careCompanyRepository;
    	this.careCompanyTypeRepository = careCompanyTypeRepository;
    	this.insuranceCompanyRepository = insuranceCompanyRepository;
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
        InsuranceCompany insuranceCompany = null;
        if(careCompany.getInsuranceCompany() != null) {
        	String companyName = careCompany.getInsuranceCompany().getName();
        	insuranceCompany = insuranceCompanyRepository.findByName(companyName);
        	if(insuranceCompany == null) {
        		log.warn("No such insurance company : {}, no insurance company will be set", companyName);
        	}
        }
        careCompany.careCompanyType(careCompanyType).insuranceCompany(insuranceCompany);
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
    public Page<CareCompany> findAll(CareCompanyFilterModel filter, Pageable pageable) {
        log.debug("Request to get all CareCompanies");
		List<SearchCriteria> criterias = new LinkedList<>();
		if(filter.getName() != null)
			criterias.add(new SearchCriteria("name", ":", filter.getName()));
		if(filter.getTypeName() != null)
			criterias.add(new SearchCriteria("careCompanyType", ":", filter.getTypeName()));
		if(filter.getInsuranceName() != null)
			criterias.add(new SearchCriteria("insuranceCompany", ":", filter.getInsuranceName()));
		
		Page<CareCompany> careCompanies = null;
		if(criterias.isEmpty()){
			careCompanies = careCompanyRepository.findAll(pageable);
		}else{
			Specifications<CareCompany> specifications = null;
			for (int i = 0; i < criterias.size(); i++) {
				if(i == 0) {
					specifications = Specifications.where(new CareCompanySpecification(criterias.get(0)));
				} else {
					specifications.and(new CareCompanySpecification(criterias.get(i)));
				}
			}
			careCompanies = careCompanyRepository.findAll(specifications, pageable);
		}
		
		return careCompanies;
       
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