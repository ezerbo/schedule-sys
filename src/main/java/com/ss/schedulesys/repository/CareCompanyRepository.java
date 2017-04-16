package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.CareCompany;

/**
 * Spring Data JPA repository for the CareCompany entity.
 */
public interface CareCompanyRepository extends JpaRepository<CareCompany,Long> {

	public CareCompany findByName(String name);
}