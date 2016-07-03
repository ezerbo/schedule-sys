package com.rj.schedulesys.service;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.dao.EmployeeDao;
import com.rj.schedulesys.dao.PhoneNumberDao;
import com.rj.schedulesys.dao.PhoneNumberLabelDao;
import com.rj.schedulesys.dao.PhoneNumberTypeDao;
import com.rj.schedulesys.domain.Employee;
import com.rj.schedulesys.domain.PhoneNumber;
import com.rj.schedulesys.domain.PhoneNumberLabel;
import com.rj.schedulesys.domain.PhoneNumberType;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.view.model.PhoneNumberViewModel;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PhoneNumberService {
	
	@Autowired
	private PhoneNumberDao phoneNumberDao;
	
	@Autowired
	private PhoneNumberLabelDao phoneNumberLabelDao;
	
	@Autowired
	private PhoneNumberTypeDao phoneNumberTypeDao;
	
	@Autowired
	private DozerBeanMapper dozerMapper;
	
	@Autowired
	private ObjectValidator<PhoneNumberViewModel> validator;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	/**
	 * @param employeeId
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public PhoneNumberViewModel create(Long employeeId, PhoneNumberViewModel viewModel){
		
		Assert.notNull(viewModel, "No phone number provided");
		
		validator.validate(viewModel);
		
		log.debug("Adding new phone number : {} for employee with id : {}", viewModel, employeeId);
		
		Employee employee = validateEmployee(employeeId);
		
		PhoneNumberLabel phoneNumberLabel = validatePhoneNumberLabel(viewModel.getNumberLabel());
		
		PhoneNumberType phoneNumberType = validatePhoneNumberType(viewModel.getNumberType());
		
		PhoneNumber phoneNumber = phoneNumberDao.findByEmployeeAndLabel(
				employeeId, StringUtils.upperCase(viewModel.getNumberLabel()));
		
		if(phoneNumber != null){
			log.error("Employee with id : {} already has a {} phone number"
						, employeeId, viewModel.getNumberLabel());
			throw new RuntimeException("Employee with id : " + employeeId 
					+ " already has a " + viewModel.getNumberLabel() + " phone number");
		}
		
		if(phoneNumberDao.findByNumber(viewModel.getNumber()) != null){
			log.error("Phone number : {} is already in use", viewModel.getNumber());
			throw new RuntimeException("Phone number : " + viewModel.getNumber() + " is already in use");
		}
		
		phoneNumber = PhoneNumber.builder()
				.number(viewModel.getNumber())
				.employee(employee)
				.phoneNumberLabel(phoneNumberLabel)
				.phoneNumberType(phoneNumberType)
				.build();
		
		phoneNumber = phoneNumberDao.merge(phoneNumber);
		
		log.debug("Phone number created : {}", phoneNumber);
		
		return dozerMapper.map(phoneNumber, PhoneNumberViewModel.class);
	}
	
	/**
	 * @param employeeId
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public PhoneNumberViewModel update(Long employeeId, PhoneNumberViewModel viewModel){
		
		Assert.notNull(viewModel, "No phone number provided");
		
		Employee employee =  validateEmployee(employeeId);
		
		PhoneNumber existingNumber = phoneNumberDao.findByEmployeeIdAndNumberId(employee.getId(), viewModel.getId());
		
		PhoneNumberLabel phoneNumberLabel = validatePhoneNumberLabel(viewModel.getNumberLabel());
		
		PhoneNumberType phoneNumberType = validatePhoneNumberType(viewModel.getNumberType());
		
		if(existingNumber == null){
			log.error("No phone number found with id : {} for employee with id : {}"
					, viewModel.getId(), employeeId);
			throw new RuntimeException("No phone number found with id : " + viewModel.getId() 
				+ " for employee with id : " + employeeId);
		}
		
		if(!StringUtils.equalsIgnoreCase(existingNumber.getPhoneNumberLabel().getName(), viewModel.getNumberLabel())){
			log.warn("Phone number label updated, checking its uniqueness");
			if(phoneNumberDao.findByEmployeeAndLabel(employee.getId(), viewModel.getNumberLabel()) != null){
				log.error("Employee with id : {} already has a {} phone number", employee.getId(), viewModel.getNumberLabel());
				throw new RuntimeException("Employee with id : " + employeeId 
						+ " already has a " + viewModel.getNumberLabel() + " phone number");
			}
		}
		
		PhoneNumber phoneNumber = phoneNumberDao.findByNumber(viewModel.getNumber());
		
		if(phoneNumber != null){
			if(!phoneNumber.getEmployee().getId().equals(employeeId)){
				log.error("Phone number : {} is already in use", viewModel.getNumber());
				throw new RuntimeException("Phone number : " + viewModel.getNumber() + " is already in use");
			}
		}
		
		existingNumber.setNumber(viewModel.getNumber());
		existingNumber.setPhoneNumberLabel(phoneNumberLabel);
		existingNumber.setPhoneNumberType(phoneNumberType);
		
		phoneNumberDao.merge(existingNumber);
		
		return viewModel;
	}
	
	/**
	 * @param employeeId
	 * @param numberId
	 * @return
	 */
	public PhoneNumberViewModel findByEmployeeAndNumberId(Long employeeId, Long numberId){
		
		log.debug("Fetching phone number with id : {} for employee with id : {}", numberId, employeeId);
		
		PhoneNumber phoneNumber = phoneNumberDao.findByEmployeeIdAndNumberId(employeeId, numberId);
		
		PhoneNumberViewModel viewModel = null;
		
		if(phoneNumber == null){
			log.warn("No phoneNumber found with id : {} for employee with id : {}", numberId, employeeId);
		}else{
			viewModel = dozerMapper.map(phoneNumber, PhoneNumberViewModel.class);
		}
		
		return viewModel;
	}
	
	/**
	 * @param numberLabel
	 * @return
	 */
	public PhoneNumberLabel validatePhoneNumberLabel(String numberLabel){
		PhoneNumberLabel phoneNumberLabel = phoneNumberLabelDao.findByName(numberLabel);
		if(phoneNumberLabel == null){
			log.error("No such phone number label : {}, expected values are 'PRIMARY', 'SECONDARY' and 'OTHER'", numberLabel);
			throw new RuntimeException("No such phone number label : " + numberLabel
					+ ", expected values are 'PRIMARY', 'SECONDARY' and 'OTHER'");
		}
		return phoneNumberLabel;
	}
	
	/**
	 * @param numberType
	 * @return
	 */
	public PhoneNumberType validatePhoneNumberType(String numberType){
		PhoneNumberType phoneNumberType = phoneNumberTypeDao.findByName(numberType);
		if(phoneNumberType == null){
			log.error("No such phone number type : {}, expected values are 'HOME', 'MOBILE'" + numberType);
			throw new RuntimeException("No such phone number type : " + numberType 
				+ ", expected values are 'HOME', 'MOBILE'");
		}
		return phoneNumberType;
	}
	
	/**
	 * @param employeeId
	 * @return
	 */
	public Employee validateEmployee(Long employeeId){
		Employee employee = employeeDao.findOne(employeeId);
		
		if(employee == null){
			log.error("No employee found with id : {}", employeeId);
			throw new RuntimeException("No employee found with id : " + employeeId);
		}
		
		return employee;
	}
}
