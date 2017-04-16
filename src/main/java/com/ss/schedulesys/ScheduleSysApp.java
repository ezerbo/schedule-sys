package com.ss.schedulesys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ezerbo
 *
 */
@SpringBootApplication(scanBasePackages =  "com.ss.schedulesys")
public class ScheduleSysApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SpringApplication(ScheduleSysApp.class)
			.run(args);
	}

}