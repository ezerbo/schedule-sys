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

import com.ss.schedulesys.domain.ScheduleStatus;
import com.ss.schedulesys.service.ScheduleStatusService;
import com.ss.schedulesys.web.rest.util.HeaderUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing ScheduleStatus.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ScheduleStatusResource {

        
    private ScheduleStatusService scheduleStatusService;
    
    @Autowired
    public ScheduleStatusResource(ScheduleStatusService scheduleStatusService) {
    	this.scheduleStatusService = scheduleStatusService;
	}

    /**
     * POST  /schedule-statuses : Create a new scheduleStatus.
     *
     * @param scheduleStatus the scheduleStatus to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scheduleStatus, or with status 400 (Bad Request) if the scheduleStatus has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/schedule-statuses")
    public ResponseEntity<ScheduleStatus> createScheduleStatus(@Valid @RequestBody ScheduleStatus scheduleStatus) throws URISyntaxException {
        log.debug("REST request to save ScheduleStatus : {}", scheduleStatus);
        if (scheduleStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("scheduleStatus", "idexists", "A new scheduleStatus cannot already have an ID")).body(null);
        }
        ScheduleStatus result = scheduleStatusService.save(scheduleStatus);
        return ResponseEntity.created(new URI("/api/schedule-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("scheduleStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schedule-statuses : Updates an existing scheduleStatus.
     *
     * @param scheduleStatus the scheduleStatus to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scheduleStatus,
     * or with status 400 (Bad Request) if the scheduleStatus is not valid,
     * or with status 500 (Internal Server Error) if the scheduleStatus couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/schedule-statuses")
    public ResponseEntity<ScheduleStatus> updateScheduleStatus(@Valid @RequestBody ScheduleStatus scheduleStatus) throws URISyntaxException {
        log.debug("REST request to update ScheduleStatus : {}", scheduleStatus);
        if (scheduleStatus.getId() == null) {
            return createScheduleStatus(scheduleStatus);
        }
        ScheduleStatus result = scheduleStatusService.save(scheduleStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("scheduleStatus", scheduleStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schedule-statuses : get all the scheduleStatuses.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of scheduleStatuses in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/schedule-statuses")
    public ResponseEntity<List<ScheduleStatus>> getAllScheduleStatuses()
        throws URISyntaxException {
        log.debug("REST request to get a page of ScheduleStatuses");
        List<ScheduleStatus> scheduleStatuses = scheduleStatusService.findAll();
        return new ResponseEntity<>(scheduleStatuses, HttpStatus.OK);
    }

    /**
     * GET  /schedule-statuses/:id : get the "id" scheduleStatus.
     *
     * @param id the id of the scheduleStatus to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scheduleStatus, or with status 404 (Not Found)
     */
    @GetMapping("/schedule-statuses/{id}")
    public ResponseEntity<ScheduleStatus> getScheduleStatus(@PathVariable Long id) {
        log.debug("REST request to get ScheduleStatus : {}", id);
        ScheduleStatus scheduleStatus = scheduleStatusService.findOne(id);
        return Optional.ofNullable(scheduleStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /schedule-statuses/:id : delete the "id" scheduleStatus.
     *
     * @param id the id of the scheduleStatus to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/schedule-statuses/{id}")
    public ResponseEntity<Void> deleteScheduleStatus(@PathVariable Long id) {
        log.debug("REST request to delete ScheduleStatus : {}", id);
        scheduleStatusService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("scheduleStatus", id.toString())).build();
    }

}
