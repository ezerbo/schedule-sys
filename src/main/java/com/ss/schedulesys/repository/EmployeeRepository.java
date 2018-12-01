package com.ss.schedulesys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.schedulesys.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>,  JpaSpecificationExecutor<Employee> {

	@Query("from Employee e where lower(concat(e.firstName, ' ', e.lastName)) like lower(concat('%', :query, '%'))")
	public List<Employee> searchByFirstAndLastNames(@Param("query") String query);
}
