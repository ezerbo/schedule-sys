package com.ss.schedulesys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.InsuranceCompany;
import com.ss.schedulesys.repository.InsuranceCompanyRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

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
		String companyName = insuranceCompany.getName();
		if(insuranceCompanyRepository.findByName(companyName) != null) {
			log.error("Insurance company name : '{}' already is use", companyName);
			throw new ScheduleSysException(String.format("Name '%s' already in use", companyName));
		}
		return insuranceCompanyRepository.save(insuranceCompany);
	}
	
	@Transactional(readOnly = true)
	public List<InsuranceCompany> findAll() {
		log.debug("Getting all insurance companies");
		return insuranceCompanyRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public InsuranceCompany findOne(Long id) {
		log.debug("Getting InsuranceCompany with ID : " + id);
		return insuranceCompanyRepository.findOne(id);
	}
	
	public void delete(Long id) {
		log.debug("Deleting insurance company with ID : ", id);
		insuranceCompanyRepository.delete(id);
	}
}
