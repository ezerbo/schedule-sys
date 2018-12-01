package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.CareCompanyType;

public interface CareCompanyTypeRepository extends JpaRepository<CareCompanyType, Long> {
	
	CareCompanyType findOneByName(String name);

}
