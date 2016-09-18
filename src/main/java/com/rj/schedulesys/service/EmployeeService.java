package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.schedulesys.dao.EmployeeDao;
import com.rj.schedulesys.dao.PhoneNumberDao;
import com.rj.schedulesys.dao.PhoneNumberLabelDao;
import com.rj.schedulesys.dao.PhoneNumberTypeDao;
import com.rj.schedulesys.dao.PositionDao;
import com.rj.schedulesys.domain.Employee;
import com.rj.schedulesys.domain.PhoneNumber;
import com.rj.schedulesys.domain.PhoneNumberLabel;
import com.rj.schedulesys.domain.PhoneNumberType;
import com.rj.schedulesys.domain.Position;
import com.rj.schedulesys.service.util.PhoneNumberUtil;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.view.model.EmployeeViewModel;
import com.rj.schedulesys.view.model.PhoneNumberViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmployeeService {

	private EmployeeDao employeeDao;
	private PositionDao positionDao;
	private PhoneNumberDao phoneNumberDao;
	private PhoneNumberTypeDao phoneNumberTypeDao;
	private PhoneNumberLabelDao phoneNumberLabelDao;
	
	private DozerBeanMapper dozerMapper;
	private ObjectValidator<EmployeeViewModel> validator;
	
	@Autowired
	public EmployeeService(EmployeeDao employeeDao, PositionDao positionDao, PhoneNumberDao phoneNumberDao,
			PhoneNumberLabelDao phoneNumberLabelDao, PhoneNumberTypeDao phoneNumberTypeDao,
			ObjectValidator<EmployeeViewModel> validator, DozerBeanMapper dozerMapper) {
		this.employeeDao = employeeDao;
		this.positionDao = positionDao;
		this.phoneNumberDao = phoneNumberDao;
		this.phoneNumberLabelDao = phoneNumberLabelDao;
		this.phoneNumberTypeDao = phoneNumberTypeDao;
		this.validator = validator;
		this.dozerMapper = dozerMapper;
	}
	
	@Transactional
	public EmployeeViewModel create(EmployeeViewModel viewModel){
		log.debug("Creating employee : {}", viewModel);
		Assert.notNull(viewModel, "No employee provided");
		Assert.notEmpty(viewModel.getPhoneNumbers(), "No phone number provided");
		validator.validate(viewModel);
		log.debug("Creating new employee : {}", viewModel);
		Position position = this.validatePosition(viewModel.getPositionName());
		//At least the primary phone number should be provided
		PhoneNumberUtil.assertPrimaryPhoneNumberExist(viewModel.getPhoneNumbers());
		//Make sure two phone numbers do not have the same label
		PhoneNumberUtil.assertNoDuplicateLabelExist(viewModel.getPhoneNumbers());
		List<PhoneNumber> phoneNumbers = new LinkedList<>();
		for(PhoneNumberViewModel vm : viewModel.getPhoneNumbers()){
			PhoneNumber phoneNumber = phoneNumberDao.findByNumber(vm.getNumber());
			if(phoneNumber != null){
				log.error("Phone number : {} already exists", vm.getNumber());
				throw new RuntimeException("Phone number : " + vm.getNumber() + " already exists");
			}
			PhoneNumberLabel phoneNumberLabel = PhoneNumberUtil.validatePhoneNumberLabel(
					vm.getNumberLabel(), phoneNumberLabelDao);
			PhoneNumberType phoneNumberType = PhoneNumberUtil.validatePhoneNumberType(
					vm.getNumberType(), phoneNumberTypeDao);
			phoneNumber = PhoneNumber.builder()
					.number(vm.getNumber())
					.phoneNumberLabel(phoneNumberLabel)
					.phoneNumberType(phoneNumberType)
					.build();
			phoneNumbers.add(phoneNumber);
		}
		Employee employee = dozerMapper.map(viewModel, Employee.class);
		employee.setPosition(position);
		employee = employeeDao.merge(employee);
		
		for(PhoneNumber phoneNumber : phoneNumbers){
			phoneNumber.setEmployee(employee);
			phoneNumberDao.merge(phoneNumber);
		}
		return dozerMapper.map(employee, EmployeeViewModel.class);
	}
	
	@Transactional
	public EmployeeViewModel update(EmployeeViewModel viewModel){
		Assert.notNull(viewModel, "No nurse provided");
		Assert.notNull(viewModel.getId(), "No nurse Id provided");
		validator.validate(viewModel);
		log.debug("Creating/Updating nurse : {}", viewModel);
		Employee employee = employeeDao.findOne(viewModel.getId());
		if(employee == null){
			log.error("No nurse found with id : " + viewModel.getId());
			throw new RuntimeException("No nurse found with id : " + viewModel.getId());
		}
		Position position = this.validatePosition(viewModel.getPositionName());
		employee.setComment(viewModel.getComment());
		employee.setDateOfHire(viewModel.getDateOfHire());
		employee.setEbc(viewModel.getEbc());
		employee.setFirstName(viewModel.getFirstName());
		employee.setLastName(viewModel.getLastName());
		employee.setLastDayOfWork(viewModel.getLastDayOfWork());
		employee.setRehireDate(viewModel.getRehireDate());
		employee.setInsvc(viewModel.getInsvc());
		log.info("Phone numbers : {}", employee.getPhoneNumbers());
		employee.setPosition(position);
		employee = employeeDao.merge(employee);
		return dozerMapper.map(employee, EmployeeViewModel.class);
	}

	public Position validatePosition(String positionName){
		Position position = positionDao.findByName(positionName);
		if(position == null){
			log.error("No position found with name : {}", positionName);
			throw new RuntimeException("No position found with name : " + positionName);
		}
		return position;
	}
	
	public EmployeeViewModel findOne(Long id){
		log.debug("Fetching employee with id : {}", id);
		Employee employee  = employeeDao.findOne(id);
		log.debug("Employee found : {}", employee);
		return dozerMapper.map(employee, EmployeeViewModel.class);
	}
}