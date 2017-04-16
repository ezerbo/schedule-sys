package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
