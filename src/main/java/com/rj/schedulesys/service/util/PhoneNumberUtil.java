package com.rj.schedulesys.service.util;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;

import com.rj.schedulesys.dao.PhoneNumberLabelDao;
import com.rj.schedulesys.dao.PhoneNumberTypeDao;
import com.rj.schedulesys.data.PhoneNumberLabelConstants;
import com.rj.schedulesys.domain.PhoneNumber;
import com.rj.schedulesys.domain.PhoneNumberLabel;
import com.rj.schedulesys.domain.PhoneNumberType;
import com.rj.schedulesys.view.model.PhoneNumberViewModel;

import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PhoneNumberUtil {

	/**
	 * @param phoneNumbers
	 * @return converted phone numbers
	 */
	public static List<PhoneNumberViewModel> convert(List<PhoneNumber> phoneNumbers, DozerBeanMapper dozerMapper){
		Assert.notNull(dozerMapper, "No mapper provided");
		List<PhoneNumberViewModel> viewModels = new LinkedList<>();
		for(PhoneNumber phoneNumber : phoneNumbers){
			viewModels.add(dozerMapper.map(phoneNumber, PhoneNumberViewModel.class));
		}
		return viewModels;
	}
	
	/**
	 * @param type
	 * @param phoneNumberTypeDao
	 * @return
	 */
	public static PhoneNumberType validatePhoneNumberType(String type, PhoneNumberTypeDao phoneNumberTypeDao){
		PhoneNumberType phoneNumberType = phoneNumberTypeDao.findByName(type);
		if(phoneNumberType == null){
			log.error("No such phone type : {}", type);
			throw new RuntimeException("No such phone number type : " + type);
		}
		return phoneNumberType;
	}
	
	/**
	 * @param label
	 * @param phoneNumberLabelDao
	 * @return
	 */
	public static PhoneNumberLabel validatePhoneNumberLabel(String label, PhoneNumberLabelDao phoneNumberLabelDao){
		PhoneNumberLabel phoneNumberLabel = phoneNumberLabelDao.findByName(label);
		if(phoneNumberLabel == null){
			log.error("No such phone number label : {}", label);
			throw new RuntimeException("No such phone number label : " + label);
		}
		return phoneNumberLabel;
	}
	
	/**
	 * @param viewModels
	 * @throws RuntimeException when no primary phone number is found in the list
	 */
	public static PhoneNumberViewModel assertPrimaryPhoneNumberExist(List<PhoneNumberViewModel> viewModels){
		
		for(int i = 0; i < viewModels.size(); i++){
			PhoneNumberViewModel viewModel = viewModels.get(i);
			if(StringUtils.equals(viewModel.getNumberLabel(), PhoneNumberLabelConstants.PRIMARY_LABEL)){
				return viewModel;
			}
			
			if(i == viewModels.size() -1){
				log.error("No primary phone number found");
				throw new RuntimeException("No primary phone number found");
			}
			
		}
		
		return null;
		
	}
	
	/**
	 * @param viewModels
	 */
	public static void assertNoDuplicateLabelExist(List<PhoneNumberViewModel> viewModels){
		for(int i = 0; i < viewModels.size(); i++){
			PhoneNumberViewModel iViewModel = viewModels.get(i);
			for(int j = i + 1; j < viewModels.size() -1; j++){
				PhoneNumberViewModel jViewModel = viewModels.get(j);
				if(StringUtils.equalsIgnoreCase(iViewModel.getNumberLabel(), jViewModel.getNumberLabel())){
					log.error("Duplicate phone number label : {}", jViewModel.getNumberLabel());
					throw new RuntimeException("Duplicate phone number label : " + jViewModel.getNumberLabel());
				}
			}
		}
	}
}
