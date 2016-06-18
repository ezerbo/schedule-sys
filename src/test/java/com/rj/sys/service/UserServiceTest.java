package com.rj.sys.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rj.sys.config.TestConfiguration;
import com.rj.sys.data.UserRole;
import com.rj.sys.view.model.ScheduleSysUserViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class UserServiceTest {
	
	public @Autowired UserService userService;
	
	@Test
	public void test_findByUsername_WithExistingUser(){
		ScheduleSysUserViewModel viewModel = userService.findByUsername("ezerbo");
		assertEquals(viewModel.getUserRole(), UserRole.ADMIN_ROLE);
	}
	
	@Test
	public void test_findByUsername_WithNonExistingUser(){
		ScheduleSysUserViewModel viewModel = userService.findByUsername("This user does not exist");
		assertNull(viewModel);
	}
	
	@Test
	public void test_findAllByRole_WithADMIN_Role_Returns_03_Users(){
		List<ScheduleSysUserViewModel> users = userService.findAllByRole(UserRole.ADMIN_ROLE);
		assertEquals(users.size(), 3);
	}
	
	@Test
	public void test_findAllbyRole_WithSUPERVISOR_Role_Returns_07_Users(){
		List<ScheduleSysUserViewModel> users = userService.findAllByRole(UserRole.SUPERVISOR_ROLE);
		assertEquals(users.size() , 7);
	}
	
	@Test
	public void test_findAll_Returns_10_Users(){
		List<ScheduleSysUserViewModel> users = userService.findAll();
		assertEquals(users.size(), 10);
	}
	
	@Test
	public void test_findOne_WithExistingId(){
		ScheduleSysUserViewModel viewModel = userService.findOne(1L);
		assertEquals(viewModel.getUsername(), "ezerbo");
	}
	
	@Test
	public void test_findOne_WithNonExistingId(){
		ScheduleSysUserViewModel viewModel = userService.findOne(0L);
		assertNull(viewModel);
	}
	
	@Test(expected = RuntimeException.class)
	public void test_createOrUpdate_WithExistingRole_ThrowsException(){
		ScheduleSysUserViewModel viewModel = ScheduleSysUserViewModel.builder()
				.username("ezerbo-test")
				.userRole("This role does not exist")
				.password("random password")
				.build();
		userService.createOrUpdate(viewModel);
	}
	
	@Test(expected = RuntimeException.class)
	public void test_createOrUpdate_WithNonExistingUser_ThrowsException(){
		ScheduleSysUserViewModel viewModel = ScheduleSysUserViewModel.builder()
				.username("ezerbo")
				.userRole(UserRole.ADMIN_ROLE)
				.build();
		userService.createOrUpdate(viewModel);
	}
	
	@Test
	public void test_createOrUpdate_WithExistingUserAndRole_Adds_One_User(){
		ScheduleSysUserViewModel viewModel = ScheduleSysUserViewModel.builder()
				.username("new-user")
				.userRole(UserRole.ADMIN_ROLE)
				.password("secured-password")
				.build();
		userService.createOrUpdate(viewModel);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_createOrUpdate_WithNull_ThrowsException(){
		userService.createOrUpdate(null);
	}
	
}