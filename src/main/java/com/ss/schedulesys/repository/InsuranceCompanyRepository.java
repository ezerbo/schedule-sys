package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.InsuranceCompany;

public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany, Long> {

	public InsuranceCompany findByName(String name);
}
