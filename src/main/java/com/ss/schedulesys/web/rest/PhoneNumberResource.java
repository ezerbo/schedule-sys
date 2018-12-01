package com.ss.schedulesys.web.rest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.ss.schedulesys.domain.PhoneNumber;
import com.ss.schedulesys.service.PhoneNumberService;
import com.ss.schedulesys.web.rest.util.HeaderUtil;
import com.ss.schedulesys.web.rest.util.PaginationUtil;

/**
 * REST controller for managing PhoneNumber.
 */
@RestController
@RequestMapping("/api")
public class PhoneNumberResource {

    private final Logger log = LoggerFactory.getLogger(PhoneNumberResource.class);
        
    private PhoneNumberService phoneNumberService;
    
    @Autowired
    public PhoneNumberResource(PhoneNumberService phoneNumberService) {
		this.phoneNumberService = phoneNumberService;
	}

    /**
     * POST  /phone-numbers : Create a new phoneNumber.
     *
     * @param phoneNumber the phoneNumber to create
     * @return the ResponseEntity with status 201 (Created) and with body the new phoneNumber, or with status 400 (Bad Request) if the phoneNumber has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/phone-numbers")
    public ResponseEntity<PhoneNumber> createPhoneNumber(@Valid @RequestBody PhoneNumber phoneNumber) throws URISyntaxException {
        log.debug("REST request to save PhoneNumber : {}", phoneNumber);
        if (phoneNumber.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("phoneNumber", "idexists", "A new phoneNumber cannot already have an ID")).body(null);
        }
        PhoneNumber result = phoneNumberService.create(phoneNumber);
        return ResponseEntity.created(new URI("/api/phone-numbers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("phoneNumber", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /phone-numbers : Updates an existing phoneNumber.
     *
     * @param phoneNumber the phoneNumber to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated phoneNumber,
     * or with status 400 (Bad Request) if the phoneNumber is not valid,
     * or with status 500 (Internal Server Error) if the phoneNumber couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/phone-numbers")
    public ResponseEntity<PhoneNumber> updatePhoneNumber(@Valid @RequestBody PhoneNumber phoneNumber) throws URISyntaxException {
        log.debug("REST request to update PhoneNumber : {}", phoneNumber);
        if (phoneNumber.getId() == null) {
            return createPhoneNumber(phoneNumber);
        }
        PhoneNumber result = phoneNumberService.create(phoneNumber);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("phoneNumber", phoneNumber.getId().toString()))
            .body(result);
    }

    /**
     * GET  /phone-numbers : get all the phoneNumbers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of phoneNumbers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/phone-numbers")
    public ResponseEntity<List<PhoneNumber>> getAllPhoneNumbers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of PhoneNumbers");
        Page<PhoneNumber> page = phoneNumberService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/phone-numbers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /phone-numbers/:id : get the "id" phoneNumber.
     *
     * @param id the id of the phoneNumber to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the phoneNumber, or with status 404 (Not Found)
     */
    @GetMapping("/phone-numbers/{id}")
    public ResponseEntity<PhoneNumber> getPhoneNumber(@PathVariable Long id) {
        log.debug("REST request to get PhoneNumber : {}", id);
        PhoneNumber phoneNumber = phoneNumberService.findOne(id);
        return Optional.ofNullable(phoneNumber)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /phone-numbers/:id : delete the "id" phoneNumber.
     *
     * @param id the id of the phoneNumber to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/phone-numbers/{id}")
    public ResponseEntity<Void> deletePhoneNumber(@PathVariable Long id) {
        log.debug("REST request to delete PhoneNumber : {}", id);
        if(phoneNumberService.findOne(id) == null){
        	return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        phoneNumberService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("phoneNumber", id.toString())).build();
    }

    /**
     * SEARCH  /_search/phone-numbers?query=:query : search for the phoneNumber corresponding
     * to the query.
     *
     * @param query the query of the phoneNumber search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
//    @GetMapping("/_search/phone-numbers")
//    @Timed
//    public ResponseEntity<List<PhoneNumber>> searchPhoneNumbers(@RequestParam String query, Pageable pageable)
//        throws URISyntaxException {
//        log.debug("REST request to search for a page of PhoneNumbers for query {}", query);
//        Page<PhoneNumber> page = phoneNumberService.search(query, pageable);
//        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/phone-numbers");
//        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//    }


}