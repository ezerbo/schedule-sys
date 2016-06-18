package com.rj.sys.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.rj.sys.config.TestConfiguration;
import com.rj.sys.data.UserRole;
import com.rj.sys.util.TestUtil;
import com.rj.sys.view.model.ScheduleSysUserViewModel;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class UserControllerTest {

	private @Autowired WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_create_WithNonExistingRole() throws Exception{
		
		ScheduleSysUserViewModel viewModel = TestUtil.aNewScheduleSysUserViewModel(
				null, "new-user", "secured-one", "Non existing role"
				);
		
		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$", containsString("No such user role")));
	}
	
	@Test
	public void test_create_WithExistingUsername() throws IOException, Exception{
		
		ScheduleSysUserViewModel viewModel = TestUtil.aNewScheduleSysUserViewModel(
				null, "ezerbo", "secured-one", UserRole.SUPERVISOR_ROLE
				);
		
		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$", is("A user with username : ezerbo already exists")));
		
	}
	
	@Test
	public void test_create_WithExistingRoleAndNonExistingUsername() throws IOException, Exception{
		
		ScheduleSysUserViewModel viewModel = TestUtil.aNewScheduleSysUserViewModel(
				null, "new-user", "secured-one", UserRole.ADMIN_ROLE
				);
		
		mockMvc.perform(post("/users")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().is2xxSuccessful())
			.andExpect(jsonPath("$", is("User successfully created")));
	}
	
	
}
