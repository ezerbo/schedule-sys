package com.ss.schedulesys.web.rest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ss.schedulesys.config.Constants;
import com.ss.schedulesys.domain.ScheduleSysUser;
import com.ss.schedulesys.repository.ScheduleSysUserRepository;
import com.ss.schedulesys.service.MailService;
import com.ss.schedulesys.service.UserService;
import com.ss.schedulesys.service.errors.ErrorVM;
import com.ss.schedulesys.web.rest.util.HeaderUtil;
import com.ss.schedulesys.web.rest.util.PaginationUtil;

import lombok.extern.slf4j.Slf4j;


/**
 * REST controller for managing users.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class UserResource {

    private MailService mailService;
    private UserService userService;
    private ScheduleSysUserRepository userRepository;
    
    
    @Autowired
    public UserResource(ScheduleSysUserRepository userRepository,
    		MailService mailService, UserService userService) {
    	this.userRepository = userRepository;
    	this.mailService = mailService;
    	this.userService = userService;
	}

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     * </p>
     *
     * @param managedUserVM the user to create
     * @param request the HTTP request
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/users")
    //@Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<?> createUser(@RequestBody @Valid ScheduleSysUser managedUserVM, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save User : {}", managedUserVM);
        String errorMsg;
        //Lowercase the user login before comparing with database
        if (userRepository.findOneByUsername(managedUserVM.getUsername().toLowerCase()).isPresent()) {
            errorMsg = String.format("Username '%s' already in use", managedUserVM.getUsername());
        	return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "userexists", errorMsg))
                .body(ErrorVM.builder().message(errorMsg).build());
        } else if (userRepository.findOneByEmailAddress(managedUserVM.getEmailAddress()).isPresent()) {
        	errorMsg = String.format("Email address '%s' already in use", managedUserVM.getEmailAddress());
        	return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", errorMsg))
                .body(ErrorVM.builder().message(errorMsg).build());
        } else {
        	ScheduleSysUser newUser = userService.createUser(managedUserVM);
        	 String baseUrl = request.getHeader("Origin");
            mailService.sendActivationEmail(newUser, baseUrl);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getUsername()))
                .headers(HeaderUtil.createAlert( "A user is created with identifier " + newUser.getUsername(), newUser.getUsername()))
                .body(newUser);
        }
    }

    /**
     * PUT  /users : Updates an existing User.
     *
     * @param managedUserVM the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     */
    @PutMapping("/users")
    public ResponseEntity<ScheduleSysUser> updateUser(@Valid @RequestBody ScheduleSysUser managedUserVM) {
        log.debug("REST request to update User : {}", managedUserVM);
        Optional<ScheduleSysUser> existingUser = userRepository.findOneByEmailAddress(managedUserVM.getEmailAddress());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest()
            		.headers(HeaderUtil.createFailureAlert("userManagement", "emailexists",
            				String.format("E-mail '%s' already in use", managedUserVM.getEmailAddress())))
            		.body(null);
        }
        existingUser = userRepository.findOneByUsername(managedUserVM.getUsername().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest()
            		.headers(HeaderUtil.createFailureAlert("userManagement", "userexists",
            				String.format("Username '%s' already in use", managedUserVM.getUsername())))
            		.body(null);
        }
        userService.updateUser(managedUserVM);
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert("A user is updated with identifier " + managedUserVM.getUsername(), managedUserVM.getUsername()))
            .body(userRepository.findOne(managedUserVM.getId()));
    }

    /**
     * GET  /users : get all users.
     * 
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     * @throws URISyntaxException if the pagination headers couldn't be generated
     */
    @GetMapping("/users")
    //@Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<List<ScheduleSysUser>> getAllUsers(Pageable pageable)
        throws URISyntaxException {
        Page<ScheduleSysUser> page = userRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{field:" + Constants.LOGIN_REGEX + "}")
    public ResponseEntity<ScheduleSysUser> getUser(@PathVariable String field, @RequestParam(required = false) String by) {
        log.info("REST request to get User : {}", field);
        if(by == null || StringUtils.equals(by, "username"))//When 'by' not specified, then fetch user by username
	        return userService.findByUsername(field)
	                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
	                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        
        return userService.findByEmailAddress(field)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    //@Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.info("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A user is deleted with identifier " + login, login)).build();
    }
  //TODO At least one admin should be in the db at all times. Prevent API from deleting all admin users.
}