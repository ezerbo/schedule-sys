package com.rj.schedulesys.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
import com.rj.schedulesys.view.model.LicenseTypeViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class LicenseTypeServiceTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Autowired
	private LicenseTypeService licenseTypeService;
	
	@Test
	public void test_findAll(){
		assertEquals(5, licenseTypeService.findAll().size());
	}
	
	@Test
	public void test_findOne_WithNonExistingTestId(){
		assertNull(licenseTypeService.findOne(0L));
	}
	
	@Test
	public void test_findOne_WithExistingTestId(){
		assertEquals("LNP", licenseTypeService.findOne(2L).getTypeName());
	}
	
	@Test
	public void test_create_WithExistingLicenseTypeName(){
		expectedException.expectMessage("License type name : RN already in use");
		expectedException.expect(RuntimeException.class);
		LicenseTypeViewModel viewModel = TestUtil.aNewLicenseTypeViewModel(null, "RN");
		licenseTypeService.create(viewModel);
	}
}
