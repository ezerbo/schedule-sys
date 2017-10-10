package com.ss.schedulesys.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.Employee;
import com.ss.schedulesys.domain.EmployeeType;
import com.ss.schedulesys.domain.Position;
import com.ss.schedulesys.domain.SearchCriteria;
import com.ss.schedulesys.domain.specification.EmployeeSpecification;
import com.ss.schedulesys.repository.EmployeeRepository;
import com.ss.schedulesys.repository.EmployeeTypeRepository;
import com.ss.schedulesys.repository.PositionRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;
import com.ss.schedulesys.web.vm.EmployeeFilter;

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
			.map(result -> employeeTypeRepository.findByName(result.getName()))
			.orElseThrow(() -> new ScheduleSysException("A valid type is required to create an employee"));
		
		Position position = Optional.ofNullable(employee.getPosition())
				.map(result -> positionRepository.findByName(result.getName()))
				.orElseThrow(() -> new ScheduleSysException("A Valid position is required to create an employee"));
		
		employee.setEmployeeType(employeeType);
		employee.setPosition(position);
		employee = employeeRepository.save(employee);
		return employee;
	}
	
	@Transactional(readOnly = true)
	public Page<Employee> findAll(EmployeeFilter filter, Pageable pageable){
		log.debug("Getting all employees : {}");
		List<SearchCriteria> criterias = new LinkedList<>();
		if(filter.getFirstName() != null)
			criterias.add(new SearchCriteria("firstName", ":", filter.getFirstName()));
		if(filter.getLastName() != null)
			criterias.add(new SearchCriteria("lastName", ":", filter.getLastName()));
		if(filter.getEmployeeTypeName() != null)
			criterias.add(new SearchCriteria("employeeType", ":", filter.getEmployeeTypeName()));
		if(filter.getPositionName() != null)
			criterias.add(new SearchCriteria("position", ":", filter.getPositionName()));
		
		Page<Employee> employees = null;
		if(criterias.isEmpty()){
			employees = employeeRepository.findAll(pageable);
		}else{
			Specifications<Employee> specifications = null;
			for (int i = 0; i < criterias.size(); i++) {
				if(i == 0) {
					specifications = Specifications.where(new EmployeeSpecification(criterias.get(0)));
				} else {
					specifications.and(new EmployeeSpecification(criterias.get(i)));
				}
			}
			employees = employeeRepository.findAll(specifications, pageable);
		}
		
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

	public List<Employee> search(String query) {
		log.debug("Request to get Employees with query : {}", query);
		List<Employee> employees = employeeRepository.searchByFirstAndLastNames(query);
		return employees;
	}
	 
//	 @Transactional(readOnly = true)
//	 public Page<Employee> search(String query, Pageable pageable) {
//		 log.debug("Request to search for a page of Employees for query {}", query);
//		 Page<Employee> result = employeeSearchRepository.search(queryStringQuery(query), pageable);
//		 return result;
//	 }
}
