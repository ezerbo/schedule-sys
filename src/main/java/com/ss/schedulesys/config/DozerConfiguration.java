package com.ss.schedulesys.config;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozerConfiguration {
	
	@Bean
	public DozerBeanMapper dozerBeanMapper(){
		DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
		dozerBeanMapper.addMapping(new CustomDozerBeanMappingBuilder());
		return dozerBeanMapper;
	}
	
	class CustomDozerBeanMappingBuilder extends BeanMappingBuilder{

		@Override
		protected void configure() {
			
		}
		
	}
}