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
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.StaffMemberViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class StaffMemberServiceTest {

	private @Autowired StaffMemberService staffMemberService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void test_findAll_ForSpecificFacility(){
		assertEquals(2, staffMemberService.findAllByFacility(1L).size());
	}
	
	@Test
	public void test_find_WithNonExistingFacility(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No facility found with id : 0");
		staffMemberService.findAllByFacility(0L);
	}
	
	@Test
	public void test_delete_WithNonExistingStaffMemberId(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No staff member found with id : 0");
		staffMemberService.delete(0L);
	}
	
	@Test
	public void test_delete_WithExistingStaffMemberId(){
		staffMemberService.delete(1L);
		assertNull(staffMemberService.findOne(1L));
	}
	
	@Test
	public void test_create_WithExistingFirstNameLastNameAndTitle(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A staff member with first name 'STAFF-MEMBER-1-FN'"
				+ ", last name 'STAFF-MEMBER-1-LN' and title 'STAFF-MEMBER-1-TITLE' already exists for facility with name 'Sunnyside'");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				1L, "STAFF-MEMBER-1-FN", "STAFF-MEMBER-1-LN", "STAFF-MEMBER-1-TITLE", "Sunnyside"
				);
		
		staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithNonExistingFacility(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No such facility : 'Non existing facility'");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				1L, "NEW-FIRST-NAME", "NEW-LAST-NAME", "NEW-TITLE", "Non existing facility"
				);
		
		staffMemberService.create(viewModel);
		
	}
	
	@Test
	public void test_create_WithStaffMemberFirstNameLength_LessThan_03(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("firstName size must be between 3 and 50");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "NE", "NEW-LAST-NAME", "NEW-TITLE", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStaffMemberFirstNameLength_Greater_Than_50(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("firstName size must be between 3 and 50");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "THIS IS A REALLY LONG FIRST NAME WHOSE NAME IS THIS ?", "NEW-LAST-NAME", "NEW-TITLE", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStaffMemberLastNameLength_LessThan_03(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("lastName size must be between 3 and 50");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "NEW-FIRST-NAME-TEST", "NE", "NEW-TITLE", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStaffMemberLastNameLength_GreaterThan_50(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("lastName size must be between 3 and 50");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "NEW-FIRST-NAME-TEST", "THIS IS A REALLY LONG LAST NAME WHOSE NAME IS THIS ?", "NEW-TITLE", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStaffMemberTitleLength_LessThan_03(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("title size must be between 3 and 50");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "NEW-FIRST-NAME-TEST", "NEW-LAST-NAME-TEST", "NE", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStaffMemberTitleLength_GreaterThan_50(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("title size must be between 3 and 50");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "NEW-FIRST-NAME-TEST", "NEW-LAST-NAME-TEST", "THIS A REALLY LONG STAFF MEMBER TITLE, WHO DOES IT BELONG TO ?", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithNewStaffMember(){
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "NEW-FIRST-NAME", "NEW-LAST-NAME", "NEW-TITLE", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
		
		assertNotNull(viewModel.getId());
	}
	
	@Test
	public void test_update_WithNonExistingFacilityName(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No facility found with name 'This facility does not exist'");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				1L, "NEW-FIRST-NAME", "NEW-LAST-NAME", "NEW-TITLE", "This facility does not exist"
				);
		
		viewModel = staffMemberService.update(viewModel);
	}
	
	@Test
	public void test_update_WithExistingFirstNameLastNameAndTitle(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A staff member with first name 'STAFF-MEMBER-2-FN'"
				+ ", last name 'STAFF-MEMBER-2-LN' and title 'STAFF-MEMBER-2-TITLE' already exists for facility with name 'Sunnyside'");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				1L, "STAFF-MEMBER-2-FN", "STAFF-MEMBER-2-LN", "STAFF-MEMBER-2-TITLE", "Sunnyside"
				);
		
		staffMemberService.update(viewModel);
	}
	
	@Test
	public void test_update_WithNewFirstNameNewLastNameAndNewTitle(){
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				1L, "NEW-STAFF-MEMBER-FN", "NEW-STAFF-MEMBER-LN", "NEW-STAFF-MEMBER-TITLE", "Sunnyside"
				);
		
		viewModel = staffMemberService.update(viewModel);
		
		assertEquals(staffMemberService.findOne(1L).getLastName(), viewModel.getLastName());
	}
	
}