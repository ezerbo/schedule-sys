package com.ss.schedulesys.web.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.schedulesys.domain.CareCompanyType;
import com.ss.schedulesys.service.CareCompanyTypeService;

import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing CareCompanyType.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class CareCompanyTypeResource {

        
    private CareCompanyTypeService careCompanyTypeService;
    
    @Autowired
    public CareCompanyTypeResource(CareCompanyTypeService careCompanyTypeService) {
		this.careCompanyTypeService = careCompanyTypeService;
	}

    /**
     * POST  /care-company-types : Create a new careCompanyType.
     *
     * @param careCompanyType the careCompanyType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new careCompanyType, or with status 400 (Bad Request) if the careCompanyType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
//    @PostMapping("/care-company-types")
//    public ResponseEntity<CareCompanyType> createCareCompanyType(@Valid @RequestBody CareCompanyType careCompanyType) throws URISyntaxException {
//        log.debug("REST request to save CareCompanyType : {}", careCompanyType);
//        if (careCompanyType.getId() != null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("careCompanyType", "idexists", "A new careCompanyType cannot already have an ID")).body(null);
//        }
//        CareCompanyType result = careCompanyTypeService.save(careCompanyType);
//        return ResponseEntity.created(new URI("/api/care-company-types/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert("careCompanyType", result.getId().toString()))
//            .body(result);
//    }

    /**
     * PUT  /care-company-types : Updates an existing careCompanyType.
     *
     * @param careCompanyType the careCompanyType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated careCompanyType,
     * or with status 400 (Bad Request) if the careCompanyType is not valid,
     * or with status 500 (Internal Server Error) if the careCompanyType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
//    @PutMapping("/care-company-types")
//    public ResponseEntity<CareCompanyType> updateCareCompanyType(@Valid @RequestBody CareCompanyType careCompanyType) throws URISyntaxException {
//        log.debug("REST request to update CareCompanyType : {}", careCompanyType);
//        if (careCompanyType.getId() == null) {
//            return createCareCompanyType(careCompanyType);
//        }
//        CareCompanyType result = careCompanyTypeService.save(careCompanyType);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert("careCompanyType", careCompanyType.getId().toString()))
//            .body(result);
//    }

    /**
     * GET  /care-company-types : get all the careCompanyTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of careCompanyTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/care-company-types")
    public ResponseEntity<List<CareCompanyType>> getAllCareCompanyTypes() {
        log.debug("REST request to get a page of CareCompanyTypes");
        List<CareCompanyType> careCompanyTypes = careCompanyTypeService.findAll();
        return new ResponseEntity<>(careCompanyTypes, HttpStatus.OK);
    }

    /**
     * GET  /care-company-types/:id : get the "id" careCompanyType.
     *
     * @param id the id of the careCompanyType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the careCompanyType, or with status 404 (Not Found)
     */
    @GetMapping("/care-company-types/{id}")
    public ResponseEntity<CareCompanyType> getCareCompanyType(@PathVariable Long id) {
        log.debug("REST request to get CareCompanyType : {}", id);
        CareCompanyType careCompanyType = careCompanyTypeService.findOne(id);
        return Optional.ofNullable(careCompanyType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /care-company-types/:id : delete the "id" careCompanyType.
     *
     * @param id the id of the careCompanyType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
//    @DeleteMapping("/care-company-types/{id}")
//    public ResponseEntity<Void> deleteCareCompanyType(@PathVariable Long id) {
//        log.debug("REST request to delete CareCompanyType : {}", id);
//        careCompanyTypeService.delete(id);
//        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("careCompanyType", id.toString())).build();
//    }

}
