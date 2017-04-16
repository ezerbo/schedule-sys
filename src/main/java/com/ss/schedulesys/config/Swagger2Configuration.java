package com.ss.schedulesys.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ezerbo
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2Configuration {
	
	@Autowired
	private Environment env;

	/**
	 * url : http://localhost:8080/swagger-ui.html
	 * @return
	 */
	@Bean
	public Docket api(){
		return new 	Docket(DocumentationType.SWAGGER_2)
				.enable(env.acceptsProfiles(Constants.DEV_PROFILE))
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
}
