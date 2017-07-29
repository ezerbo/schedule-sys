package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.LicenseType;

public interface LicenseTypeRepository extends JpaRepository<LicenseType, Long> {

	public LicenseType findByName(String name);
}
