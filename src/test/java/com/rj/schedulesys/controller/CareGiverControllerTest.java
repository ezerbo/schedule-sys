package com.rj.schedulesys.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
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
import com.rj.schedulesys.view.model.EmployeeViewModel;
import com.rj.schedulesys.view.model.PhoneNumberViewModel;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class CareGiverControllerTest {
	
	private @Autowired WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_findOne_WithNonExistingNurseId() throws Exception{
		mockMvc.perform(get("/care-givers/{id}", 0))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No care giver found with id : 0")));
	}
	
	@Test
	public void test_findOne_WithExistingNurseId() throws Exception{
		mockMvc.perform(get("/care-givers/{id}", 6))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.lastName", is("SANOGO")))
			.andExpect(jsonPath("$.firstName", is("Flora Madina")))
			.andExpect(jsonPath("$.positionName", is("COMPANION")))
			.andExpect(jsonPath("$.comment", is("COMMENT ON EMPLOYEE WITH ID 6")));
	}
	
	@Test
	public void test_create_WithNonExistingPosition() throws IOException, Exception{
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(null, "(718)-720-9836", "PRIMARY", "MOBILE");
		//(Long id, String firstName, String lastName, String positionName, Boolean ebc, Boolean cpr, Date dateOfHire, Date rehireDate, Date lastDayOfWork, String comment)
		
		EmployeeViewModel aNewEmployeeViewModel = TestUtil.aNewEmployeeViewModel(0L, "firstName", "lastName", "This position does not exist"
				, true, new Date(), null, null, null);
		
		aNewEmployeeViewModel.setPhoneNumbers(Arrays.asList(aNewPhoneNumberViewModel));
		mockMvc.perform(post("/care-givers")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(aNewEmployeeViewModel)))
		.andExpect(status().is4xxClientError())
		.andExpect(jsonPath("$", is("No position found with name : This position does not exist")));
	}
	
	@Test
	public void test_create_WithExisingPosition() throws IOException, Exception{
		
		PhoneNumberViewModel aNewPhoneNumberViewModel = TestUtil.aNewPhoneNumberViewModel(null, "7187209836", "PRIMARY", "MOBILE");
		
		EmployeeViewModel aNewEmployeeViewModel = TestUtil.aNewEmployeeViewModel(0L, "firstName", "lastName", "COMPANION"
				, true, new Date(), null, null, null);
		
		aNewEmployeeViewModel.setPhoneNumbers(Arrays.asList(aNewPhoneNumberViewModel));
		
		mockMvc.perform(post("/care-givers")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(aNewEmployeeViewModel)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$", is("Care giver successfully created")));
	}
	
}
