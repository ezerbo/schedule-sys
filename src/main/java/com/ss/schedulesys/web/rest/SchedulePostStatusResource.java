package com.ss.schedulesys.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.ss.schedulesys.domain.SchedulePostStatus;
import com.ss.schedulesys.service.SchedulePostStatusService;
import com.ss.schedulesys.web.rest.util.HeaderUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing SchedulePostStatus.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class SchedulePostStatusResource {

        
    private SchedulePostStatusService schedulePostStatusService;
    
    @Autowired
    public SchedulePostStatusResource(SchedulePostStatusService schedulePostStatusService) {
    	this.schedulePostStatusService = schedulePostStatusService;
	}

    /**
     * POST  /schedule-post-statuses : Create a new schedulePostStatus.
     *
     * @param schedulePostStatus the schedulePostStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new schedulePostStatus, or with status 400 (Bad Request) if the schedulePostStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/schedule-post-statuses")
    public ResponseEntity<SchedulePostStatus> createSchedulePostStatus(@Valid @RequestBody SchedulePostStatus schedulePostStatus) throws URISyntaxException {
        log.debug("REST request to save SchedulePostStatus : {}", schedulePostStatus);
        if (schedulePostStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("schedulePostStatus", "idexists", "A new schedulePostStatus cannot already have an ID")).body(null);
        }
        SchedulePostStatus result = schedulePostStatusService.save(schedulePostStatus);
        return ResponseEntity.created(new URI("/api/schedule-post-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("schedulePostStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schedule-post-statuses : Updates an existing schedulePostStatus.
     *
     * @param schedulePostStatus the schedulePostStatus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated schedulePostStatus,
     * or with status 400 (Bad Request) if the schedulePostStatus is not valid,
     * or with status 500 (Internal Server Error) if the schedulePostStatus couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/schedule-post-statuses")
    public ResponseEntity<SchedulePostStatus> updateSchedulePostStatus(@Valid @RequestBody SchedulePostStatus schedulePostStatus) throws URISyntaxException {
        log.debug("REST request to update SchedulePostStatus : {}", schedulePostStatus);
        if (schedulePostStatus.getId() == null) {
            return createSchedulePostStatus(schedulePostStatus);
        }
        SchedulePostStatus result = schedulePostStatusService.save(schedulePostStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("schedulePostStatus", schedulePostStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schedule-post-statuses : get all the schedulePostStatuses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of schedulePostStatuses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/schedule-post-statuses")
    public ResponseEntity<List<SchedulePostStatus>> getAllSchedulePostStatuses()
        throws URISyntaxException {
        log.debug("REST request to get a page of SchedulePostStatuses");
        List<SchedulePostStatus> schedulePostStatuses = schedulePostStatusService.findAll();
        return new ResponseEntity<>(schedulePostStatuses, HttpStatus.OK);
    }

    /**
     * GET  /schedule-post-statuses/:id : get the "id" schedulePostStatus.
     *
     * @param id the id of the schedulePostStatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the schedulePostStatus, or with status 404 (Not Found)
     */
    @GetMapping("/schedule-post-statuses/{id}")
    public ResponseEntity<SchedulePostStatus> getSchedulePostStatus(@PathVariable Long id) {
        log.debug("REST request to get SchedulePostStatus : {}", id);
        SchedulePostStatus schedulePostStatus = schedulePostStatusService.findOne(id);
        return Optional.ofNullable(schedulePostStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /schedule-post-statuses/:id : delete the "id" schedulePostStatus.
     *
     * @param id the id of the schedulePostStatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/schedule-post-statuses/{id}")
    public ResponseEntity<Void> deleteSchedulePostStatus(@PathVariable Long id) {
        log.debug("REST request to delete SchedulePostStatus : {}", id);
        schedulePostStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("schedulePostStatus", id.toString())).build();
    }

}