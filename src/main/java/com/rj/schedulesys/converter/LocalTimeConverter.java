package com.rj.schedulesys.converter;

import java.sql.Time;
import java.time.LocalTime;

import org.dozer.CustomConverter;

public class LocalTimeConverter implements CustomConverter {

	@Override
	public Object convert(Object destination, Object source
			, Class<?> destClass, Class<?> sourceClass) {
		
		if(destClass == LocalTime.class){
			return ((Time)source).toLocalTime();
		}else if(destClass == Time.class){
			return Time.valueOf((LocalTime)source);
		}
		
		return null;
		
	}

}
