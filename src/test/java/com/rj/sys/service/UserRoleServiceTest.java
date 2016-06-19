package com.rj.sys.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rj.schedulesys.data.UserRole;
import com.rj.schedulesys.service.UserRoleService;
import com.rj.schedulesys.view.model.UserRoleViewModel;
import com.rj.sys.config.TestConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
public class UserRoleServiceTest {
	
	private @Autowired UserRoleService userRoleService;
	
	@Test
	public void test_findByRole_WithNonExistingRole(){
		UserRoleViewModel viewModel = userRoleService.findByRole("This role does not exist");
		assertNull(viewModel);
	}
	
	@Test
	public void test_findByRole_WithExistingRole(){
		UserRoleViewModel viewModel = userRoleService.findByRole(UserRole.ADMIN_ROLE);
		assertEquals(viewModel.getId(), Long.valueOf(1));
	}
	
	@Test
	public void test_findById_WithExistingRole(){
		UserRoleViewModel viewModel = userRoleService.findById(1L);
		assertEquals(viewModel.getUserRole(), UserRole.ADMIN_ROLE);
	}
	
	@Test
	public void test_findById_WithNonExistingRole(){
		UserRoleViewModel viewModel = userRoleService.findById(0L);
		assertNull(viewModel);
	}
	
	@Test
	public void test_FindAll(){
		List<UserRoleViewModel> userRoles = userRoleService.findAll();
		assertEquals(userRoles.size(), 2);
	}
	
}
