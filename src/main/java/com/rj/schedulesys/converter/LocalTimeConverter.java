package com.rj.schedulesys.converter;

import org.dozer.DozerConverter;
import org.joda.time.LocalTime;

public class LocalTimeConverter extends DozerConverter<LocalTime, LocalTime> {

	public LocalTimeConverter() {
		super(LocalTime.class, LocalTime.class);
	}

	@Override
	public LocalTime convertFrom(LocalTime source, LocalTime destination) {
		 
		if (source == null) {
	            return null;
		 }
		return new LocalTime(source);
	}

	@Override
	public LocalTime convertTo(LocalTime source, LocalTime destination) {
		
		if (source == null) {
            return null;
		}
		return new LocalTime(source);
	}
	
	

}
