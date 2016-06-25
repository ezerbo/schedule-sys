package com.rj.schedulesys.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
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

import com.rj.schedulesys.config.TestConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class PositionTypeControllerTest {

	private @Autowired WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_findAll() throws Exception{
		mockMvc.perform(get("/position-types"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)));
	}
	
	@Test
	public void test_findOne_WithNonExisting() throws Exception{
		mockMvc.perform(get("/position-types/{id}", 0L))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No position type found with id or name : 0")));
	}
	
	@Test
	public void test_findOne_WithExistingPosition() throws Exception{
		mockMvc.perform(get("/position-types/{id}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is("NURSE")));
	}
	
	
	@Test
	public void test_findAllPositions_WithNonExistingId() throws Exception{
		mockMvc.perform(get("/position-types/{id}/positions", 0L))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No position type found with id : 0")));
	}
	
	@Test
	public void test_findAllPositions_WithExistingId() throws Exception{
		mockMvc.perform(get("/position-types/{id}/positions", 2L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)));
	}
	
}