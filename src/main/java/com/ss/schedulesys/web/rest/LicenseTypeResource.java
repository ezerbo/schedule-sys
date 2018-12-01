package com.ss.schedulesys.web.rest;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.schedulesys.domain.LicenseType;
import com.ss.schedulesys.service.LicenseTypeService;
import com.ss.schedulesys.web.rest.util.HeaderUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing LicenseType.
 */

@Slf4j
@RestController
@RequestMapping("/api")
public class LicenseTypeResource {

    private LicenseTypeService licenseTypeService;
    
    @Autowired
    public LicenseTypeResource(LicenseTypeService licenseTypeService) {
		this.licenseTypeService = licenseTypeService;
	}

    /**
     * POST  /license-types : Create a new licenseType.
     *
     * @param licenseType the licenseType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new licenseType, or with status 400 (Bad Request) if the licenseType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
//    @PostMapping("/license-types")
//    public ResponseEntity<LicenseType> createLicenseType(@Valid @RequestBody LicenseType licenseType) throws URISyntaxException {
//        log.debug("REST request to save LicenseType : {}", licenseType);
//        if (licenseType.getId() != null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("licenseType", "idexists", "A new licenseType cannot already have an ID")).body(null);
//        }
//        LicenseType result = licenseTypeService.save(licenseType);
//        return ResponseEntity.created(new URI("/api/license-types/" + result.getId()))
//            .headers(HeaderUtil.createEntityCreationAlert("licenseType", result.getId().toString()))
//            .body(result);
//    }

    /**
     * PUT  /license-types : Updates an existing licenseType.
     *
     * @param licenseType the licenseType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated licenseType,
     * or with status 400 (Bad Request) if the licenseType is not valid,
     * or with status 500 (Internal Server Error) if the licenseType couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
//    @PutMapping("/license-types")
//    public ResponseEntity<LicenseType> updateLicenseType(@Valid @RequestBody LicenseType licenseType) throws URISyntaxException {
//        log.debug("REST request to update LicenseType : {}", licenseType);
//        if (licenseType.getId() == null) {
//            return createLicenseType(licenseType);
//        }
//        LicenseType result = licenseTypeService.save(licenseType);
//        return ResponseEntity.ok()
//            .headers(HeaderUtil.createEntityUpdateAlert("licenseType", licenseType.getId().toString()))
//            .body(result);
//    }

    /**
     * GET  /license-types : get all the licenseTypes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of licenseTypes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/license-types")
    public ResponseEntity<List<LicenseType>> getAllLicenseTypes()
        throws URISyntaxException {
        log.debug("REST request to get a page of LicenseTypes");
        List<LicenseType> licenseTypes = licenseTypeService.findAll();
        return new ResponseEntity<>(licenseTypes, HttpStatus.OK);
    }

    /**
     * GET  /license-types/:id : get the "id" licenseType.
     *
     * @param id the id of the licenseType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the licenseType, or with status 404 (Not Found)
     */
    @GetMapping("/license-types/{id}")
    public ResponseEntity<LicenseType> getLicenseType(@PathVariable Long id) {
        log.debug("REST request to get LicenseType : {}", id);
        LicenseType licenseType = licenseTypeService.findOne(id);
        return Optional.ofNullable(licenseType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /license-types/:id : delete the "id" licenseType.
     *
     * @param id the id of the licenseType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/license-types/{id}")
    public ResponseEntity<Void> deleteLicenseType(@PathVariable Long id) {
        log.debug("REST request to delete LicenseType : {}", id);
        licenseTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("licenseType", id.toString())).build();
    }

}