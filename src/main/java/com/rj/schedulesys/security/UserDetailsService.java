package com.rj.schedulesys.security;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.rj.schedulesys.domain.ScheduleSysUser;
import com.rj.schedulesys.service.UserService;

import lombok.extern.slf4j.Slf4j;


/**
 * Authenticate a user from the database.
 */
@Slf4j
@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {


    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        
    	log.debug("Authenticating {}", username);

        String lowercaseLogin = username.toLowerCase(Locale.ENGLISH);
        ScheduleSysUser loadedUser = userService.loadByUsername(username);
        
        if(loadedUser == null){
        	log.error("No user found with username : {}", username);
        	throw new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database");
        }else{
        	String userRole = loadedUser.getUserRole().getUserRole();
        	List<GrantedAuthority> grantedAuthorities = Arrays.asList(new SimpleGrantedAuthority(userRole));
        	return new User(lowercaseLogin, loadedUser.getPasswordhash(), grantedAuthorities);
        }
        
    }
}