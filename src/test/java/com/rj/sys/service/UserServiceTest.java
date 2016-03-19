package com.rj.sys.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.config.TestConfiguration;
import com.rj.sys.dao.PositionDao;
import com.rj.sys.dao.UserDao;
import com.rj.sys.dao.UserTypeDao;
import com.rj.sys.domain.Position;
import com.rj.sys.domain.User;
import com.rj.sys.domain.UserType;
import com.rj.sys.dto.UserTO;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
public class UserServiceTest {
	
	private @PersistenceContext EntityManager entityManager;
	
	private @Autowired UserService userService;
	
	private @Autowired UserDao userDao;
	private @Autowired PositionDao positionDao;
	private @Autowired UserTypeDao userTypeDao;
	
	@Test
	@Transactional
	public void createNewUser(){
		
		Position position = buildPostion();
		position = positionDao.merge(position);
		
		UserType userType = buildUserType();
		userType = userTypeDao.merge(userType);
		entityManager.flush();
		
		UserTO userToBeSaved = buildUser();
		//userToBeSaved.setPositionType(position);
		userToBeSaved.setType(userType.getType());
		//userService.createUser(userToBeSaved);
		entityManager.flush();
		User savedUser = userDao.findOneByEmailAddress(userToBeSaved.getEmailAddress());
		
		assertEquals(savedUser.getUsername(), userToBeSaved.getUsername());
	}
	
	@Transactional
	public void findAllActiveUsers(){
		@SuppressWarnings("unused")
		List<User> activeUsers = userDao.findAllActiveUsers();
	}
	
	private UserTO buildUser(){
		UserTO employee = UserTO.builder()
				.emailAddress("itenmitsurigui@kenshin.com")
				.username("himura")
				.password("secret")
				.firstName("myFirstname")
				.lastName("myLastname")
				.primaryPhoneNumber("+1-718-790-9836")
				.ebc(true)
				.cpr("mycpr")
				.build()
		;
		return employee;
	}
	
	
	private Position buildPostion(){
		Position position = Position.builder()
				.name("RN")
				//.positionType(Position.N_POSITION)
				.build()
		;
		return position;
	}
	
	private UserType buildUserType(){
		UserType userType = UserType.builder()
				.type(User.SIMPLE_USER)
				.build()
		;
		return userType;
	}
}
