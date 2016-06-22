package com.rj.schedulesys.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import com.rj.schedulesys.view.model.TestViewModel;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class TestControllerTest {

private @Autowired WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_findAll() throws Exception{
		mockMvc.perform(get("/tests"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(10)));
	}
	
	@Test
	public void test_findOne_WithNonExistingTestId() throws Exception{
		mockMvc.perform(get("/tests/{id}", 0))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No test found with id : 0")));
	}
	
	@Test
	public void test_findOne_WithExistingTestId() throws Exception{
		mockMvc.perform(get("/tests/{id}", 1))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("DRUG TEST")));
	}
	
	@Test
	public void test_create_WithExistingTestName() throws Exception{
		
		TestViewModel viewModel = TestUtil.aNewTestViewModel(
				null, "FLU VACCINE", false, true, true
				);
		mockMvc.perform(post("/tests")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("A test with name : FLU VACCINE already exists")));
	}
	
	@Test
	public void test_create_WithNonExistingTestName() throws Exception{
		
		TestViewModel viewModel = TestUtil.aNewTestViewModel(
				null, "A NEW FLU VACCINE", false, true, true
				);
		
		mockMvc.perform(post("/tests")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$", is("Test created successfully")));
	}
	
	@Test
	public void test_update_WithNonExistingTestId() throws Exception{
		
		TestViewModel viewModel = TestUtil.aNewTestViewModel(
				0L, "A NEW FLU VACCINE", false, true, true
				);
		
		mockMvc.perform(put("/tests/{id}", 0)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$", is("No test found with id : 0")));
	}
	
	@Test
	public void test_update_WithExistingTestIdButExistingTestName() throws Exception{
		
		TestViewModel viewModel = TestUtil.aNewTestViewModel(
				5L, "PHYSICAL", false, true, true
				);
		
		mockMvc.perform(put("/tests/{id}", 5)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("A test with name : PHYSICAL already exists")));
	}
	
	@Test
	public void test_update_WithExistingTestIdAndNewTestName() throws Exception{
		
		TestViewModel viewModel = TestUtil.aNewTestViewModel(
				5L, "UPDATED-TEST-NAME", false, true, true
				);
		
		mockMvc.perform(put("/tests/{id}", 5)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", is("Test updated successfully")));
		
	}
	
	@Test
	public void test_delete_WithNonExistingTestId() throws Exception{
		
		mockMvc.perform(delete("/tests/{id}", 0)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$", is("No test found with id : 0")));
	}
	
	@Test
	public void test_delete_WithExistingTestIdThatHasBeenTakenByNurses() throws Exception{
		
		mockMvc.perform(delete("/tests/{id}", 2)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("Test with id : 2 can not be deleted, it has been taken by nurses")));
	}
	
	@Test
	public void test_delete_WithExistingTestThatHasSubCategories() throws Exception{
		
		mockMvc.perform(delete("/tests/{id}", 4)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().is5xxServerError())
		
		.andExpect(jsonPath("$", is("Test with id : 4 can not be deleted, delete all its sub categories first")));
	}
	
	@Test
	public void test_delete_WithExistingTestThatHasNoSubCategoriesAndHasNotBeenTakenByNurses() throws Exception{
		
		mockMvc.perform(delete("/tests/{id}", 10)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		
		.andExpect(jsonPath("$", is("Test successfully deleted")));
	}
}
