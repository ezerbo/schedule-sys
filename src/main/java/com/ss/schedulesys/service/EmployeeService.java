package com.ss.schedulesys.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.Employee;
import com.ss.schedulesys.domain.EmployeeType;
import com.ss.schedulesys.domain.Position;
import com.ss.schedulesys.repository.EmployeeRepository;
import com.ss.schedulesys.repository.EmployeeTypeRepository;
import com.ss.schedulesys.repository.PositionRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class EmployeeService {
	
	private EmployeeRepository employeeRepository;
	private EmployeeTypeRepository employeeTypeRepository;
	private PositionRepository positionRepository;
	
	@Autowired
	public EmployeeService(EmployeeRepository employeeRepository,
			EmployeeTypeRepository employeeTypeRepository, PositionRepository positionRepository) {
		this.employeeRepository = employeeRepository;
		this.positionRepository = positionRepository;
		this.employeeTypeRepository = employeeTypeRepository;
	}
	
	public Employee create(Employee employee){
		log.info("Saving employee : {}", employee);
		EmployeeType employeeType = Optional.ofNullable(employee.getEmployeeType())
			.map(result -> employeeTypeRepository.findOne(result.getId()))
			.orElseThrow(() -> new ScheduleSysException("A valid type is required to create an employee"));
		
		Position position = Optional.ofNullable(employee.getPosition())
				.map(result -> positionRepository.findOne(result.getId()))
				.orElseThrow(() -> new ScheduleSysException("A Valid position is required to create an employee"));
		
		employee.setEmployeeType(employeeType);
		employee.setPosition(position);
		employee = employeeRepository.save(employee);
		return employee;
	}
	
	@Transactional(readOnly = true)
	public Page<Employee> findAll(Pageable pageable){
		log.debug("Getting all employees : {}");
		Page<Employee> employees = employeeRepository.findAll(pageable);
		return employees;
	}
	
	@Transactional(readOnly = true) 
    public Employee findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        Employee employee = employeeRepository.findOne(id);
        return employee;
    }
	
	public void delete(Long id) {
		log.debug("Request to delete Employee : {}", id);
	    employeeRepository.delete(id);
	}
	 
//	 @Transactional(readOnly = true)
//	 public Page<Employee> search(String query, Pageable pageable) {
//		 log.debug("Request to search for a page of Employees for query {}", query);
//		 Page<Employee> result = employeeSearchRepository.search(queryStringQuery(query), pageable);
//		 return result;
//	 }
}
