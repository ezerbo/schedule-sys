package com.rj.schedulesys.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

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
public class EmployeeControllerTest {

	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_findAllSchedule_WithDates_ForEmployee_WithID_4() throws Exception{
		mockMvc.perform(get("/employees/{id}/schedules", 4)
				.param("startDate", new Date().toString())
				.param("endDate", new Date().toString())
				)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)));
	}
	
	@Test
	public void test_findAllSchedule_WithDates_ForEmployee_WithID_5() throws Exception{
		mockMvc.perform(get("/employees/{id}/schedules", 5)
				.param("startDate", new Date().toString())
				.param("endDate", new Date().toString())
				)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)));
	}
	
	@Test
	public void test_findOne() throws Exception{
		mockMvc.perform(get("/employees/{id}", 1))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName", is("Boureima Edouard")))
			.andExpect(jsonPath("$.lastName", is("ZERBO")))
			.andExpect(jsonPath("$.positionName", is("RN")))
			.andExpect(jsonPath("$.comment", is("COMMENT ON EMPLOYEE WITH ID 1")));
	}
}