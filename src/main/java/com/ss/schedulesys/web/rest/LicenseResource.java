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

import com.ss.schedulesys.domain.License;
import com.ss.schedulesys.service.EmployeeService;
import com.ss.schedulesys.service.LicenseService;
import com.ss.schedulesys.web.rest.util.HeaderUtil;
import com.ss.schedulesys.web.rest.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing License.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class LicenseResource {

        
    private LicenseService licenseService;
    private EmployeeService employeeService;
    
    
    //@Autowired
    public LicenseResource(LicenseService licenseService, EmployeeService employeeService) {
    	this.licenseService = licenseService;
    	this.employeeService = employeeService;
	}

    /**
     * POST  /licenses : Create a new license.
     *
     * @param license the license to create
     * @return the ResponseEntity with status 201 (Created) and with body the new license, or with status 400 (Bad Request) if the license has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/licenses")
    public ResponseEntity<License> createLicense(@Valid @RequestBody License license) throws URISyntaxException {
        log.info("REST request to save License : {}", license);
        if (license.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("license", "idexists", "A new license cannot already have an ID")).body(null);
        }
        employeeService.save(license.getEmployee());
        License result = licenseService.save(license);
        return ResponseEntity.created(new URI("/api/licenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("license", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /licenses : Updates an existing license.
     *
     * @param license the license to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated license,
     * or with status 400 (Bad Request) if the license is not valid,
     * or with status 500 (Internal Server Error) if the license couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/licenses")
    public ResponseEntity<License> updateLicense(@Valid @RequestBody License license) throws URISyntaxException {
        log.debug("REST request to update License : {}", license);
        if (license.getId() == null) {
            return createLicense(license);
        }
        employeeService.save(license.getEmployee());
        License result = licenseService.save(license);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("license", license.getId().toString()))
            .body(result);
    }

    /**
     * GET  /licenses : get all the licenses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of licenses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/licenses")
    public ResponseEntity<List<License>> getAllLicenses(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Licenses");
        Page<License> page = licenseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/licenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /licenses/:id : get the "id" license.
     *
     * @param id the id of the license to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the license, or with status 404 (Not Found)
     */
    @GetMapping("/licenses/{id}")
    public ResponseEntity<License> getLicense(@PathVariable Long id) {
        log.debug("REST request to get License : {}", id);
        License license = licenseService.findOne(id);
        return Optional.ofNullable(license)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /licenses/:id : delete the "id" license.
     *
     * @param id the id of the license to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/licenses/{id}")
    public ResponseEntity<Void> deleteLicense(@PathVariable Long id) {
        log.debug("REST request to delete License : {}", id);
        licenseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("license", id.toString())).build();
    }

}