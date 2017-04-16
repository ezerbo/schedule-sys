package com.ss.schedulesys.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.CareCompanyType;
import com.ss.schedulesys.repository.CareCompanyTypeRepository;

/**
 * Service Implementation for managing CareCompanyType.
 */
@Service
@Transactional
public class CareCompanyTypeService {

    private final Logger log = LoggerFactory.getLogger(CareCompanyTypeService.class);
    
    private CareCompanyTypeRepository careCompanyTypeRepository;
    
    @Autowired
    public CareCompanyTypeService(CareCompanyTypeRepository careCompanyTypeRepository) {
		this.careCompanyTypeRepository = careCompanyTypeRepository;
	}

    /**
     * Save a careCompanyType.
     *
     * @param careCompanyType the entity to save
     * @return the persisted entity
     */
    public CareCompanyType save(CareCompanyType careCompanyType) {
        log.debug("Request to save CareCompanyType : {}", careCompanyType);
        CareCompanyType result = careCompanyTypeRepository.save(careCompanyType);
        return result;
    }

    /**
     *  Get all the careCompanyTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CareCompanyType> findAll(Pageable pageable) {
        log.debug("Request to get all CareCompanyTypes");
        Page<CareCompanyType> result = careCompanyTypeRepository.findAll(pageable);
        return result;
    }
    
    @Transactional(readOnly = true) 
    public List<CareCompanyType> findAll() {
        log.debug("Request to get all CareCompanyTypes");
        List<CareCompanyType> result = careCompanyTypeRepository.findAll();
        return result;
    }

    /**
     *  Get one careCompanyType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CareCompanyType findOne(Long id) {
        log.debug("Request to get CareCompanyType : {}", id);
        CareCompanyType careCompanyType = careCompanyTypeRepository.findOne(id);
        return careCompanyType;
    }

    /**
     *  Delete the  careCompanyType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CareCompanyType : {}", id);
        careCompanyTypeRepository.delete(id);
    }

}