package com.rj.schedulesys.controller;


import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.LicenseViewModel;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class LicenseControllerTest {

private @Autowired WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void setup(){
		mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
	}
	
	@Test
	public void test_findOne_WithNonExistingLicense() throws Exception{
		mockMvc.perform(get("/licenses/{id}", 0))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$", is("No license found with id : 0")));
	}
	
	@Test
	public void test_findOne_WithExistingLicense() throws Exception{
		mockMvc.perform(get("/licenses/{id}", 1))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(1)))
			.andExpect(jsonPath("$.nurse.id", is(1)))
			.andExpect(jsonPath("$.number", is("111222333444551")));
	}
	
	@Test
	public void test_create_WithNonExistingNurse() throws IOException, Exception{
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				null, 1L, 0L, "##2777330000111", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		mockMvc.perform(post("/licenses").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(aNewLicenseViewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("No nurse found with id : 0" )));
	}
	
	@Test
	public void test_create_WithExistingLicenseNumber() throws IOException, Exception{
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				null, 1L, 1L, "111222333444557", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		mockMvc.perform(post("/licenses").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(aNewLicenseViewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("License number 111222333444557 is already in use" )));
	}
	
	@Test
	public void test_create_WithValidData() throws IOException, Exception{
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				null, 2L, 1L, "111222333444557000", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		mockMvc.perform(post("/licenses").contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(aNewLicenseViewModel)))
		.andExpect(status().isCreated())
		.andExpect(jsonPath("$", is("License successfully created" )));
	}
	
	@Test
	public void test_update_WithNonExistingLicense() throws IOException, Exception{
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				0L, 1L, 0L, "111222333444557000", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		mockMvc.perform(put("/licenses/{id}", 0).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(aNewLicenseViewModel)))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$", is("No license found with id : 0" )));
	}
	
	@Test
	public void test_update_WithNonExistingNurse() throws IOException, Exception{
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				1L, 3L, 0L, "11122233344455232", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		mockMvc.perform(put("/licenses/{id}", 1L).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(aNewLicenseViewModel)))
		.andExpect(status().is5xxServerError())
		.andExpect(jsonPath("$", is("No nurse found with id : 0" )));
	}
	
	@Test
	public void test_update_WithValidData() throws IOException, Exception{
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				6L, 5L, 1L, "11122233344455232", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		mockMvc.perform(put("/licenses/{id}", 6L).contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(TestUtil.convertObjectToJsonBytes(aNewLicenseViewModel)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", is("License successfully updated" )));
	}
	
	@Test
	public void test_delete_WithNonExistingLicense() throws Exception{
		mockMvc.perform(delete("/licenses/{id}", 0L).contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$", is("No license found with id : 0")));
	}
	
	@Test
	public void test_delete_WithExistingLicense() throws Exception{
		mockMvc.perform(delete("/licenses/{id}", 5L).contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", is("License successfully deleted")));
	}
	
}