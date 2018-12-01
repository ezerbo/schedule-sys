package com.ss.schedulesys.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.schedulesys.domain.Test;
import com.ss.schedulesys.domain.TestSubcategory;
import com.ss.schedulesys.service.TestService;
import com.ss.schedulesys.service.TestSubCategoryService;
import com.ss.schedulesys.web.rest.util.HeaderUtil;
import com.ss.schedulesys.web.rest.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing Test.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class TestResource {

        
    private TestService testService;
    private TestSubCategoryService testSubcategoryService;
    
    @Autowired
    public TestResource(TestService testService, TestSubCategoryService testSubcategoryService) {
    	this.testService = testService;
    	this.testSubcategoryService = testSubcategoryService;
	}

    /**
     * POST  /tests : Create a new test.
     *
     * @param test the test to create
     * @return the ResponseEntity with status 201 (Created) and with body the new test, or with status 400 (Bad Request) if the test has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/tests")
    public ResponseEntity<Test> createTest(@Valid @RequestBody Test test) throws URISyntaxException {
        log.debug("REST request to save Test : {}", test);
        if (test.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("test", "idexists", "A new test cannot already have an ID")).body(null);
        }
        Test result = testService.save(test);
        return ResponseEntity.created(new URI("/api/tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("test", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tests : Updates an existing test.
     *
     * @param test the test to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated test,
     * or with status 400 (Bad Request) if the test is not valid,
     * or with status 500 (Internal Server Error) if the test couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/tests")
    public ResponseEntity<Test> updateTest(@Valid @RequestBody Test test) throws URISyntaxException {
        log.debug("REST request to update Test : {}", test);
        if (test.getId() == null) {
            return createTest(test);
        }
        Test result = testService.save(test);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("test", test.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tests : get all the tests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tests in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/tests")
    public ResponseEntity<List<Test>> getAllTests(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Tests");
        Page<Test> page = testService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /tests/:id : get the "id" test.
     *
     * @param id the id of the test to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the test, or with status 404 (Not Found)
     */
    @GetMapping("/tests/{id}")
    public ResponseEntity<Test> getTest(@PathVariable String id) {
        log.info("REST request to get Test : {}", id);
        Test test = StringUtils.isNumeric(id) ? testService.findOne(Long.parseLong(id)) : testService.findOne(id);
        return Optional.ofNullable(test)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/tests/{id}/subcategories")
    public ResponseEntity<List<TestSubcategory>> getAllSubcategories(@PathVariable Long id){
    	log.debug("REST request to get all sucategories for test with id : {}", id);
    	List<TestSubcategory> testSubcategories = testSubcategoryService.findAllByTest(id);
    	return (!testSubcategories.isEmpty()) 
    			? new ResponseEntity<>(testSubcategories, HttpStatus.OK)
    			: new ResponseEntity<>(HttpStatus.NOT_FOUND);    	
    }

    /**
     * DELETE  /tests/:id : delete the "id" test.
     *
     * @param id the id of the test to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/tests/{id}")
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        log.debug("REST request to delete Test : {}", id);
        testService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("test", id.toString())).build();
    }
    
    /**
     * GET  /tests/search : get tests starting with 'query'
     *
     * @param query the query used to retrieve tests
     * @return the ResponseEntity with status 200 (OK) and with body the test
     */
    @GetMapping("/tests/search")
    public ResponseEntity<List<Test>> search(@RequestParam String query) {
        log.info("REST request to get Tests : {}", query);
        List<Test> tests = testService.search(query);
        return  new ResponseEntity<>(tests, HttpStatus.OK);
    }
}
