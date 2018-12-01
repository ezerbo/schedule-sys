package com.ss.schedulesys.domain.enumeration;

import com.ss.schedulesys.service.errors.ScheduleSysException;

public enum PhoneNumberLabel {
	MOBILE, HOME;
	
	public static PhoneNumberLabel validate(String value){
		try {
			return valueOf(value);
		} catch (Exception e) {
			throw new ScheduleSysException(String.format("No such phone number label : %s", value));
		}
	}
}
