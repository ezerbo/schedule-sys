package com.rj.sys.utils;

import com.rj.sys.domain.User;

public class ServiceHelper {
	
	private ServiceHelper(){
		//Utility class, should not be instanciated
	}
	
	public static String [] getFirstAndLastNames(String formattedName){
		return formattedName.split(",");
	}
	
	public static String formatFirstAndLastNames(User user){
		String filledBy = new StringBuilder()
							.append(user.getFirstName())
							.append(",")
							.append(user.getLastName())
							.toString();
		return filledBy;
	}
}
