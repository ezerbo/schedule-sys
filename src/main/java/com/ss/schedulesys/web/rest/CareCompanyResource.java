package com.ss.schedulesys.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.schedulesys.domain.CareCompany;
import com.ss.schedulesys.domain.CompanyContact;
import com.ss.schedulesys.domain.Schedule;
import com.ss.schedulesys.service.CareCompanyService;
import com.ss.schedulesys.service.CompanyContactService;
import com.ss.schedulesys.service.ScheduleService;
import com.ss.schedulesys.web.rest.util.HeaderUtil;
import com.ss.schedulesys.web.rest.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing CareCompany.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class CareCompanyResource {

    private CareCompanyService careCompanyService;
    private ScheduleService scheduleService;
    private CompanyContactService contactService;
    
    @Autowired
    public CareCompanyResource(CareCompanyService careCompanyService,
    		ScheduleService scheduleService, CompanyContactService contactService) {
    	this.careCompanyService = careCompanyService;
    	this.scheduleService = scheduleService;
    	this.contactService = contactService;
	}

    /**
     * POST  /care-companies : Create a new careCompany.
     *
     * @param careCompany the careCompany to create
     * @return the ResponseEntity with status 201 (Created) and with body the new careCompany, or with status 400 (Bad Request) if the careCompany has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/care-companies")
    public ResponseEntity<CareCompany> createCareCompany(@Valid @RequestBody CareCompany careCompany) throws URISyntaxException {
        log.debug("REST request to save CareCompany : {}", careCompany);
        if (careCompany.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("careCompany", "idexists", "A new careCompany cannot already have an ID")).body(null);
        }
        CareCompany result = careCompanyService.save(careCompany);
        return ResponseEntity.created(new URI("/api/care-companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("careCompany", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /care-companies : Updates an existing careCompany.
     *
     * @param careCompany the careCompany to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated careCompany,
     * or with status 400 (Bad Request) if the careCompany is not valid,
     * or with status 500 (Internal Server Error) if the careCompany couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/care-companies")
    public ResponseEntity<CareCompany> updateCareCompany(@Valid @RequestBody CareCompany careCompany) throws URISyntaxException {
        log.debug("REST request to update CareCompany : {}", careCompany);
        if (careCompany.getId() == null) {
            return createCareCompany(careCompany);
        }
        CareCompany result = careCompanyService.save(careCompany);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("careCompany", careCompany.getId().toString()))
            .body(result);
    }

    /**
     * GET  /care-companies : get all the careCompanies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of careCompanies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/care-companies")
    public ResponseEntity<List<CareCompany>> getAllCareCompanies(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CareCompanies");
        Page<CareCompany> page = careCompanyService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/care-companies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /care-companies/:id : get the "id" careCompany.
     *
     * @param id the id or name of the careCompany to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the careCompany, or with status 404 (Not Found)
     */
    @GetMapping("/care-companies/{id}")
    public ResponseEntity<CareCompany> getCareCompany(@PathVariable String id) {
        log.debug("REST request to get CareCompany : {}", id);
        CareCompany careCompany = (StringUtils.isNumber(id)) 
        		? careCompanyService.findOne(Long.valueOf(id)) : careCompanyService.findOneByName(id);
        return Optional.ofNullable(careCompany)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /care-companies/:id : delete the "id" careCompany.
     *
     * @param id the id of the careCompany to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/care-companies/{id}")
    public ResponseEntity<Void> deleteCareCompany(@PathVariable Long id) {
        log.debug("REST request to delete CareCompany : {}", id);
        careCompanyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("careCompany", id.toString())).build();
    }
    
    @GetMapping("/care-companies/{id}/contacts")
    public ResponseEntity<List<CompanyContact>> getContacts(@PathVariable Long id){
    	log.debug("REST request to get contacts for company with id : {}", id);
    	List<CompanyContact> contacts = contactService.getAllByEmployee(id);
    	return (!contacts.isEmpty())
    			? new ResponseEntity<>(contacts, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    //TODO Add contact resources
    @GetMapping("/care-companies/{id}/schedules")
    public ResponseEntity<List<Schedule>> getSchedules(@PathVariable Long id){
    	log.debug("REST request to get schedules for company with id : {}", id);
    	List<Schedule> schedules = scheduleService.findAllByEmployee(id);
    	return (!schedules.isEmpty())
    			? new ResponseEntity<>(schedules, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
