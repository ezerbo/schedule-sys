package com.ss.schedulesys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.schedulesys.domain.CompanyContact;

/**
 * Spring Data JPA repository for the Contact entity.
 */
public interface CompanyContactRepository extends JpaRepository<CompanyContact,Long> {
	
	public CompanyContact findByPhoneNumber(String phoneNumber);
	
	@Query("from CompanyContact cc where cc.id = :employeeId")
	public List<CompanyContact> getAllByEmployee(@Param("employeeId") Long employeeId);
}