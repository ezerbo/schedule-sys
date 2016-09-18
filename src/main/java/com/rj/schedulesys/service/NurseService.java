package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.dao.EmployeeDao;
import com.rj.schedulesys.dao.NurseDao;
import com.rj.schedulesys.dao.PositionDao;
import com.rj.schedulesys.data.PositionTypeConstants;
import com.rj.schedulesys.domain.Employee;
import com.rj.schedulesys.domain.Nurse;
import com.rj.schedulesys.domain.Position;
import com.rj.schedulesys.service.util.PhoneNumberUtil;
import com.rj.schedulesys.view.model.EmployeeViewModel;
import com.rj.schedulesys.view.model.NurseViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NurseService {

	private NurseDao nurseDao;
	private EmployeeDao employeeDao;
	private PositionDao positionDao;
	
	private DozerBeanMapper dozerMapper;
	
	@Autowired
	public NurseService(NurseDao nurseDao, EmployeeDao employeeDao,
			PositionDao positionDao, DozerBeanMapper dozerMapper) {
		this.nurseDao = nurseDao;
		this.employeeDao = employeeDao;
		this.positionDao = positionDao;
		this.dozerMapper = dozerMapper;
	}
	
	/**
	 * @param viewModel
	 * @return
	 */
	@Transactional
	public NurseViewModel create(EmployeeViewModel viewModel){
		Employee employee = employeeDao.findOne(viewModel.getId());
		Nurse nurse = Nurse.builder()
				.employee(employee)
				.build();
		nurseDao.merge(nurse);
		return dozerMapper.map(nurse, NurseViewModel.class);
	}
	
	/**
	 * @param id
	 */
	@Transactional
	public void delete(Long id){
		log.debug("Deleting nurse with id : {}", id);
		Nurse nurse = nurseDao.findOne(id);
		if(nurse == null){
			log.error("No nurse found with id : {}", id);
			throw new RuntimeException("No nurse found with id : " + id);
		}
		employeeDao.delete(nurse.getEmployee());
		log.debug("Nurse with id : {} successfully deleted");
	}
	
	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public NurseViewModel findOne(Long id){
		log.debug("Fetching nurse with id : {}", id);
		Nurse nurse = nurseDao.findOne(id);
		NurseViewModel viewModel = null;
		if(nurse == null){
			log.warn("No nurse found with id : {}", id);
		}else{
			Employee employee = nurse.getEmployee();
			viewModel = dozerMapper.map(employee, NurseViewModel.class);
			viewModel.setPhoneNumbers(PhoneNumberUtil.convert(
					employee.getPhoneNumbers(), dozerMapper));
		}
		return viewModel;
	}
	
	/**
	 * @return
	 */
	@Transactional
	public List<NurseViewModel> findAll(){
		log.debug("Fetching all nurses");
		List<Nurse> nurses = nurseDao.findAll();
		List<NurseViewModel> viewModels = new LinkedList<>();
		for(Nurse nurse : nurses){
			Employee employee = nurse.getEmployee();
			NurseViewModel viewModel = dozerMapper.map(employee, NurseViewModel.class);
			viewModel.setPhoneNumbers(PhoneNumberUtil.convert(
					employee.getPhoneNumbers(), dozerMapper));
			viewModels.add(viewModel);
		}
		log.debug("Nurses : {}", viewModels);
		return viewModels;
	}
	
	/**
	 * @param positionName
	 * @return
	 */
	@Transactional
	public List<NurseViewModel> findAllByPosition(String positionName){
		if(positionDao.findByName(positionName) == null){
			log.error("No position found with name : {}", positionName);
			throw new RuntimeException("No position found with name : " + positionName);
		}
		log.debug("Fetching all nurses with position : {}", positionName);
		List<Nurse> nurses = nurseDao.findAllByPosition(positionName);
		List<NurseViewModel> viewModels = new LinkedList<>();
		for(Nurse nurse : nurses){
			Employee employee = nurse.getEmployee();
			NurseViewModel viewModel = dozerMapper.map(employee, NurseViewModel.class);
			viewModel.setPhoneNumbers(PhoneNumberUtil.convert(employee.getPhoneNumbers(), dozerMapper));
			viewModels.add(viewModel);
		}
		log.debug("Nurses : {}", viewModels);
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
				, PositionTypeConstants.NURSE_POSITION)){
			log.error("No such position : {} for nurses", positionName);
			throw new RuntimeException("No such position : " + positionName + " for nurses");
		}
		return position;
	}
	
}