package com.ss.schedulesys.service;
import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ss.schedulesys.domain.ScheduleSysUser;
import com.ss.schedulesys.domain.UserRole;
import com.ss.schedulesys.repository.ScheduleSysUserRepository;
import com.ss.schedulesys.repository.UserRoleRepository;
import com.ss.schedulesys.security.SecurityUtils;
import com.ss.schedulesys.service.errors.ScheduleSysException;
import com.ss.schedulesys.service.util.RandomUtil;
import com.ss.schedulesys.web.vm.UserProfileVM;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private PasswordEncoder passwordEncoder;
    private ScheduleSysUserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    
    @Autowired
    public UserService(PasswordEncoder passwordEncoder,
    		ScheduleSysUserRepository userRepository, UserRoleRepository userRoleRepository) {
    	this.passwordEncoder = passwordEncoder;
    	this.userRepository = userRepository;
    	this.userRoleRepository = userRoleRepository;
	}
    
    @Transactional(readOnly = true)
    public Optional<ScheduleSysUser> findByUsername(String username){
    	return userRepository.findOneByUsername(username);
    }
    
    @Transactional(readOnly = true)
    public Optional<ScheduleSysUser> findByEmailAddress(String email) {
    	return userRepository.findOneByEmailAddress(email);
    }

    public Optional<ScheduleSysUser> activateRegistration(String key, String password) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<ScheduleSysUser> completePasswordReset(String newPassword, String key) {
       log.info("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
            .filter(user -> {
                DateTime oneDayAgo = DateTime.now().minusHours(24);
                return user.getResetDate().isAfter(oneDayAgo);//Reset keys are valid for a day only
           })
           .map(user -> {
                user.password(passwordEncoder.encode(newPassword))
                	.resetDate(null).resetKey(null);
               return userRepository.save(user);
           });
    }

    public Optional<ScheduleSysUser> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailAddress(mail)
            .filter(ScheduleSysUser::isActivated)//Password reset can be requested for activated accounts only
            .map(user -> {
                user.resetKey(RandomUtil.generateResetKey())
                	.resetDate(DateTime.now());
                userRepository.save(user);
                return user;
            });
    }

    public ScheduleSysUser createUser(UserProfileVM user) {
    	log.debug("Creating user : {}", user);
    	 UserRole userRole = userRoleRepository.findByName(user.getRole())
         		.orElseThrow(() -> new ScheduleSysException(String.format("No such user role : %s", user.getRole())));
        ScheduleSysUser newScheduleSysUser = new ScheduleSysUser();
        newScheduleSysUser.setUsername(user.getUsername());
        // new user gets initially a generated password
        newScheduleSysUser.setFirstName(user.getFirstName());
        newScheduleSysUser.setLastName(user.getLastName());
        newScheduleSysUser.setEmailAddress(user.getEmailAddress());
        newScheduleSysUser.setUserRole(userRole);
        // new user is not active
        newScheduleSysUser.setActivated(false);
        // new user gets registration key
        newScheduleSysUser.setActivationKey(RandomUtil.generateActivationKey());
        userRepository.save(newScheduleSysUser);
        log.debug("Created Information for ScheduleSysUser: {}", newScheduleSysUser);
        return newScheduleSysUser;
    }

    public void updateUser(UserProfileVM user) {
    	Assert.notNull(user.getId(), "User ID is required");
        Optional.of(userRepository
            .findOne(user.getId()))
            .ifPresent(u -> {
                u.setUsername(user.getUsername());
                u.setFirstName(user.getFirstName());
                u.setLastName(user.getLastName());
                u.setEmailAddress(user.getEmailAddress());
                u.setActivated(user.isActivated());
                UserRole userRole = userRoleRepository.findByName(user.getRole())
                		.orElseThrow(() -> new ScheduleSysException(String.format("No such user role : %s", user.getRole())));
                u.setUserRole(userRole);
                userRepository.save(u);
                log.debug("Changed Information for ScheduleSysUser: {}", u);
            });
    }

    public void deleteUser(String login) {
        userRepository.findOneByUsername(login).ifPresent(u -> {
        	if(u.getSchedules().isEmpty() && u.getScheduleUpdates().isEmpty())
        		userRepository.delete(u);
        	else
        		throw new ScheduleSysException(
        				String.format("unable to delete user, schedule have been created by %s", login));
            log.debug("Deleted ScheduleSysUser: {}", u);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByUsername(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for ScheduleSysUser: {}", u);
        });
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedScheduleSysUsers() {
        DateTime now = DateTime.now();
        List<ScheduleSysUser> users = userRepository.findAllByActivatedIsFalseAndCreateDateBefore(now.minusDays(3));
        for (ScheduleSysUser user : users) {
            log.debug("Deleting not activated user {}", user.getUsername());
            userRepository.delete(user);
        }
    }

}