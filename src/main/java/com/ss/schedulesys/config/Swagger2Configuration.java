package com.ss.schedulesys.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ss.schedulesys.config.ScheduleSysProperties.ApiInfo;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ezerbo
 *
 */
@Configuration
@EnableSwagger2
@Profile(ProfileConstants.DEV_PROFILE)
public class Swagger2Configuration {
	
	@Autowired
	private ScheduleSysProperties scheduleSysProperties;
	
	/**
	 * url : http://localhost:8080/swagger-ui.html
	 * @return docket Api documentation bean
	 */
	@Bean
	public Docket api(){
		ApiInfo ssApiInfo = scheduleSysProperties.getApiInfo();
		Contact contact = new Contact(ssApiInfo.getContact().getName(),
				ssApiInfo.getContact().getUrl(), ssApiInfo.getContact().getEmail());
		
		springfox.documentation.service.ApiInfo apiInfo = new ApiInfoBuilder()
						.title(scheduleSysProperties.getApiInfo().getTitle())
						.description(scheduleSysProperties.getApiInfo().getDescription())
						.version(scheduleSysProperties.getApiInfo().getVersion())
						.contact(contact)
						.build();
		
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
}
