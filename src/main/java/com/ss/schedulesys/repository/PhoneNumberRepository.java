package com.ss.schedulesys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.schedulesys.domain.PhoneNumber;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {

	@Query(value = "from PhoneNumber p where p.employee.id = :employeeId")
	public List<PhoneNumber> findAllByEmployee(@Param("employeeId") Long employeeId);

	public PhoneNumber findByNumber(String number);
	
	@Query(value = "from PhoneNumber p where p.employee.id = :employeeId and p.label = :label")
	public PhoneNumber findByEmployeeAndLabel(@Param("employeeId") Long employeeId, @Param("label") String label);
}
