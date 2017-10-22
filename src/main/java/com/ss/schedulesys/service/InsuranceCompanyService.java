package com.ss.schedulesys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.InsuranceCompany;
import com.ss.schedulesys.repository.InsuranceCompanyRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class InsuranceCompanyService {

	private InsuranceCompanyRepository insuranceCompanyRepository;
	
	public InsuranceCompanyService(InsuranceCompanyRepository insuranceCompanyRepository) {
		this.insuranceCompanyRepository = insuranceCompanyRepository;
	}
	
	public InsuranceCompany save(InsuranceCompany insuranceCompany) {
		log.debug("Saving a new Insurance Company : " + insuranceCompany);
		return insuranceCompanyRepository.save(insuranceCompany);
	}
	
	@Transactional(readOnly = true)
	public List<InsuranceCompany> getAll() {
		log.debug("Getting all insurance companies");
		return insuranceCompanyRepository.findAll();
	}
	
	public void delete(Long id) {
		log.debug("Deleting insurance company with ID : ", id);
		insuranceCompanyRepository.delete(id);
	}
}
