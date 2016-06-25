package com.rj.schedulesys.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.rj.schedulesys.util.PasswordHashUtil;

public class PBKDF2PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		 try {
		      return PasswordHashUtil.createHash(rawPassword.toString());
		 } catch(Exception e){
			 throw new RuntimeException("An error occured while creating password hash");
		 }
		
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		try {
		      return PasswordHashUtil.validatePassword(
		    		  rawPassword.toString(), encodedPassword
		    		  );
		} catch (Exception ex) {
		      throw new RuntimeException("An error occured while validating password");
		} 
		
	}

}