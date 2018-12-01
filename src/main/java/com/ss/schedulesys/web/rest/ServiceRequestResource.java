package com.ss.schedulesys.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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

import com.ss.schedulesys.domain.ServiceRequest;
import com.ss.schedulesys.service.ServiceRequestService;
import com.ss.schedulesys.web.rest.util.HeaderUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class ServiceRequestResource {

	private ServiceRequestService serviceRequestService;
	
	public ServiceRequestResource(ServiceRequestService serviceRequestService) {
		this.serviceRequestService = serviceRequestService;
	}
	
	/**
     * POST  /service-requests : Create a new service request.
     *
     * @param serviceRequest the ServiceRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new service request, or with status 400 (Bad Request) if the service request has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/service-requests")
    public ResponseEntity<ServiceRequest> create(@Valid @RequestBody ServiceRequest serviceRequest) throws URISyntaxException {
        log.info("REST request to save ServiceRequest : {}", serviceRequest);
        if (serviceRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serviceRequest", "idexists", "A new service request cannot already have an ID")).body(null);
        }
        ServiceRequest result = serviceRequestService.save(serviceRequest);
        return ResponseEntity.created(new URI("/api/service-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /service-request : Updates an existing service request.
     *
     * @param serviceRequest the Service Request to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated service request,
     * or with status 400 (Bad Request) if the service request is not valid,
     * or with status 500 (Internal Server Error) if the service request couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/service-requests")
    public ResponseEntity<ServiceRequest> update(@Valid @RequestBody ServiceRequest serviceRequest) throws URISyntaxException {
        log.debug("REST request to update ServiceRequest : {}", serviceRequest);
        if (serviceRequest.getId() == null) {
            return create(serviceRequest);
        }
        ServiceRequest result = serviceRequestService.save(serviceRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceRequest", serviceRequest.getId().toString()))
            .body(result);
    }
    
    /**
     * GET  /service-requests : get all the service requests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of service requests in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/service-requests")
    public ResponseEntity<List<ServiceRequest>> getAll() throws URISyntaxException {
        log.debug("REST request to get a page of ServiceRequests");
        List<ServiceRequest> requests = serviceRequestService.findAll();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    /**
     * GET  /service-requests/:id : get the "id" servicRequest.
     *
     * @param id the id of the service request to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the service request, or with status 404 (Not Found)
     */
    @GetMapping("/service-requests/{id}")
    public ResponseEntity<ServiceRequest> get(@PathVariable Long id) {
        log.debug("REST request to get ServiceRequest : {}", id);
        ServiceRequest serviceRequest = serviceRequestService.findOne(id);
        return Optional.ofNullable(serviceRequest)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /service-requests/:id : delete the "id" service request.
     *
     * @param id the id of the service request to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/service-requests/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete ServiceRequest : {}", id);
        serviceRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceRequest", id.toString())).build();
    }
}
