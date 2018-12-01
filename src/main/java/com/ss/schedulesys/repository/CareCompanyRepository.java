package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ss.schedulesys.domain.CareCompany;

/**
 * Spring Data JPA repository for the CareCompany entity.
 */
public interface CareCompanyRepository extends JpaRepository<CareCompany,Long>, JpaSpecificationExecutor<CareCompany> {

	public CareCompany findByNameIgnoreCase(String name);
}