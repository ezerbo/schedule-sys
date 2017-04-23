package com.ss.schedulesys.security;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.ScheduleSysUser;
import com.ss.schedulesys.repository.ScheduleSysUserRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * Authenticate a user from the database.
 */
@Slf4j
@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private ScheduleSysUserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.info("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        Optional<ScheduleSysUser> userFromDatabase = userRepository.findOneByUsername(lowercaseLogin);
        return userFromDatabase.map(user -> {
            if (!user.isActivated()) {
                throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
            }
            //Had to prefix authority with ROLE_ to get this to work when jwt is parsed
            GrantedAuthority grantedAutority = new SimpleGrantedAuthority("ROLE_" + user.getUserRole().getName());
            return new User(lowercaseLogin, user.getPassword(), Arrays.asList(grantedAutority));
        }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
        "database"));
    }
}
