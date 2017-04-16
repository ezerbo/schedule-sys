package com.ss.schedulesys.config;

import javax.sql.DataSource;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import liquibase.integration.spring.SpringLiquibase;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ezerbo
 *
 */
@Slf4j
@Configuration
@EntityScan("com.ss.schedulesys.domain")
@EnableConfigurationProperties({LiquibaseProperties.class})
@EnableJpaRepositories(basePackages = {"com.ss.schedulesys.repository"})
public class DatabaseConfiguration {
	
	@Autowired
	private Environment env;

	/**
	 * @param dataSource
	 * @param liquibaseProperties
	 * @return liquibase configuration bean
	 */
	@Bean
    public SpringLiquibase liquibase(DataSource dataSource, LiquibaseProperties liquibaseProperties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:config/liquibase/master.xml");
        liquibase.setContexts(liquibaseProperties.getContexts());
        liquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        liquibase.setDropFirst(liquibaseProperties.isDropFirst());
        if (env.acceptsProfiles(Constants.LIQUIBASE_PROFILE)) {
        	liquibase.setShouldRun(liquibaseProperties.isEnabled());
        	log.debug("Configuring Liquibase");
        } else {
        	liquibase.setShouldRun(false);
        }
        return liquibase;
    }
	
	/**
	 * Create registration bean for H2 console's servlet
	 * @return H2 console's registration bean
	 */
	@Bean
	@Profile(Constants.DEV_PROFILE)
	public ServletRegistrationBean h2ServletRegistrationBean(){
		ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/h2-console/*");
        return registrationBean;
	}
}