package com.rj.schedulesys.util;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.Assert;

import com.rj.schedulesys.domain.Employee;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ServiceHelper {
	
	private ServiceHelper(){
		//Utility class, should not be instanciated
	}
	
	public static String [] getFirstAndLastNames(String formattedName){
		if(!formattedName.contains(",")){
			throw new RuntimeException(formattedName + " is not well formatted, example : firstname,lastname");
		}
		return formattedName.split(",");
	}
	
	public static String formatFirstAndLastNames(Employee user){
		String filledBy = new StringBuilder()
							.append(user.getFirstName())
							.append(",")
							.append(user.getLastName())
							.toString();
		return filledBy;
	}
	
	public static void logAndThrowException(String message){
		log.error(message);
		throw new RuntimeException(message);
	}
	
	public static String formatLocalTime(LocalTime localTime){
		Assert.notNull(localTime, "No time provided");
		DateTimeFormatter formatter = DateTimeFormat.forPattern("HH:mm");
		return localTime.toString(formatter);
	}
}
