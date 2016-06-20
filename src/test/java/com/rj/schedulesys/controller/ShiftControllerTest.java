package com.rj.schedulesys.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.time.LocalTime;

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

import com.rj.schedulesys.config.TestConfiguration;
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.ShiftViewModel;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class ShiftControllerTest {

private @Autowired WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_findAll() throws Exception{
		mockMvc.perform(get("/shifts"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(3)));
	}
	
	@Test
	public void test_findOne_WithNonExistingShift() throws Exception{
		mockMvc.perform(get("/shifts/{id}", 0L))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No shift found with either id or name : 0")));
	}
	
	@Test
	public void test_findOne_WithExistingShiftId() throws Exception{
		mockMvc.perform(get("/shifts/{id}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.name", is("NIGHT")));
	}
	
	@Test
	public void test_findOne_WithExistingShiftName() throws Exception{
		mockMvc.perform(get("/shifts/{id}", "DAY"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(2)))
			.andExpect(jsonPath("$.name", is("DAY")));
	}
	
	@Test
	public void test_delete_WithNonExistingShift() throws Exception{
		mockMvc.perform(delete("/shifts/{id}", 0L))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No shift found with id : 0")));
	}
	
	@Test
	public void test_delete_WithExitingShift() throws Exception{
		mockMvc.perform(delete("/shifts/{id}", 3L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is("Shift successfully deleted")));
	}
	
//	@Test
//	public void test_create_WithExistingShiftName() throws Exception{
//		
//		ShiftViewModel viewModel = TestUtil.aNewShiftViewModel(
//				null, "NIGHT", LocalTime.now(), LocalTime.MIDNIGHT
//				);
//		
//		mockMvc.perform(post("/shifts")
//				.contentType(MediaType.APPLICATION_JSON_UTF8)
//				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
//			.andExpect(status().is5xxServerError());
//			//.andExpect(jsonPath("$", is("")));
//	}
	
}