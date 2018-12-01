package com.ss.schedulesys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.EmployeeType;
import com.ss.schedulesys.repository.EmployeeTypeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class EmployeeTypeService {

	private EmployeeTypeRepository employeeTypeRepository;
	
	@Autowired
	public EmployeeTypeService(EmployeeTypeRepository employeeTypeRepository) {
		this.employeeTypeRepository = employeeTypeRepository;
	}

	/**
	 * Get an EmployeeType using its name
	 * 
	 * @param name
	 * @return An EmployeeType
	 */
	@Transactional(readOnly = true)
	public EmployeeType findByName(String name){
		log.debug("Request to get EmployeeType with name : {}", name);
		EmployeeType employeeType = employeeTypeRepository.findByName(name);
		return employeeType;
	}
	
	/**
	 *  Get an EmployeeType using its ID
	 *  
	 * @param id
	 * @return An EmployeeType
	 */
	@Transactional(readOnly = true)
	public EmployeeType findOne(Long id){
		log.debug("Request to get EmployeeType with id : {}", id);
		EmployeeType employeeType = employeeTypeRepository.findOne(id);
		return employeeType;
	}
	
	/**
	 *  Get all employee types
	 *  
	 * @return employees
	 */
	@Transactional(readOnly = true)
	public List<EmployeeType> findAll(){
		log.debug("Request to get all employee types");
		List<EmployeeType> employeeTypes = employeeTypeRepository.findAll();
		return employeeTypes;
	}
	
}