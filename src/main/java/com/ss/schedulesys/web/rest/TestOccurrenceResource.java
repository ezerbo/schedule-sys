package com.ss.schedulesys.web.rest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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

import com.ss.schedulesys.domain.TestOccurrence;
import com.ss.schedulesys.service.TestOccurrenceService;
import com.ss.schedulesys.web.rest.util.HeaderUtil;
import com.ss.schedulesys.web.rest.util.PaginationUtil;

/**
 * REST controller for managing TestOccurrence.
 */
@RestController
@RequestMapping("/api")
public class TestOccurrenceResource {

    private final Logger log = LoggerFactory.getLogger(TestOccurrenceResource.class);
    
    @Autowired
    private TestOccurrenceService testOcurrenceService;

    /**
     * POST  /test-ocurrences : Create a new testOcurrence.
     *
     * @param testOcurrence the testOcurrence to create
     * @return the ResponseEntity with status 201 (Created) and with body the new testOcurrence, or with status 400 (Bad Request) if the testOcurrence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/test-occurrences")
    public ResponseEntity<TestOccurrence> createTestOccurrence(@Valid @RequestBody TestOccurrence testOcurrence) throws URISyntaxException {
        log.debug("REST request to save TestOccurrence : {}", testOcurrence);
        if (testOcurrence.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("testOcurrence", "idexists", "A new testOcurrence cannot already have an ID")).body(null);
        }
        TestOccurrence result = testOcurrenceService.save(testOcurrence);
        return ResponseEntity.created(new URI("/api/test-ocurrences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("testOcurrence", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /test-occurrences : Updates an existing testOcurrence.
     *
     * @param testOcurrence the testOcurrence to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated testOcurrence,
     * or with status 400 (Bad Request) if the testOcurrence is not valid,
     * or with status 500 (Internal Server Error) if the testOcurrence couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/test-occurrences")
    public ResponseEntity<TestOccurrence> updateTestOccurrence(@Valid @RequestBody TestOccurrence testOcurrence) throws URISyntaxException {
        log.debug("REST request to update TestOccurrence : {}", testOcurrence);
        if (testOcurrence.getId() == null) {
            return createTestOccurrence(testOcurrence);
        }
        TestOccurrence result = testOcurrenceService.save(testOcurrence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("testOcurrence", testOcurrence.getId().toString()))
            .body(result);
    }
    
    /**
     * GET  /test-ocurrences : get all the testOcurrences.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of testOcurrences in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/test-occurrences")
    public ResponseEntity<List<TestOccurrence>> getAllTestOcurrences(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TestOccurrences");
        Page<TestOccurrence> page = testOcurrenceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/test-occurrences");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * DELETE  /test-ocurrences/:id : delete the "id" testOcurrence.
     *
     * @param id the id of the testOcurrence to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/test-occurrences/{id}")
    public ResponseEntity<Void> deleteTestOccurrence(@PathVariable Long id) {
        log.debug("REST request to delete TestOccurrence : {}", id);
        testOcurrenceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("testOccurrence", id.toString())).build();
    }


}