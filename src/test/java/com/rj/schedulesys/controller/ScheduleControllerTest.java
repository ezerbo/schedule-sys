package com.rj.schedulesys.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.rj.schedulesys.config.TestConfiguration;
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.CreateScheduleViewModel;
import com.rj.schedulesys.view.model.UpdateScheduleViewModel;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
@WithMockUser(username = "ezerbo", password = "secret", roles =  "ADMIN")
public class ScheduleControllerTest {

	@Autowired
	private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_create_WithCONFIRMEDStatusButNoEmployee() throws Exception{
		CreateScheduleViewModel aNewCreateScheduleViewModel = TestUtil.aNewCreateScheduleViewModel(
				null, 1L, 2L, 1L, new Date(), "Comment on schedule");
		mockMvc.perform(post("/schedules").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(aNewCreateScheduleViewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("The schedule is of status 'CONFIRMED' but no nurse or care giver is provided")));
	}
	
	@Test
	public void test_create_WithDuplicateShift() throws IOException, Exception{
		CreateScheduleViewModel viewModel = TestUtil.aNewCreateScheduleViewModel(
				1L, 1L, 1L, 1L, new Date(), "Comment on the schedule");
		mockMvc.perform(post("/schedules").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", startsWith("Employee with id : 1 already has a shift on")));
		
	}
	
	@Test
	public void test_create_WithValidData() throws IOException, Exception{
		CreateScheduleViewModel viewModel = TestUtil.aNewCreateScheduleViewModel(
				10L, 1L, 1L, 1L, new Date(), "Comment on the schedule");
		
		mockMvc.perform(post("/schedules").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$", startsWith("Schedule successfully created")));
	}
	
	@Test
	public void test_update_WithNonExistingSchedule() throws IOException, Exception{
		UpdateScheduleViewModel viewModel = TestUtil.aNewUpdateScheduleViewModel(
				0L, 1L, 1L, 1L, 1L, 1L, 0., 0., false, new Date(), "Comment on the schedule");
		
		mockMvc.perform(put("/schedules/{id}", 0).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$", startsWith("No schedule found with id : 0")));
	}
	
	@Test
	public void test_update_WithTimesheetReceivedButNoEmployee() throws IOException, Exception{
		UpdateScheduleViewModel viewModel = TestUtil.aNewUpdateScheduleViewModel(
				1L, null, 1L, 1L, 1L, null, 0., 0., true, new Date(), "Comment on the schedule");
		mockMvc.perform(put("/schedules/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", startsWith("No time sheet can be provided when the schedule has no employee or is not confirmed")));
	}
	
	@Test
	public void test_update_WithTimesheetReceivedButNoHoursWorked() throws IOException, Exception{
		UpdateScheduleViewModel viewModel = TestUtil.aNewUpdateScheduleViewModel(
				1L, 1L, 1L, 2L, 1L, null, 0., 0., true, new Date(), "Comment on the schedule");
		mockMvc.perform(put("/schedules/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", startsWith("Time sheet is received but the actual hours worked was not submitted")));
	}
	
	@Test
	public void test_update_WithValidData() throws IOException, Exception{
		UpdateScheduleViewModel viewModel = TestUtil.aNewUpdateScheduleViewModel(
				1L, 1L, 1L, 2L, 1L, 1L, 8D, 0., true, new Date(), "Comment on the schedule");
		mockMvc.perform(put("/schedules/{id}", 1).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", startsWith("Schedule updated successfully")));
	} 
	
	@Test
	public void test_delete_WithNonExistingSchedule() throws Exception{
		mockMvc.perform(delete("/schedules/{id}", 0))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No schedule found with id : 0")));
	}
	
	@Test
	public void test_delete_WithExistingSchedule() throws Exception{
		mockMvc.perform(delete("/schedules/{id}", 8))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is("Schedule successfully deleted")));
	}
}
