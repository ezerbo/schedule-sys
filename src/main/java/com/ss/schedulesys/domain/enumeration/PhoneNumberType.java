package com.ss.schedulesys.domain.enumeration;

import com.ss.schedulesys.service.errors.ScheduleSysException;

public enum PhoneNumberType {
	HOME, MOBILE;
	
	public static PhoneNumberType validate(String value){
		try {
			return valueOf(value);
		} catch (Exception e) {
			throw new ScheduleSysException(String.format("no such phone number type : %s", value));
		}
	}
}
