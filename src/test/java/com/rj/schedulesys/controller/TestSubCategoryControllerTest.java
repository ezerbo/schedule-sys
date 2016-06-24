package com.rj.schedulesys.controller;



import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.rj.schedulesys.view.model.TestSubCategoryViewModel;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class TestSubCategoryControllerTest {
	
private @Autowired WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_findOne_WithNonExistingTest() throws Exception{
		mockMvc.perform(get("/test-sub-categories/{id}", 0))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$", is("No test sub category found with id or name : 0")));
	}
	
	@Test
	public void test_findAllByTest_WithExistingTest() throws Exception{
		mockMvc.perform(get("/test-sub-categories/{id}", 3))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("QUANTIFERON GOLD")))
		.andExpect(jsonPath("$.testName", is("TB TEST")));
		
	}
	
	@Test
	public void test_findOne_WithExistingTest() throws Exception{
		mockMvc.perform(get("/test-sub-categories/{id}", 4))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("SAMPLE-1")));
	}
	
	@Test
	public void test_create_WithNonExsitingParentTest() throws Exception{

		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				null, "sub-test-name", "This parent test does not exists"
				);
		
		mockMvc.perform(post("/test-sub-categories")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("No test found with name : This parent test does not exists")));
		
	}
	
	@Test
	public void test_create_WithExistingParentTest_ThatHasAlreadyASubCategoryOfSameName() throws Exception{
		
		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				null, "ANNUAL PPD", "TB TEST"
				);
		
		mockMvc.perform(post("/test-sub-categories")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("A sub category with name : ANNUAL PPD already exists for test with name : TB TEST")));
	}
	
	@Test
	public void test_create_WithExistingParentTest_ThatHasNotTheCategoryBeingCreated() throws Exception{
		
		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				null, "BRAND-NEW-SUBCAT-1", "TB TEST"
				);
		
		mockMvc.perform(post("/test-sub-categories")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$", is("Test sub category successfully created")));
		
	}
	
	@Test
	public void test_update_WithNonExistingTestSubCategoryId() throws  Exception{
		
		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				0L, "BRAND-NEW-SUBCAT-1", "TB TEST"
				);
		
		mockMvc.perform(put("/test-sub-categories/{id}", 0L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$", is("No test sub category found with id : 0")));
		
	}
	
	@Test
	public void test_update_WithExistingTestSubCategory_ThatAlreayHas_ASubCategory_WithTheNewName() throws Exception{
		
		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				1L, "2-STEP PPD", "TB TEST"
				);
		
		mockMvc.perform(put("/test-sub-categories/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("A sub category with name : 2-STEP PPD already exists for test with name : TB TEST")));
	}
	
	@Test
	public void test_update_WithExistingTestSubCategory_AndNewSubCategoryName() throws Exception{
		
		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				1L, "BRAND-NEW-NAME", "TB TEST"
				);
		
		mockMvc.perform(put("/test-sub-categories/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", is("Test sub category successfully updated")));
		
	}
	
	@Test
	public void test_delete_WithNonExistingTestSubCategoryId() throws Exception{
		
		mockMvc.perform(delete("/test-sub-categories/{id}", 0L)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$", is("No test sub category found with id : 0")));
	}
	
	@Test
	public void test_delete_WithExistingTestSubCategoryThatHasBeenTakenByNurses() throws Exception{
		
		mockMvc.perform(delete("/test-sub-categories/{id}", 4L)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("Test sub category with id : 4 can not be deleted, it has been taken by nurses")));
	}
}
