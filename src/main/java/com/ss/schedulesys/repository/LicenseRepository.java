package com.ss.schedulesys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.schedulesys.domain.License;

public interface LicenseRepository extends JpaRepository<License, Long> {

	public License findByNumber(String number);
	
	@Query("from License l where l.employee.id = :employeeId")
	public List<License> findByEmployeeId(@Param("employeeId") Long employeeId);
}
