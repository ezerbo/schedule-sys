package com.ss.schedulesys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.schedulesys.domain.Employee;
import com.ss.schedulesys.domain.Test;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query("from Employee e where lower(e.firstName) like lower(concat('%', :query, '%')) or lower(e.lastName) like lower(concat('%', :query, '%'))")
	public List<Employee> searchByFirstAndLastNames(@Param("query") String query);
}
