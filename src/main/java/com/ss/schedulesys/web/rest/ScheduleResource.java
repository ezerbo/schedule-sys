package com.ss.schedulesys.web.rest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.schedulesys.config.CompanyTypeConstants;
import com.ss.schedulesys.domain.Schedule;
import com.ss.schedulesys.domain.ScheduleSummary;
import com.ss.schedulesys.domain.ScheduleSysUser;
import com.ss.schedulesys.domain.ScheduleUpdate;
import com.ss.schedulesys.service.ScheduleService;
import com.ss.schedulesys.service.ScheduleSummaryService;
import com.ss.schedulesys.service.ScheduleUpdateService;
import com.ss.schedulesys.service.UserService;
import com.ss.schedulesys.service.util.SecurityUtil;
import com.ss.schedulesys.web.rest.util.HeaderUtil;
import com.ss.schedulesys.web.rest.util.PaginationUtil;

/**
 * @author ezerbo
 * REST controller for managing Schedule.
 */
@RestController
@RequestMapping("/api")
public class ScheduleResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleResource.class);
        
    private ScheduleService scheduleService;
    //TODO Remove once security feature has been built
    private UserService userService;
    private ScheduleSummaryService scheduleSummaryService;
    private ScheduleUpdateService scheduleUpdateService;
    
    @Autowired
    public ScheduleResource(
    		ScheduleService scheduleService, UserService userService,
    		ScheduleSummaryService scheduleSummaryService, ScheduleUpdateService scheduleUpdateService) {
		this.scheduleService = scheduleService;
		this.userService = userService;
		this.scheduleSummaryService = scheduleSummaryService;
		this.scheduleUpdateService = scheduleUpdateService;
	}

    /**
     * POST  /schedules : Create a new schedule.
     *
     * @param schedule the schedule to create
     * @return the ResponseEntity with status 201 (Created) and with body the new schedule, or with status 400 (Bad Request) if the schedule has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/schedules")
    public ResponseEntity<Schedule> createSchedule(@Valid @RequestBody Schedule schedule) throws URISyntaxException {
        log.debug("REST request to save Schedule : {}", schedule);
        if (schedule.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("schedule", "idexists", "A new schedule cannot already have an ID")).body(null);
        }
        ScheduleSysUser user = userService.findByUsername(SecurityUtil.getAuthenticatedUser()).get();
        Schedule result = scheduleService.save(schedule, user);
        return ResponseEntity.created(new URI("/api/schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("schedule", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schedules : Updates an existing schedule.
     *
     * @param schedule the schedule to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated schedule,
     * or with status 400 (Bad Request) if the schedule is not valid,
     * or with status 500 (Internal Server Error) if the schedule couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/schedules")
    public ResponseEntity<Schedule> updateSchedule(@Valid @RequestBody Schedule schedule) throws URISyntaxException {
        log.debug("REST request to update Schedule : {}", schedule);
        if (schedule.getId() == null) {
            return createSchedule(schedule);
        }
        ScheduleSysUser user = userService.findByUsername(SecurityUtil.getAuthenticatedUser()).get();
        Schedule result = scheduleService.save(schedule, user);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("schedule", schedule.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schedules : get all the schedules.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of schedules in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> getAllSchedules(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Schedules");
        Page<Schedule> page = scheduleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/schedules");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /schedules : get history of changes applied to schedule with provided id.
     *
     * @param id id of schedule being looked up
     * @return the ResponseEntity with status 200 (OK) and the list of schedules in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/schedules/{id}/audit")
    public ResponseEntity<List<ScheduleUpdate>> getAllScheduleAudit(@PathVariable Long id)
        throws URISyntaxException {
        log.debug("REST request to get a page of Schedules");
        List<ScheduleUpdate> scheduleUpdates = scheduleUpdateService.findByScheduleId(id);
        return new ResponseEntity<>(scheduleUpdates, HttpStatus.OK);
    }
    
    /**
     * GET  /schedules : get all the schedules on specific date.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of schedules in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/schedules/employee-summary")
    public ResponseEntity<List<Schedule>> getAllSchedules(@RequestParam Date scheduleDate, @RequestParam(required = false) String by)
        throws URISyntaxException {
    	// Ignored 'by', Will use it when we need a summary of schedules for private cares
        log.debug("REST request to get a page of Schedules");
        List<Schedule> schedules = scheduleService.findAllByDateAndCompanyType(scheduleDate, CompanyTypeConstants.FACILITY);
        return new ResponseEntity<>(schedules, HttpStatus.OK);
    }

    /**
     * GET  /schedules/:id : get the "id" schedule.
     *
     * @param id the id of the schedule to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the schedule, or with status 404 (Not Found)
     */
    @GetMapping("/schedules/{id}")
    public ResponseEntity<Schedule> getSchedule(@PathVariable Long id) {
        log.debug("REST request to get Schedule : {}", id);
        Schedule schedule = scheduleService.findOne(id);
        return Optional.ofNullable(schedule)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/schedules/company-summary")
    public ResponseEntity<List<ScheduleSummary>> getSchedulesSummary(@RequestParam Date scheduleDate) throws Exception {
        log.info("REST request to get Schedules summary for : {}", scheduleDate);
        List<ScheduleSummary> schedulesSummary = scheduleSummaryService.getSchedulesSummary(scheduleDate);
        return new ResponseEntity<>(schedulesSummary, HttpStatus.OK);
    }

    /**
     * DELETE  /schedules/:id : delete the "id" schedule.
     *
     * @param id the id of the schedule to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        log.debug("REST request to delete Schedule : {}", id);
        scheduleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("schedule", id.toString())).build();
    }

}