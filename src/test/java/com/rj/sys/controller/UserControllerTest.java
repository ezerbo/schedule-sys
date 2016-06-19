package com.rj.sys.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.rj.schedulesys.data.UserRole;
import com.rj.schedulesys.view.model.ScheduleSysUserViewModel;
import com.rj.sys.config.TestConfiguration;
import com.rj.sys.util.TestUtil;

@Transactional
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
			.andExpect(jsonPath("$", is("A user with username 'ezerbo' already exists")));
		
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
	
	@Test
	public void test_update_WithNonExistingUserId() throws IOException, Exception{
		
		ScheduleSysUserViewModel viewModel = TestUtil.aNewScheduleSysUserViewModel(
				0L, "new-user", "secured-one", UserRole.ADMIN_ROLE
				);
		
		mockMvc.perform(put("/users/{id}", 0)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void test_update_WithExistingUserIdAndNewUsername() throws IOException, Exception{
		
		ScheduleSysUserViewModel viewModel = TestUtil.aNewScheduleSysUserViewModel(
				1L, "new-username", "secured-one", UserRole.ADMIN_ROLE
				);
		
		mockMvc.perform(put("/users/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is("User successfully updated")));
	}
	
	@Test
	public void test_delete_WithNonExistingUserId() throws IOException, Exception{
		
		ScheduleSysUserViewModel viewModel = TestUtil.aNewScheduleSysUserViewModel(
				0L, "new-username", "secured-one", UserRole.ADMIN_ROLE
				);
		
		mockMvc.perform(delete("/users/{id}", 0)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void test_delete_WithUserThatHasCreatedASchedule() throws IOException, Exception{
		
		ScheduleSysUserViewModel viewModel = TestUtil.aNewScheduleSysUserViewModel(
				1L, "ezerbo", "secured-one", UserRole.ADMIN_ROLE
				);
		
		mockMvc.perform(delete("/users/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().is5xxServerError());
	}
	
	@Test
	public void test_delete_WithUserThatHasCreatedOrUpdatedNoSchedules() throws IOException, Exception{
		
		ScheduleSysUserViewModel viewModel = TestUtil.aNewScheduleSysUserViewModel(
				7L, "szerbo", "secured-one", UserRole.ADMIN_ROLE
				);
		
		mockMvc.perform(delete("/users/{id}", 7)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is("User successfully deleted")));
	}
	
	
}
