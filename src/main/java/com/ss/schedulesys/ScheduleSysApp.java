package com.ss.schedulesys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ss.schedulesys.config.ScheduleSysProperties;

/**
 * @author ezerbo
 *
 */
@SpringBootApplication
@EnableConfigurationProperties(value = {ScheduleSysProperties.class})
public class ScheduleSysApp {
//TODO Check for email server availability at start up
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new SpringApplication(ScheduleSysApp.class)
			.run(args);
	}

}