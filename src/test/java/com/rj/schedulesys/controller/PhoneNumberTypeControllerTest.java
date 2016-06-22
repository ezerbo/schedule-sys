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
public class PhoneNumberTypeControllerTest {

	private MockMvc mockMvc;
	
	private @Autowired WebApplicationContext context;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_findAll() throws Exception{
		mockMvc.perform(get("/phone-number-types"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)));
	}
	
	@Test
	public void test_findOne_WithNonExistingStatus() throws Exception{
		mockMvc.perform(get("/phone-number-types/{id}", 0))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$", is("No phone number type found with either id or name : 0")));
	}
	
	@Test
	public void test_findOne_WithExistingStatus() throws Exception{
		mockMvc.perform(get("/phone-number-types/{id}", 1))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("HOME")));
	}
	
	@Test
	public void test_findOneWithNonExistingName() throws Exception{
		mockMvc.perform(get("/phone-number-types/{id}", "This name does not exist"))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$", is("No phone number type found with either id or name : This name does not exist")));
	}
	
	@Test
	public void test_findOneWithExistingName() throws Exception{
		mockMvc.perform(get("/phone-number-types/{id}", 2))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("MOBILE")));
	}
	
}
