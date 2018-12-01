package com.ss.schedulesys.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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

import com.ss.schedulesys.domain.TestSubcategory;
import com.ss.schedulesys.service.TestSubCategoryService;
import com.ss.schedulesys.web.rest.util.HeaderUtil;
import com.ss.schedulesys.web.rest.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing TestSubcategory.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class TestSubCategoryResource {

        
    private TestSubCategoryService testSubCategoryService;
    
    @Autowired
    public TestSubCategoryResource(TestSubCategoryService testSubCategoryService) {
    	this.testSubCategoryService = testSubCategoryService;
	}

    /**
     * POST  /test-sub-categories : Create a new testSubCategory.
     *
     * @param testSubCategory the testSubCategory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new testSubCategory, or with status 400 (Bad Request) if the testSubCategory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/test-sub-categories")
    public ResponseEntity<TestSubcategory> createTestSubcategory(@Valid @RequestBody TestSubcategory testSubCategory) throws URISyntaxException {
        log.debug("REST request to save TestSubcategory : {}", testSubCategory);
        if (testSubCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("testSubCategory", "idexists", "A new testSubCategory cannot already have an ID")).body(null);
        }
        TestSubcategory result = testSubCategoryService.save(testSubCategory);
        return ResponseEntity.created(new URI("/api/test-sub-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("testSubCategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /test-sub-categories : Updates an existing testSubCategory.
     *
     * @param testSubCategory the testSubCategory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated testSubCategory,
     * or with status 400 (Bad Request) if the testSubCategory is not valid,
     * or with status 500 (Internal Server Error) if the testSubCategory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/test-sub-categories")
    public ResponseEntity<TestSubcategory> updateTestSubcategory(@Valid @RequestBody TestSubcategory testSubCategory) throws URISyntaxException {
        log.debug("REST request to update TestSubcategory : {}", testSubCategory);
        if (testSubCategory.getId() == null) {
            return createTestSubcategory(testSubCategory);
        }
        TestSubcategory result = testSubCategoryService.save(testSubCategory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("testSubCategory", testSubCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /test-sub-categories : get all the testSubCategories.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of testSubCategories in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/test-sub-categories")
    public ResponseEntity<List<TestSubcategory>> getAllTestSubCategories(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of TestSubCategories");
        Page<TestSubcategory> page = testSubCategoryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/test-sub-categories");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /test-sub-categories/:id : get the "id" testSubCategory.
     *
     * @param id the id of the testSubCategory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the testSubCategory, or with status 404 (Not Found)
     */
    @GetMapping("/test-sub-categories/{id}")
    public ResponseEntity<TestSubcategory> getTestSubcategory(@PathVariable Long id) {
        log.debug("REST request to get TestSubcategory : {}", id);
        TestSubcategory testSubCategory = testSubCategoryService.findOne(id);
        return Optional.ofNullable(testSubCategory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /test-sub-categories/:id : delete the "id" testSubCategory.
     *
     * @param id the id of the testSubCategory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/test-sub-categories/{id}")
    public ResponseEntity<Void> deleteTestSubcategory(@PathVariable Long id) {
        log.debug("REST request to delete TestSubcategory : {}", id);
        testSubCategoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("testSubCategory", id.toString())).build();
    }

}