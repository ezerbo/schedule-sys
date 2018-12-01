package com.ss.schedulesys.service.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author ezerbo
 *
 */
public class DateUtil {

	private DateUtil(){
		
	}
	
	/**
	 * @return yesterday's date
	 */
	public static Date yesterday(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}
	
}
