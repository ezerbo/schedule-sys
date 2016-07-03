package com.rj.schedulesys.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.web.context.WebApplicationContext;

import com.rj.schedulesys.config.TestConfiguration;
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.PositionViewModel;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class PositionControllerTest {

	private @Autowired WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_findOne_WithNonExistingPositionId() throws Exception{
		mockMvc.perform(get("/positions/{id}", 0L))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No position found with either id or name : 0")));
	}
	
	@Test
	public void test_findOne_WithExistingPositionId() throws Exception{
		mockMvc.perform(get("/positions/{id}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is("RN")));
	}
	
	@Test
	public void test_findByName_WithNonExistingPositionName() throws Exception{
		mockMvc.perform(get("/positions/{id}", "This test does not exist"))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No position found with either id or name : This test does not exist")));
	}
	
	@Test
	public void test_create_WithNonExisitingPositionType() throws Exception{
		
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(
				null, "Some name", "This position does not exist");
		
		mockMvc.perform(post("/positions")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$", is("No position type found with name : This position does not exist")));
	}
	
	@Test
	public void test_create_WithExistingPositionTypeThatAlreadyHasThePositionBeingCreated() throws Exception{
		
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(
				null, "RN", "NURSE");
		
		mockMvc.perform(post("/positions")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$", is("A position with name : RN already exists for position type with name : NURSE")));
	}
	
	@Test
	public void test_create_WithExistingPositionTypeAndNonExistingPosition() throws IOException, Exception{
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(
				null, "RN-NEW", "NURSE");
		
		mockMvc.perform(post("/positions")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$", is("Position successfully created")));
	}
	
	@Test
	public void test_update_WithNonExistingPosition() throws IOException, Exception{
		
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(
				0L, "RN-NEW", "NURSE");
		
		mockMvc.perform(put("/positions/{id}", 0L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No position found with id : 0")));
	}
	
	@Test
	public void test_update_WithExistingPositionTypeThatAlreadyHasThePositionBeingCreated() throws IOException, Exception{
		
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(
				1L, "LNP", "NURSE");
		
		mockMvc.perform(put("/positions/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$", is("A position with name : LNP already exists for position type with name : NURSE")));
	}
	
	@Test
	public void test_update_WithExistingPositionTypeAndNonExistingPosition() throws IOException, Exception{
		
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(
				3L, "NEW-NAME", "NURSE");
		
		mockMvc.perform(put("/positions/{id}", 3L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is("Position successfully updated")));
	}
	
	@Test
	public void test_delete_WithNonExistingPosition() throws IOException, Exception{
		
		mockMvc.perform(delete("/positions/{id}", 0L))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No position found with id : 0")));
	}
	
	@Test
	public void test_delete_WithExistingPositionButAssignedToEmployees() throws IOException, Exception{
		
		mockMvc.perform(delete("/positions/{id}", 11L))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$", is("Position with id : 11 can not be deleted")));
	}
	
	@Test
	public void test_delete_WithExistingPositionNotAssignedToAnyEmployees() throws IOException, Exception{
		
		mockMvc.perform(delete("/positions/{id}", 10L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is("Position successfully deleted")));
	}
	
}