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

import com.ss.schedulesys.domain.EmployeeType;
import com.ss.schedulesys.service.EmployeeTypeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class EmployeeTypeResource {

	private EmployeeTypeService employeeTypeService;

	@Autowired
	public EmployeeTypeResource(EmployeeTypeService employeeTypeService) {
		this.employeeTypeService = employeeTypeService;
	}


//	@PostMapping("/employee-types")
//	public ResponseEntity<EmployeeType> createEmployeeType(@Valid @RequestBody EmployeeType employeeType) throws URISyntaxException {
//		log.debug("REST request to save EmployeeType : {}", employeeType);
//		if (employeeType.getId() != null) {
//			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("employeeType", "idexists", "A new employeeType cannot already have an ID")).body(null);
//		}
//		EmployeeType result = employeeTypeService.save(employeeType);
//		return ResponseEntity.created(new URI("/api/employee-types/" + result.getId()))
//				.headers(HeaderUtil.createEntityCreationAlert("employeeType", result.getId().toString()))
//				.body(result);
//	}

	/**
	 * PUT  /employee-types : Updates an existing employeeType.
	 *
	 * @param employeeType the employeeType to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated employeeType,
	 * or with status 400 (Bad Request) if the employeeType is not valid,
	 * or with status 500 (Internal Server Error) if the employeeType couldnt be updated
	 * @throws URISyntaxException if the Location URI syntax is incorrect
	 */
//	@PutMapping("/employee-types")
//	public ResponseEntity<EmployeeType> updateEmployeeType(@Valid @RequestBody EmployeeType employeeType) throws URISyntaxException {
//		log.debug("REST request to update EmployeeType : {}", employeeType);
//		if (employeeType.getId() == null) {
//			return createEmployeeType(employeeType);
//		}
//		EmployeeType result = employeeTypeService.save(employeeType);
//		return ResponseEntity.ok()
//				.headers(HeaderUtil.createEntityUpdateAlert("employeeType", employeeType.getId().toString()))
//				.body(result);
//	}

	/**
	 * GET  /employee-types : get all the employeeTypes.
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of employeeTypes in body
	 * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
	 */
	@GetMapping("/employee-types")
	public ResponseEntity<List<EmployeeType>> getAllEmployeeTypes()
			throws URISyntaxException {
		log.debug("REST request to get a page of EmployeeTypes");
		List<EmployeeType> employeeTypes = employeeTypeService.findAll();
		return new ResponseEntity<>(employeeTypes, HttpStatus.OK);
	}

	/**
	 * GET  /employee-types/:id : get the "id" employeeType.
	 *
	 * @param id the id of the employeeType to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the employeeType, or with status 404 (Not Found)
	 */
	@GetMapping("/employee-types/{id}")
	public ResponseEntity<EmployeeType> getEmployeeType(@PathVariable Long id) {
		log.debug("REST request to get EmployeeType : {}", id);
		EmployeeType employeeType = employeeTypeService.findOne(id);
		return Optional.ofNullable(employeeType)
				.map(result -> new ResponseEntity<>(
						result,
						HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE  /employee-types/:id : delete the "id" employeeType.
	 *
	 * @param id the id of the employeeType to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
//	@DeleteMapping("/employee-types/{id}")
//	public ResponseEntity<Void> deleteEmployeeType(@PathVariable Long id) {
//		log.debug("REST request to delete EmployeeType : {}", id);
//		employeeTypeService.delete(id);
//		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("employeeType", id.toString())).build();
//	}

}