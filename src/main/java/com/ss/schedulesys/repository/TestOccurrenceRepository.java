package com.ss.schedulesys.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.schedulesys.domain.TestOccurrence;

public interface TestOccurrenceRepository extends JpaRepository<TestOccurrence, Long> {

	@Query("from TestOccurrence to where to.employee.id = :employeeId")
	List<TestOccurrence> findAllByEmployee(@Param("employeeId") Long employeeId);
	
	@Query("from TestOccurrence to where to.test.id = :testId and to.employee.id = :employeeId"
			+ " and to.completionDate = :completionDate and to.expiryDate = :expiryDate")
	TestOccurrence findDuplicate(@Param("testId") Long testId, @Param("employeeId") Long employeeId,
			@Param("completionDate") Date completionDate, @Param("expiryDate") Date expiryDate);

}
