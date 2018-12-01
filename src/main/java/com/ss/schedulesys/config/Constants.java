package com.ss.schedulesys.config;

public class Constants {

	public final static String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
	
	public final static String SHIFT_TIME_REGEX = "(1[012]|0?[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)";
	
	public static final int PASSWORD_MIN_LENGTH = 4;
	
    public static final int PASSWORD_MAX_LENGTH = 100;
}
