package com.ss.schedulesys.service;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class TestLocalTime {

	@Test
	public void testLocalTime(){
		LocalTime time = LocalTime.now();
		DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
		System.out.println(dateFormat.format(new Date()));
	}
	
	@Test
	public void testRegex(){
		String timePattern ="(1[012]|0?[1-9]):[0-5][0-9](\\s)?(?i)(am|pm)";
		Pattern pattern = Pattern.compile(timePattern);
		Matcher matcher = pattern.matcher("01:00AM");
		assertTrue(matcher.matches());
	}
	
	@Test
	public void testOpPrecedence(){
		System.out.println(!(true&&false));
	}
}