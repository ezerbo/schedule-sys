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
import com.rj.schedulesys.view.model.StaffMemberViewModel;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class StaffMemberControllerTest {

	private @Autowired WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_findOne_WithNonExistingStaffMember() throws Exception{
		mockMvc.perform(get("/staff-members/{id}", 0))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No staff member found with id : 0")));
			
	}
	
	@Test
	public void test_findOne_WithExistingStaffMember() throws Exception{
		mockMvc.perform(get("/staff-members/{id}", 1L))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.lastName", is("STAFF-MEMBER-1-LN")))
			.andExpect(jsonPath("$.firstName", is("STAFF-MEMBER-1-FN")))
			.andExpect(jsonPath("$.title", is("STAFF-MEMBER-1-TITLE")))
			.andExpect(jsonPath("$.facilityName", is("Sunnyside")));
	}
	
	@Test
	public void test_create_WithNonExistingFacility() throws Exception{
	
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "NEW-FIRST NAME-1", "NEW-LAST-NAME-1", "NEW-TITLE-1", "This facility does not exists"
				);
		
		mockMvc.perform(post("/staff-members")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$", is("No such facility : 'This facility does not exists'")));
	}
	
	@Test
	public void test_create_WithExistingMember() throws Exception{
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "STAFF-MEMBER-1-FN", "STAFF-MEMBER-1-LN", "STAFF-MEMBER-1-TITLE", "Sunnyside"
				);
		
		mockMvc.perform(post("/staff-members")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$", is("A staff member with first name 'STAFF-MEMBER-1-FN'"
				+ ", last name 'STAFF-MEMBER-1-LN' and title 'STAFF-MEMBER-1-TITLE' already exists for facility with name 'Sunnyside'")));
	}
	
	@Test
	public void test_create_WithNewMember() throws Exception{
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "STAFF-MEMBER-N-FN", "STAFF-MEMBER-N-LN", "STAFF-MEMBER-N-TITLE", "Sunnyside"
				);
		
		mockMvc.perform(post("/staff-members")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$", is("Successfully created staff member")));
		
	}
	
	@Test
	public void test_update_WithNonExistingMember() throws Exception{
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				0L, "STAFF-MEMBER-N-FN", "STAFF-MEMBER-N-LN", "STAFF-MEMBER-N-TITLE", "Sunnyside"
				);
		
		mockMvc.perform(put("/staff-members/{id}", 0)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No staff member found with id : 0")));
	}
	
	@Test
	public void test_update_WithExistingStaffMemberFirstNameLastNameAndTitle() throws Exception{
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				3L, "STAFF-MEMBER-4-FN", "STAFF-MEMBER-4-LN", "STAFF-MEMBER-4-TITLE", "Brandywine"
				);
		
		mockMvc.perform(put("/staff-members/{id}", 3)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().is5xxServerError())
			.andExpect(jsonPath("$", is("A staff member with first name 'STAFF-MEMBER-4-FN'"
					+ ", last name 'STAFF-MEMBER-4-LN' and title 'STAFF-MEMBER-4-TITLE' already exists for facility with name 'Brandywine'")));
	}
	
	@Test
	public void test_update_WithNewStaffMemberFirstNameLastNameAndTitle() throws Exception{
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				2L, "STAFF-MEMBER-1-FN-UPDATE", "STAFF-MEMBER-2-LN-UPDATE", "STAFF-MEMBER-2-TITLE-UPDATE", "Sunnyside"
				);
		
		mockMvc.perform(put("/staff-members/{id}", 2)
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(viewModel)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is("Successfully updated staff member")));
	}
	
	@Test
	public void test_delete_WithNonExistingStaffMember() throws Exception{
		mockMvc.perform(delete("/staff-members/{id}", 0)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$", is("No staff member found with id : 0")));
		
	}
	
	@Test
	public void test_delete_WithExistingStaffMember() throws Exception{
		mockMvc.perform(delete("/staff-members/{id}", 9)
				.contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", is("Staff member successfully deleted")));
		
	}
	
}