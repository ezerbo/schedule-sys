package com.ss.schedulesys.service;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.boot.web.servlet.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author ezerbo
 *
 */
@Component
public class App404PageRegistrar implements ErrorPageRegistrar {
	
	@Override
	public void registerErrorPages(ErrorPageRegistry registry) {
		 registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
	}
	
}