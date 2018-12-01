package com.ss.schedulesys.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.ServiceRequest;
import com.ss.schedulesys.repository.ServiceRequestRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class ServiceRequestService {

	private ServiceRequestRepository serviceRequestRepository;
	
	public ServiceRequestService(ServiceRequestRepository serviceRequestRepository) {
		this.serviceRequestRepository = serviceRequestRepository;
	}
	
	public ServiceRequest save(ServiceRequest serviceRequest) {
		log.debug("Saving a service request : ", serviceRequest);
		return serviceRequestRepository.save(serviceRequest);
	}
	
	@Transactional(readOnly = true)
	public ServiceRequest findOne(Long id){
		log.debug("Getting service request with id : ", id);
		return serviceRequestRepository.findOne(id);
	}
	
	@Transactional(readOnly = true)
	public List<ServiceRequest> findAll() {
		log.debug("Getting all service requests");
		return serviceRequestRepository.findAll();
	}
	
	public void delete(Long id) {
		log.debug("Deleting service request with ID : ", id);
		serviceRequestRepository.delete(id);
	}
}