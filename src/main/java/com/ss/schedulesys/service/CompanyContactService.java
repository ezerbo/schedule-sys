package com.ss.schedulesys.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.CareCompany;
import com.ss.schedulesys.domain.CompanyContact;
import com.ss.schedulesys.repository.CareCompanyRepository;
import com.ss.schedulesys.repository.CompanyContactRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

import lombok.extern.slf4j.Slf4j;

/**
 * Service Implementation for managing Contact.
 */
@Slf4j
@Service
@Transactional
public class CompanyContactService {

    private CompanyContactRepository contactRepository;
    private CareCompanyRepository careCompanyRepository;

    public CompanyContactService(CompanyContactRepository contactRepository,
    		CareCompanyRepository careCompanyRepository) {
    	this.contactRepository = contactRepository;
    	this.careCompanyRepository = careCompanyRepository;
	}
    /**
     * Save a contact.
     *
     * @param contact the entity to save
     * @return the persisted entity
     */
    public CompanyContact save(CompanyContact contact) {
        log.debug("Request to save Contact : {}", contact);
        CareCompany careCompany = Optional.ofNullable(contact.getCareCompany())
        			.map(result -> careCompanyRepository.findOne(result.getId()))
        			.orElseThrow(() -> new ScheduleSysException("A valid care company is required to create a contact"));
        contact.setCareCompany(careCompany);
        CompanyContact result = contactRepository.save(contact);
        return result;
    }
    
    @Transactional(readOnly = true)
    public Page<CompanyContact> getAllByCareCompanyId(Long careCompanyId, Pageable pageable) {
    	log.debug("Request to get all Contacts for care company with id : {}", careCompanyId);
    	Page<CompanyContact> contacts = contactRepository.getAllByCareCompanyId(careCompanyId, pageable);
    	return contacts;
    }

    /**
     *  Get all the contacts.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CompanyContact> findAll(Pageable pageable) {
        log.debug("Request to get all Contacts");
        Page<CompanyContact> result = contactRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one contact by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CompanyContact findOne(Long id) {
        log.debug("Request to get Contact : {}", id);
        CompanyContact contact = contactRepository.findOne(id);
        return contact;
    }

    /**
     *  Delete the  contact by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Contact : {}", id);
        contactRepository.delete(id);
    }

}