package com.ss.schedulesys.config;

import javax.sql.DataSource;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("cloud")
@Configuration
public class CloudDataSourceConfiguration extends AbstractCloudConfig{
	
	@Bean
	public DataSource cloudDataSource() {
		return connectionFactory().dataSource();
	}
}
