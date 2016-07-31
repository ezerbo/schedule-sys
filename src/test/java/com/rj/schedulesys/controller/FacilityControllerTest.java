package com.rj.schedulesys.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.FacilityViewModel;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class FacilityControllerTest {

	
	private @Autowired WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_findAll() throws Exception{
		mockMvc.perform(get("/facilities"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(10)));
	}
	
	@Test
	public void test_findOne_WithNonExistingFacility() throws Exception{
		mockMvc.perform(get("/facilities/{id}", 0))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void test_findOne_WithExistingFacility() throws Exception{
		mockMvc.perform(get("/facilities/{id}", 1))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name", is("Sunnyside")))
			.andExpect(jsonPath("$.address", is("7080 Samuel Morse Dr")))
			.andExpect(jsonPath("$.phoneNumber", is("9081899371")))
			.andExpect(jsonPath("$.fax", is("9839838888")));
	}
	
	@Test
	public void test_create_WithExistingFacilityName() throws Exception{
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				null, "Sunnyside", "Somewhere", "1479868754", "2108749865");
		
		mockMvc.perform(post("/facilities")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$", is("A facility with name 'Sunnyside' already exists")));
	}
	
	@Test
	public void test_create_WithExistingFacilityPhoneNumber() throws Exception{
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				null, "new-name ", "Somewhere", "9081899371", "2108749865"
				);
		
		mockMvc.perform(post("/facilities")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$", is("A facility with phone number '9081899371' already exists")));
	}
	
	@Test
	public void test_create_WithNonExistingFacilityNameAndPhoneNumber() throws Exception{
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				null, "new-name-1", "Somewhere", "9080029321", "2108749865"
				);
		
		mockMvc.perform(post("/facilities")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$", is("Facility successfully created")));
	}
	
	@Test
	public void test_update_WithNonExistingFacilityId() throws Exception{
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				0L, "new name", "Somewhere", "9080029371", "2108749865"
				);
		
		mockMvc.perform(put("/facilities/{id}", 0L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No facility found with id : 0")));
	}
	
	@Test
	public void test_update_WithNonExistingFacilityNameAndPhoneNumber() throws Exception{
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				10L, "new name", "Somewhere", "9080029371", "2108749865"
				);
		
		mockMvc.perform(put("/facilities/{id}", 10L)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is("Facility successfully updated")));
	}
	
	@Test
	public void test_delete_WithNonExistingFacilityId() throws Exception{
		mockMvc.perform(delete("/facilities/{id}", 0L)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No facility found with id : 0")));
	}
	
	@Test
	public void test_delete_WithFacilityThatHasSchedulesOrStaffMembers() throws Exception{
		mockMvc.perform(delete("/facilities/{id}", 1L)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$", is("Facility with id : 1 can not be deleted")));
	}
	
	@Test
	public void test_delete_WithFacilityThatHasNoSchedulesAndNoStaffMembers() throws Exception{
		mockMvc.perform(delete("/facilities/{id}", 10)
		.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is("Facility successfully deleted")));
	}
	
	@Test
	public void test_findAllStaffMembers_WithNonExistingFacility() throws Exception{
		mockMvc.perform(get("/facilities/{id}/staff-members", 0)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isNotFound())
					.andExpect(jsonPath("$", is("No facility found with id : 0")));
	}
	
	@Test
	public void test_findAllStaffMembers_WithExistingFacility() throws Exception{
		mockMvc.perform(get("/facilities/{id}/staff-members", 4)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(2)));
	}
	
	@Test
	public void test_findAllSchedule_WithoutDates() throws Exception{
		mockMvc.perform(get("/facilities/{id}/schedules", 9))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(2)));
	}
	
	@Test
	public void test_findAllSchedule_WithDates() throws Exception{
		mockMvc.perform(get("/facilities/{id}/schedules", 9)
				.param("startDate", new Date().toString())
				.param("endDate", new Date().toString())
				)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(2)));
	}
	
	
	
}