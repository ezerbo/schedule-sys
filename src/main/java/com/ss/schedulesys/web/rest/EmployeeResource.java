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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.schedulesys.domain.Employee;
import com.ss.schedulesys.domain.License;
import com.ss.schedulesys.domain.PhoneNumber;
import com.ss.schedulesys.domain.Schedule;
import com.ss.schedulesys.domain.TestOccurrence;
import com.ss.schedulesys.service.EmployeeService;
import com.ss.schedulesys.service.LicenseService;
import com.ss.schedulesys.service.PhoneNumberService;
import com.ss.schedulesys.service.ScheduleService;
import com.ss.schedulesys.service.TestOccurrenceService;
import com.ss.schedulesys.web.rest.util.HeaderUtil;
import com.ss.schedulesys.web.rest.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class EmployeeResource {

	private EmployeeService employeeService;
	private PhoneNumberService phoneNumberService;
	private LicenseService licensesService;
	private TestOccurrenceService testOccurrenceService;
	private ScheduleService scheduleService;
	
	@Autowired
	public EmployeeResource(EmployeeService employeeService, PhoneNumberService phoneNumberService,
			LicenseService licensesService, TestOccurrenceService testOccurrenceService,
			ScheduleService scheduleService) {
		this.employeeService = employeeService;
		this.phoneNumberService = phoneNumberService;
		this.licensesService = licensesService;
		this.testOccurrenceService = testOccurrenceService;
		this.scheduleService = scheduleService;
	}
	
	@PostMapping("/employees")
	public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) throws URISyntaxException {
		log.debug("REST request to create an employee : {}", employee);
		if (employee.getId() != null) {
			return ResponseEntity.badRequest().headers(
					HeaderUtil.createFailureAlert("employee", "idexists", "A new employee cannot already have an ID")).body(null);
		}
		Employee result = employeeService.create(employee);
		return ResponseEntity.created(new URI("/api/employees/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("employee", result.getId().toString()))
				.body(result);
	}
	
	@PutMapping("/employees")
	public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody Employee employee) throws URISyntaxException{
		log.debug("REST request to update an employee : {}", employee);
		if(employee.getId() == null){
			return createEmployee(employee);
		}
		Employee result = employeeService.create(employee);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert("employee", employee.getId().toString()))
				.body(result);
	}
	
	@DeleteMapping("employees/{id}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){
		log.debug("REST request to delete an employee");
		if(employeeService.findOne(id) == null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		employeeService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employee", id.toString())).build();
	}
	
	@GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        log.debug("REST request to get an employee : {}", id);
        Employee employee = employeeService.findOne(id);
        return Optional.ofNullable(employee)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))					
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
	
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Employees");
        Page<Employee> page = employeeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/employees");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    @GetMapping("/employees/{id}/phone-numbers")
    public ResponseEntity<List<PhoneNumber>> getPhoneNumbers(@PathVariable Long id){
    	log.debug("REST request to get phone numbers for employee with id : {}", id);
    	List<PhoneNumber> phoneNumbers = phoneNumberService.findAllByEmployee(id);
    	return (!phoneNumbers.isEmpty()) 
    			? new ResponseEntity<>(phoneNumbers, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/employees/{id}/licenses")
    public ResponseEntity<List<License>> getLicenses(@PathVariable Long id){
    	log.debug("REST request to get licenses for employee with id : {}", id);
    	List<License> licenses = licensesService.findAllByEmployee(id);
    	return (!licenses.isEmpty())
    			? new ResponseEntity<>(licenses, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/employees/{id}/tests")
    public ResponseEntity<List<TestOccurrence>> getTests(@PathVariable Long id){
    	log.debug("REST request to get tests for employee with id : {}", id);
    	List<TestOccurrence> tests = testOccurrenceService.findAllByEmployee(id);
    	return (!tests.isEmpty())
    			? new ResponseEntity<>(tests, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/employees/{id}/schedules")
    public ResponseEntity<List<Schedule>> getSchedules(@PathVariable Long id){
    	log.debug("REST request to get schedules for employee with id : {}", id);
    	List<Schedule> schedules = scheduleService.findAllByEmployee(id);
    	return (!schedules.isEmpty())
    			? new ResponseEntity<>(schedules, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    
    /**
     * GET  /tests/search : get employees whose names start with 'query'
     *
     * @param query the query used to retrieve employees
     * @return the ResponseEntity with status 200 (OK) and with body the test
     */
    @GetMapping("/employees/search")
    public ResponseEntity<List<Employee>> search(@RequestParam String query) {
        log.info("REST request to get Employees : {}", query);
        List<Employee> tests = employeeService.search(query);
        return  new ResponseEntity<>(tests, HttpStatus.OK);
    }
	
}
