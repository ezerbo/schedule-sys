package com.ss.schedulesys.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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

import com.ss.schedulesys.domain.InsuranceCompany;
import com.ss.schedulesys.domain.License;
import com.ss.schedulesys.service.InsuranceCompanyService;
import com.ss.schedulesys.web.rest.util.HeaderUtil;
import com.ss.schedulesys.web.rest.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class InsuranceCompanyResource {

	private InsuranceCompanyService insuranceCompanyService;
	
	public InsuranceCompanyResource(InsuranceCompanyService insuranceCompanyService) {
		this.insuranceCompanyService = insuranceCompanyService;
	}
	
	  /**
     * POST  /insuance-companies : Create a new insurance company.
     *
     * @param insuranceCompany the insurance company to create
     * @return the ResponseEntity with status 201 (Created) and with body the new insurance company, or with status 400 (Bad Request) if the insurance company has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/insurance-companies")
    public ResponseEntity<InsuranceCompany> create(@Valid @RequestBody InsuranceCompany insuranceCompany) throws URISyntaxException {
        log.info("REST request to save InsuranceCompany : {}", insuranceCompany);
        if (insuranceCompany.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("insuranceCompany", "idexists", "A new insurance company cannot already have an ID")).body(null);
        }
        InsuranceCompany result = insuranceCompanyService.save(insuranceCompany);
        return ResponseEntity.created(new URI("/api/insurance-companies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("insuranceCompany", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /insurance-companies : Updates an existing insurance company.
     *
     * @param insucanceCompany the insurance company to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated insurance company,
     * or with status 400 (Bad Request) if the license is not valid,
     * or with status 500 (Internal Server Error) if the insurance company couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/insurance-companies")
    public ResponseEntity<InsuranceCompany> update(@Valid @RequestBody InsuranceCompany insuranceCompany) throws URISyntaxException {
        log.debug("REST request to update InsuranceCompany : {}", insuranceCompany);
        if (insuranceCompany.getId() == null) {
            return create(insuranceCompany);
        }
        InsuranceCompany result = insuranceCompanyService.save(insuranceCompany);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("insuranceCompany", insuranceCompany.getId().toString()))
            .body(result);
    }

    /**
     * GET  /insurance-companies : get all the insurance-companies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of insurance-companies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    /*@GetMapping("/insurance-companies")
    public ResponseEntity<List<InsuranceCompany>> getAll(){
        log.debug("REST request to get a page of InsuranceCompanies");
        List<InsuranceCompany> insuranceCompanies = insuranceCompanyService.findAll();
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }*/

    /**
     * GET  /licenses/:id : get the "id" license.
     *
     * @param id the id of the license to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the license, or with status 404 (Not Found)
     */
  /*  @GetMapping("/licenses/{id}")
    public ResponseEntity<License> getLicense(@PathVariable Long id) {
        log.debug("REST request to get License : {}", id);
        License license = licenseService.findOne(id);
        return Optional.ofNullable(license)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }*/

    /**
     * DELETE  /licenses/:id : delete the "id" license.
     *
     * @param id the id of the license to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    /*@DeleteMapping("/licenses/{id}")
    public ResponseEntity<Void> deleteLicense(@PathVariable Long id) {
        log.debug("REST request to delete License : {}", id);
        licenseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("license", id.toString())).build();
    }*/
}
