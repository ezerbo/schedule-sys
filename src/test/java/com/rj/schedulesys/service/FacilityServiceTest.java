package com.rj.schedulesys.service;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rj.schedulesys.config.TestConfiguration;
import com.rj.schedulesys.service.FacilityService;
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.FacilityViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class FacilityServiceTest {
	
	private @Autowired FacilityService facilityService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void test_findOne_WithNonExistingId(){
		assertNull(facilityService.findOne(0L));
	}
	
	@Test
	public void test_findOneWithExistingId(){
		FacilityViewModel viewModel = facilityService.findOne(1L);
		assertEquals("Sunnyside", viewModel.getName());
	}
	
	@Test
	public void test_findByPhoneNumber_WithNonExistingPhoneNumer(){
		assertNull(facilityService.findByPhoneNumber("911"));
	}
	
	@Test
	public void test_findByPhoneNumber_WithExistingPhoneNumber(){
		FacilityViewModel viewModel = facilityService.findByPhoneNumber("(908)-189-9322");
		assertEquals("Brandywine", viewModel.getName());
	}
	
	@Test
	public void test_findByName_WithNonExistingName(){
		assertNull(facilityService.findByName("This facility does not exist"));
	}
	
	@Test
	public void test_findByName_WithExistingName(){
		FacilityViewModel viewModel = facilityService.findByName("Moutmouth");
		assertEquals("442 Ridge Rd", viewModel.getAddress());
	}
	
	@Test
	public void test_findAll(){
		assertEquals(10, facilityService.findAll().size());
	}
	
	@Test
	public void test_create_WithNull(){
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("No facility provided");
		facilityService.create(null);
	}
	
	@Test
	public void test_create_WithFacilityNameLength_GreaterThan_50(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("name size must be between 3 and 50");
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				null, "This facility name contains more than 50 characters", "Somewhere", "879-541-9587", "748-987-6587"
				);
		
		facilityService.create(viewModel);
	}
	
	@Test
	public void test_create_WithFacilityNameLength_LessThan_03(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("name size must be between 3 and 50");
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				null, "12", "Somewhere", "879-541-9587", "748-987-6587"
				);
		
		facilityService.create(viewModel);
	}
	
	@Test
	public void test_create_WithFacilityAddressLength_GreaterThan_50(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("address size must be between 3 and 50");
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				null, "facility-name", "This address in really long. Does anyone live here ?", "879-541-9587", "748-987-6587"
				);
		
		facilityService.create(viewModel);
	}
	
	@Test
	public void test_create_WithFacilityAddressLength_LessThan_03(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("address size must be between 3 and 50");
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				null, "facility-name", "12", "879-541-9587", "748-987-6587"
				);
		
		facilityService.create(viewModel);
	}
	
	@Test
	public void test_create_WithInvalidFacilityPhoneNumber(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("phoneNumber 719.147.2563 is not a valid phone number");
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				null, "facility-name", "Somewhere", "719.147.2563", "(748)-987-6587"
				);
		
		facilityService.create(viewModel);
	}
	
	@Test
	public void test_create_WithInvalidFaxNumber(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("fax (748)--987-6587 is not a valid fax number");
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				null, "facility-name", "Somewhere", "(719)-147-2563", "(748)--987-6587"
				);
		
		facilityService.create(viewModel);
	}
	
	@Test
	public void test_create_WithExistingFacilityName(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A facility with name 'Sunnyside' already exists");
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				null, "Sunnyside", "Somewhere", "879-541-9587", "748-987-6587"
				);
		
		facilityService.create(viewModel);
	}
	
	@Test
	public void test_create_WithExistingFacilityPhoneNumber(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A facility with phone number '(908)-189-9377' already exists");
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				null, "New name", "Somewhere", "(908)-189-9377", "(748)-987-6587"
				);
		
		facilityService.create(viewModel);
		
	}
	
	@Test
	public void test_createWithNewFacilityNameAndNewPhoneNumber(){
		
		FacilityViewModel viewModel = TestUtil.aNewFacilityViewModel(
				null, "New facility name", "Somewhere", "(908)-888-9377", "(748)-987-6587"
				);
		
		FacilityViewModel model = facilityService.create(viewModel);
		
		assertNotNull(model.getId());
	}
	
	@Test
	public void test_delete_WithNonExistingFacilityId(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No facility found with id : " + 0);
		
		facilityService.delete(0L);
	}
	
	@Test
	public void test_delete_WithFacilityThatHasSchedules(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Facility with id : 1 can not be deleted");
		facilityService.delete(1L);
	}
	
	@Test
	public void test_delete_WithFacilityThatHasNoSchedulesAndNoStaffMembers(){
		facilityService.delete(10L);
		assertNull(facilityService.findOne(10L));
	}
	
}