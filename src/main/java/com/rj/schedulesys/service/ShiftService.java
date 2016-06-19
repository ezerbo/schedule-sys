package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.dao.ShiftDao;
import com.rj.schedulesys.domain.Shift;
import com.rj.schedulesys.view.model.ShiftViewModel;

@Slf4j
@Service
public class ShiftService {
	
	private @Autowired ShiftDao shiftDao;
	
	private @Autowired DozerBeanMapper dozerMapper;

	@Transactional
	public List<ShiftViewModel> findAll(){
		log.info("finding all shift");
		List<Shift> shifts = shiftDao.findAll();
		List<ShiftViewModel> viewModels = new LinkedList<ShiftViewModel>();
		
		for(Shift shift : shifts){
			viewModels.add(dozerMapper.map(shift, ShiftViewModel.class));	
		}
		
		return viewModels;
	}
	
	@Transactional
	public ShiftViewModel findByName(String shiftName){
		
		log.info("Finding shift by name : {}", shiftName);
		
		ShiftViewModel viewModel = null;
		try{
			viewModel = dozerMapper.map(
					shiftDao.findByName(shiftName), ShiftViewModel.class
					) ;
		}catch(Exception nre){
			log.info("No shift found with name : {}", shiftName);
		}
		
		return viewModel;
	}
	
	@Transactional
	public ShiftViewModel findbyId(Long id){
		
		log.info("Finding shift by id : {}", id);
		
		Shift shift = shiftDao.findOne(id);
		ShiftViewModel shiftViewModel = null;
		
		if(shift != null){
			shiftViewModel = dozerMapper.map(shift, ShiftViewModel.class);
		}
		return shiftViewModel;
	}
}
