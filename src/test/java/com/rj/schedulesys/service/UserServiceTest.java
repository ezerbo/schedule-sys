package com.rj.schedulesys.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rj.schedulesys.config.TestConfiguration;
import com.rj.schedulesys.dao.ScheduleSysUserDao;
import com.rj.schedulesys.data.UserRole;
import com.rj.schedulesys.service.UserService;
import com.rj.schedulesys.util.PasswordHashUtil;
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.ScheduleSysUserViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class UserServiceTest {
	
	private @Autowired ScheduleSysUserDao userDao;
	public @Autowired UserService userService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
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
	public void test_create_WithNonExistingRole_ThrowsException(){
		ScheduleSysUserViewModel viewModel = ScheduleSysUserViewModel.builder()
				.username("ezerbo-test")
				.userRole("This role does not exist")
				.password("random password")
				.build();
		userService.create(viewModel);
	}
	
	@Test(expected = RuntimeException.class)
	public void test_create_WithNonExistingUser_ThrowsException(){
		ScheduleSysUserViewModel viewModel = ScheduleSysUserViewModel.builder()
				.username("ezerbo")
				.userRole(UserRole.ADMIN_ROLE)
				.build();
		userService.create(viewModel);
	}

	@Test
	public void test_create_WithExistingUserAndRole_Adds_One_User() throws Exception{
		ScheduleSysUserViewModel viewModel = ScheduleSysUserViewModel.builder()
				.username("new-user")
				.userRole(UserRole.ADMIN_ROLE)
				.password("secured-password")
				.build();
		viewModel = userService.create(viewModel);
		assertNotNull(viewModel.getId());
		PasswordHashUtil.validatePassword("secured-password", userDao.findOne(viewModel.getId()).getPasswordhash());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void test_create_WithNull_ThrowsException(){
		userService.create(null);
	}
	
	@Test
	public void test_create_WithUsernameLength_LessThan_06(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("username size must be between 6 and 50");
		
		ScheduleSysUserViewModel viewModel = TestUtil.aNewScheduleSysUserViewModel(
				null, "test", "secured-password", UserRole.ADMIN_ROLE
				);
		
		userService.create(viewModel);
		
	}
	
	@Test
	public void test_create_WithUsernameLength_GreaterThan_50(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("username size must be between 6 and 50");
		
		ScheduleSysUserViewModel viewModel = TestUtil.aNewScheduleSysUserViewModel(
				null, "This is a username which length must be greater than 50 characters for"
				, "secured-password", UserRole.ADMIN_ROLE
				);
		
		userService.create(viewModel);
	}
	
	@Test
	public void test_create_WithPasswordLengthLessThan_03(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("password size must be between 3 and 50");
		
		ScheduleSysUserViewModel viewModel = TestUtil.aNewScheduleSysUserViewModel(
				null, "username"
				, "12", UserRole.ADMIN_ROLE
				);
		
		userService.create(viewModel);
		
	}
	
	@Test
	public void test_create_WithPasswordLengthGreaterThan_50(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("password size must be between 3 and 50");
		
		ScheduleSysUserViewModel viewModel = TestUtil.aNewScheduleSysUserViewModel(
				null, "somebody"
				, "12", UserRole.ADMIN_ROLE
				);
		
		userService.create(viewModel);
		
	}
	
	@Test
	public void test_update_WithNullPassWord(){
		
		ScheduleSysUserViewModel viewModel = TestUtil.aNewScheduleSysUserViewModel(
				1L, "username"
				, null, UserRole.ADMIN_ROLE
				);
		
		userService.update(viewModel);
	}
	
	@Test
	public void test_delete_WithNonExistingId(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No user found with id : 0");
		userService.delete(0L);
	}
	
	@Test
	public void test_delete_WithUserThatHasCreatedSchedules(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("User with id : 1 can not be deleted");
		userService.delete(1L);
	}
	
	@Test
	public void test_delete_WithUserThatHasCreatedAndUpdatedNoSchedules(){
		userService.delete(7L);
		assertNull(userService.findOne(7L));
	}
	
}