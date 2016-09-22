package com.rj.schedulesys.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Date;

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
import com.rj.schedulesys.data.EmployeeTestStatusConstants;
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.EmployeeTestViewModel;

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
			.andExpect(jsonPath("$", hasSize(2)));
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
	
	@Test
	public void test_addOrUpdateTest_WithNonExistingNurse() throws Exception{
		EmployeeTestViewModel viewModel = TestUtil.aNewNurseTestViewModel(
				0L, 1L, null, EmployeeTestStatusConstants.APPLICABLE_STATUS, new Date(),new Date());
		mockMvc.perform(post("/employees/{id}/tests", 0L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$", is("No employee found with id : 0")));
	}
	
	@Test
	public void test_addOrUpdateTest_WithNonExistingTest() throws IOException, Exception{
		EmployeeTestViewModel viewModel = TestUtil.aNewNurseTestViewModel(
				0L, 1L, null, EmployeeTestStatusConstants.APPLICABLE_STATUS, new Date(),new Date());
		mockMvc.perform(post("/employees/{id}/tests", 1L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("No test found with id : 0")));
	}
	
	@Test
	public void test_addOrUpdateTest_WithNoCompletionDateForTestThatRequiresCompletionDate() throws IOException, Exception{
		EmployeeTestViewModel viewModel = TestUtil.aNewNurseTestViewModel(
				1L, 1L, null, EmployeeTestStatusConstants.APPLICABLE_STATUS, null, new Date());
		mockMvc.perform(post("/employees/{id}/tests", 1L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("A complete date must be provided")));
	}
	
	@Test
	public void test_addOrUpdateTest_WithNoExpirationDateForTestThatRequiresExpirationDate() throws IOException, Exception{
		EmployeeTestViewModel viewModel = TestUtil.aNewNurseTestViewModel(
				2L, 1L, null, EmployeeTestStatusConstants.APPLICABLE_STATUS, new Date(), null);
		mockMvc.perform(post("/employees/{id}/tests", 1L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("An expiration date must be provided")));
	}
	
	@Test
	public void test_addOrUpdateTest_WithFutureCompletionDate() throws IOException, Exception{
		EmployeeTestViewModel viewModel = TestUtil.aNewNurseTestViewModel(
				1L, 1L, null, EmployeeTestStatusConstants.APPLICABLE_STATUS, new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)), new Date());
		mockMvc.perform(post("/employees/{id}/tests", 1L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("Completed date must be in the past")));
	}
	
	@Test
	public void test_addOrUpdateTest_WithPastExpirationDate() throws IOException, Exception{
		EmployeeTestViewModel viewModel = TestUtil.aNewNurseTestViewModel(
				1L, 1L, null, EmployeeTestStatusConstants.APPLICABLE_STATUS, new Date(), new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)));
		mockMvc.perform(post("/employees/{id}/tests", 1L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("Expiration date must be in the future")));
	}
	
	@Test
	public void test_addOrUpdateTest_WithValidData() throws IOException, Exception{
		EmployeeTestViewModel viewModel = TestUtil.aNewNurseTestViewModel(
				1L, 1L, null, EmployeeTestStatusConstants.APPLICABLE_STATUS
				, new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000))
				, new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		mockMvc.perform(post("/employees/{id}/tests", 1L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$", is("Test successfully added")));
	}

	@Test
	public void test_deleteTest_WithNonExistingNurse() throws Exception{
		mockMvc.perform(delete("/employees/{id}/tests/{testId}", 0, 1))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No employee found with id : 0")));
	}
	
	@Test
	public void test_deleteTest_WithNonExistingTest() throws Exception{
		mockMvc.perform(delete("/employees/{id}/tests/{testId}", 1, 0))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No test found with id : 0")));
	}
	
	@Test
	public void test_deleteTest_WithExistingNurseTest() throws Exception{
		mockMvc.perform(delete("/employees/{id}/tests/{testId}", 5, 4))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is("Test successfully deleted")));
	}
	
	@Test
	public void test_findAllByNurse() throws Exception{
		mockMvc.perform(get("/employees/{id}/tests", 4))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(2)));
	}
}