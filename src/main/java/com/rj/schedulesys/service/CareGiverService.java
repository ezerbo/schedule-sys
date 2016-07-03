package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.dao.CareGiverDao;
import com.rj.schedulesys.dao.EmployeeDao;
import com.rj.schedulesys.dao.PositionDao;
import com.rj.schedulesys.data.PositionTypeConstants;
import com.rj.schedulesys.domain.CareGiver;
import com.rj.schedulesys.domain.Employee;
import com.rj.schedulesys.domain.Position;
import com.rj.schedulesys.service.util.PhoneNumberUtil;
import com.rj.schedulesys.view.model.EmployeeViewModel;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CareGiverService {
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private PositionDao positionDao;

	@Autowired
	private CareGiverDao careGiverDao;
	
	@Autowired
	private DozerBeanMapper dozerMapper	;
	
	@Transactional
	public EmployeeViewModel create(EmployeeViewModel viewModel){
		
		Assert.notNull(viewModel, "No caregiver provided");
		
		Employee employee = employeeDao.findOne(viewModel.getId());
		
		Assert.notNull(employee, "No employee found with id : " + viewModel.getId());
		
		log.info("Employee id : {}", viewModel.getId());
		
		CareGiver careGiver = CareGiver.builder()
				.employee(employee)
				.build();
		careGiverDao.merge(careGiver);
		
		return dozerMapper.map(employee, EmployeeViewModel.class);
	}
	
	/**
	 * @param id
	 */
	@Transactional
	public void delete(Long id){
		
		log.debug("Deleting care giver with id : {}", id);
		
		CareGiver careGiver = careGiverDao.findOne(id);
		
		if(careGiver == null){
			log.error("No care giver found with id : {}", id);
			throw new RuntimeException("No care giver found with id : " + id);
		}
		
		employeeDao.delete(careGiver.getEmployee());
		
		log.debug("Care giver with id : {} successfully deleted");
	}

	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public EmployeeViewModel findOne(Long id){
		
		log.debug("Fetching care giver with id : {}", id);
		
		CareGiver careGiver = careGiverDao.findOne(id);
		EmployeeViewModel viewModel = null;
		
		if(careGiver == null){
			log.warn("No care giver found with id : {}", id);
		}else{
			Employee employee = careGiver.getEmployee();
			viewModel = dozerMapper.map(employee, EmployeeViewModel.class);
			viewModel.setPhoneNumbers(PhoneNumberUtil.convert(employee.getPhoneNumbers(), dozerMapper));
		}
		
		return viewModel;
	}
	
	/**
	 * @return
	 */
	@Transactional
	public List<EmployeeViewModel> findAll(){
		
		log.debug("Fetching all care givers");
		
		List<CareGiver> careGivers = careGiverDao.findAll();
		List<EmployeeViewModel> viewModels = new LinkedList<>();
		
		for(CareGiver careGiver : careGivers){
			Employee employee = careGiver.getEmployee();
			EmployeeViewModel viewModel = dozerMapper.map(employee, EmployeeViewModel.class);
			viewModel.setPhoneNumbers(PhoneNumberUtil.convert(employee.getPhoneNumbers(), dozerMapper));
			viewModels.add(viewModel);
		}
		
		log.debug("Care givers : {}", viewModels);
		
		return viewModels;
	}
	
	/**
	 * @param positionName
	 * @return
	 */
	@Transactional
	public List<EmployeeViewModel> findAllByPosition(String positionName){
		
		if(positionDao.findByName(positionName) == null){
			log.error("No position found with name : {}", positionName);
			throw new RuntimeException("No position found with name : " + positionName);
		}
		
		log.debug("Fetching all care givers with position : {}", positionName);
		
		List<CareGiver> careGivers = careGiverDao.findAllByPosition(positionName);
		
		List<EmployeeViewModel> viewModels = new LinkedList<>();
		
		for(CareGiver careGiver : careGivers){
			Employee employee = careGiver.getEmployee();
			EmployeeViewModel viewModel = dozerMapper.map(employee, EmployeeViewModel.class);
			viewModel.setPhoneNumbers(PhoneNumberUtil.convert(employee.getPhoneNumbers(), dozerMapper));
			viewModels.add(viewModel);
		}
		
		log.debug("Care givers : {}", viewModels);
		
		return viewModels;
	}
	
	/**
	 * @param positionName
	 * @return
	 */
	public Position validatePosition(String positionName){
		
		Position position = positionDao.findByName(positionName);
		
		if(position == null){
			log.error("No position found with name : {}", positionName);
			throw new RuntimeException("No position found with name : " + positionName);
		}
		
		if(!StringUtils.equalsIgnoreCase(position.getPositionType().getName()
				, PositionTypeConstants.CARE_GIVER_POSITION)){
			log.error("No such position : {} for care givers", positionName);
			throw new RuntimeException("No such position : " + positionName + " for care givers");
		}
		
		return position;
	}
	
}
