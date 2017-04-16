package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.EmployeeType;

public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, Long> {
	
	public EmployeeType findByName(String name);

}
