package com.rj.schedulesys.config;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rj.schedulesys.security.AuthoritiesConstants;
import com.rj.schedulesys.security.Http401UnauthorizedEntryPoint;
import com.rj.schedulesys.security.PBKDF2PasswordEncoder;
import com.rj.schedulesys.security.jwt.JWTConfigurer;
import com.rj.schedulesys.security.jwt.TokenProvider;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)// allows @PreAuthorize, @PostAuthorize, ...
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private Http401UnauthorizedEntryPoint authenticationEntryPoint;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private TokenProvider tokenProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new PBKDF2PasswordEncoder();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("secret").roles("USER");
	}
	 
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
		.antMatchers(HttpMethod.OPTIONS, "/**")
		.antMatchers("/schedulesys/**/*.{js,html}")
		.antMatchers("/bower_components/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.exceptionHandling()
		.authenticationEntryPoint(authenticationEntryPoint)
		.and()
		.csrf()
		.disable()
		.headers()
		.frameOptions()
		.disable()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
		.antMatchers("/authenticate").permitAll()
		//	.antMatchers("/api/account/reset_password/init").permitAll()
		// 	.antMatchers("/api/account/reset_password/finish").permitAll()
		//.antMatchers("/**").authenticated()
		//.antMatchers("/users/**").hasAuthority(AuthoritiesConstants.ADMIN)
		//.antMatchers("/configuration/ui").permitAll()
		.and()
		.apply(securityConfigurerAdapter());

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

	private JWTConfigurer securityConfigurerAdapter() {
		return new JWTConfigurer(tokenProvider);
	}

}