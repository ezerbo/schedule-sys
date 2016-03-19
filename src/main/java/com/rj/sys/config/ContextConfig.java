package com.rj.sys.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.dozer.DozerBeanMapper;
import org.hibernate.dialect.MySQLDialect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {
		 "com.rj.sys.service"
		, "com.rj.sys.dao"
		, "com.rj.sys.utils"
		, "com.rj.sys.view.controller"
		})
public class ContextConfig extends WebMvcConfigurerAdapter{
	
	@Value("${db.username}")
	private String dbUsername;
	
	@Value("${db.password}")
	private String dbPassword;
	
	@Value("${db.url}")
	private String dbUrl;
	
	@Value("${db.autoddl}")
	private String dbAutoDdl;
	
	@Value("${db.driver}")
	private String driverClassName;
	
	@Bean
	public InternalResourceViewResolver viewResolver(){
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		viewResolver.setOrder(2);
		return viewResolver;
	}
	
//	@Bean
//	public UrlBasedViewResolver tilesViewResolver(){
//		UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
//		viewResolver.setViewClass(TilesView.class);
//		viewResolver.setOrder(1);
//		return viewResolver;
//	}
//	
//	@Bean
//	public TilesConfigurer tilesConfigurer(){
//		TilesConfigurer tilesConfigurer = new TilesConfigurer();
//		tilesConfigurer.setDefinitions(
//				 "/WEB-INF/tiles-layout/tiles-config.xml"
//				,"/WEB-INF/views/**/views.xml"
//				);
//		return tilesConfigurer;
//	}
	
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
	
	@Bean
	public DataSource dataSource(){
		DriverManagerDataSource ds = new DriverManagerDataSource(
				dbUrl, dbUsername, dbPassword
				);
		ds.setDriverClassName(driverClassName);
		return ds;
	}

	@Bean
	public Map<String, Object> jpaProperties() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("hibernate.dialect", MySQLDialect.class.getName());
		return props;
	}
	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
		hibernateJpaVendorAdapter.setShowSql(false);
		hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
		return hibernateJpaVendorAdapter;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
		return new JpaTransactionManager(entityManagerFactory);
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource());
		emf.setJpaPropertyMap(this.jpaProperties());
		emf.setJpaVendorAdapter(this.jpaVendorAdapter());
		emf.setPackagesToScan("com.rj.sys.domain");
		return emf;
	}
	
	@Bean
	public static PropertyPlaceholderConfigurer placeholderConfigurer(){
		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		
		Resource[] resources = new ClassPathResource[]{
				new ClassPathResource("rj.properties")
				};
		configurer.setLocations(resources);
		configurer.setIgnoreResourceNotFound(true);
		configurer.setIgnoreUnresolvablePlaceholders(true);
		configurer.setOrder(1);
		return configurer;
	}
	
	@Bean
	public static PropertyPlaceholderConfigurer defaultPlaceholderConfigurer(){
		PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
		
		Resource[] resources = new ClassPathResource[]{
				new ClassPathResource("properties/rj_default.properties")
				};
		configurer.setLocations(resources);
		configurer.setIgnoreUnresolvablePlaceholders(true);
		configurer.setOrder(2);
		return configurer;
	}
	
	@Override
	 public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry
				.addResourceHandler("/css/**","/img/**")
				.addResourceLocations("/css/","/img/")
				;
		}
	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(false)
				.favorParameter(true)
				.parameterName("mediaType")
				.ignoreAcceptHeader(true)
				.useJaf(false)
				.defaultContentType(MediaType.APPLICATION_JSON)
				.mediaType("json", MediaType.APPLICATION_JSON)
				;
	}
	
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
}