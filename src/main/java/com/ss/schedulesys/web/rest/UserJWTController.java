package com.ss.schedulesys.web.rest;
import java.util.Collections;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.schedulesys.security.jwt.JWTConfigurer;
import com.ss.schedulesys.security.jwt.TokenProvider;
import com.ss.schedulesys.web.vm.LoginVM;

@RestController
@RequestMapping("/api")
public class UserJWTController {

    private TokenProvider tokenProvider;
    private AuthenticationManager authenticationManager;
    
    @Autowired
    public UserJWTController(TokenProvider tokenProvider,
    		AuthenticationManager authenticationManager) {
    	this.tokenProvider = tokenProvider;
    	this.authenticationManager = authenticationManager;
	}

    @PostMapping("/authenticate")
    public ResponseEntity<?> authorize(@Valid @RequestBody LoginVM loginVM, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
            response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
            return ResponseEntity.ok(new JWTToken(jwt));
        } catch (AuthenticationException exception) {
            return new ResponseEntity<>(Collections.singletonMap(
            		"AuthenticationException", exception.getLocalizedMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}