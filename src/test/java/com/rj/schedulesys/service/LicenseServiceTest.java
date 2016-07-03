package com.rj.schedulesys.service;

import static org.junit.Assert.*;

import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rj.schedulesys.config.TestConfiguration;
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.LicenseViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class LicenseServiceTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Autowired
	private LicenseService licenseService;
	
	@Test
	public void test_findOne_WithNonExistingLicense(){
		assertNull(licenseService.findOne(0L));
	}
	
	@Test
	public void test_findOne_WithExistingLicense(){
		LicenseViewModel viewModel = licenseService.findOne(1L);
		assertEquals("111222333444551", viewModel.getNumber());
	}
	
	@Test
	public void test_findAllByUser_WithNonExistingUser(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No nurse found with id : 0");
		licenseService.findAllByNurse(0L);
	}
	
	@Test
	public void test_findAllByUser_WithExistingUser(){
		assertEquals(2, licenseService.findAllByNurse(1L).size());
	}
	
	@Test
	public void test_create_WithBlankNumber(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("number may not be empty");
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				null, 1L, "", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		licenseService.create(aNewLicenseViewModel);
	}
	
	@Test
	public void test_create_WithPastDate(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("expirationDate must be in the future");
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				null, 1L, "##000eettwwww", new Date(System.currentTimeMillis() - (24 * 60 * 60 * 1000)));
		licenseService.create(aNewLicenseViewModel);
	}
	
	@Test
	public void test_create_WithExistingLicenseNumber(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("111222333444551 is already in use");
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				null, 1L, "111222333444551", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		licenseService.create(aNewLicenseViewModel);
	}
	
	@Test
	public void test_create_WithNonExistingNurse(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No nurse found with id : 0");
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				null, 0L, "0000222333444551", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		licenseService.create(aNewLicenseViewModel);
	}
	
	@Test
	public void test_create_WithValidData(){
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				null, 1L, "0000222333444551", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		aNewLicenseViewModel = licenseService.create(aNewLicenseViewModel);
		assertNotNull(aNewLicenseViewModel.getId());
	}
	
	@Test
	public void test_update_WithNonExistingNurse(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No nurse found with id : 0");
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				null, 0L, "0000222333444551", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		licenseService.update(aNewLicenseViewModel);
	}
	
	@Test
	public void test_update_WithNonExistingLicense(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No license found with id : 0");
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				0L, 1L, "0000222333444551", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		licenseService.update(aNewLicenseViewModel);
	}
	
	@Test
	public void test_update_WithExistingLicenseNumber(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("License number 111222333444552 is already in use");
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				1L, 1L, "111222333444552", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		licenseService.update(aNewLicenseViewModel);
	}
	
	@Test
	public void test_update_WithValidData(){
		LicenseViewModel aNewLicenseViewModel = TestUtil.aNewLicenseViewModel(
				1L, 1L, "000022233344455199", new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
		aNewLicenseViewModel = licenseService.create(aNewLicenseViewModel);
		assertEquals(licenseService.findOne(1L).getNumber(),aNewLicenseViewModel.getNumber());
	}
	
	@Test
	public void test_delete_WithNonExistingLicense(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No license found with id : 0");
		licenseService.delete(0L);
	}
	
	@Test
	public void test_delete_WithExistingLicense(){
		assertNotNull(licenseService.findOne(7L));
		licenseService.delete(7L);
		assertNull(licenseService.findOne(7L));
	}
	
}