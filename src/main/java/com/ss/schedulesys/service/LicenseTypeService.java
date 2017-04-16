package com.ss.schedulesys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.LicenseType;
import com.ss.schedulesys.repository.LicenseTypeRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation for managing LicenseType.
 */

@Slf4j
@Service
@Transactional
public class LicenseTypeService {

    
    private LicenseTypeRepository licenseTypeRepository;

    @Autowired
    public LicenseTypeService(LicenseTypeRepository licenseTypeRepository) {
    	this.licenseTypeRepository = licenseTypeRepository;
	}
    
    /**
     * Save a licenseType.
     *
     * @param licenseType the entity to save
     * @return the persisted entity
     */
    public LicenseType save(LicenseType licenseType) {
        log.debug("Request to save LicenseType : {}", licenseType);
        LicenseType result = licenseTypeRepository.save(licenseType);
        return result;
    }

    /**
     *  Get all the licenseTypes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<LicenseType> findAll(Pageable pageable) {
        log.debug("Request to get all LicenseTypes");
        Page<LicenseType> result = licenseTypeRepository.findAll(pageable);
        return result;
    }
    
    @Transactional(readOnly = true) 
    public List<LicenseType> findAll() {
        log.debug("Request to get all LicenseTypes");
        List<LicenseType> result = licenseTypeRepository.findAll();
        return result;
    }

    /**
     *  Get one licenseType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public LicenseType findOne(Long id) {
        log.debug("Request to get LicenseType : {}", id);
        LicenseType licenseType = licenseTypeRepository.findOne(id);
        return licenseType;
    }

    /**
     *  Delete the  licenseType by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete LicenseType : {}", id);
        licenseTypeRepository.delete(id);
    }

}