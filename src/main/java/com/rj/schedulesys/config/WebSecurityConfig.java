package com.rj.schedulesys.config;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)// allows @PreAuthorize, @PostAuthorize, ...
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	 @Override
	 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.inMemoryAuthentication().withUser("user").password("secret").roles("USER");
	 }
	 
	 @Bean
	 @Override
	 public AuthenticationManager authenticationManagerBean()
			 throws Exception {
		 return super.authenticationManagerBean();
	 }
	 
	 @Bean
	 public WebMvcConfigurer corsConfigurer() {
		 
		 return new WebMvcConfigurerAdapter() {
			 @Override
			 public void addCorsMappings(CorsRegistry registry) {
				 registry.addMapping("/api/**");
			 }
		 };
	 }
	 
	 @Bean
		public DozerBeanMapper dozerBeanMapper(){
		 List<String> mappingFiles = new ArrayList<String>();
		 mappingFiles.add("dozer/dozer-config.xml");
		 DozerBeanMapper bm = new DozerBeanMapper(mappingFiles);
		 return bm;
	 }
	 
		@Bean
		public ValidatorFactory validatorFactory(){
			return Validation.buildDefaultValidatorFactory();
		}

}