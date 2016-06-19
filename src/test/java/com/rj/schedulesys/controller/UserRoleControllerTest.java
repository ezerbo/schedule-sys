package com.rj.schedulesys.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.rj.schedulesys.data.UserRole;
import com.rj.sys.config.TestConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class UserRoleControllerTest {

	private @Autowired WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_findByIdOrRole_WithExistingId() throws Exception{
		mockMvc.perform(get("/user-roles/{idOrRole}", 1))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.userRole", is(UserRole.ADMIN_ROLE)));			
		
	}
	
	@Test
	public void test_findByIdOrRole_WithNonExistingId() throws Exception{
		mockMvc.perform(get("/user-roles/{idOrRole}", 0))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void test_findByIdOrRole_WithExistingRole() throws Exception{
		mockMvc.perform(get("/user-roles/{idOrRole}", UserRole.SUPERVISOR_ROLE))
		.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(2)))
			.andExpect(jsonPath("$.userRole", is(UserRole.SUPERVISOR_ROLE)));
	}
	
	@Test
	public void test_findByIdOrRole_WithNonExistingRole() throws Exception{ 
		mockMvc.perform(get("/user-roles/{idOrRole}", "This role does not exist"))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void test_findByAll() throws Exception{
		mockMvc.perform(get("/user-roles"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(2)));
		
	}
}