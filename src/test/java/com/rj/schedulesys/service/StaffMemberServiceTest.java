package com.rj.schedulesys.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
		assertEquals(2, staffMemberService.findAllByFacility(4L).size());
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
				1L, "STAFF-MEMBER-1-FN", "STAFF-MEMBER-1-LN", "STAFF-MEMBER-1-TITLE", "7475841470", "7475841471", "Sunnyside"
				);
		
		staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithNonExistingFacility(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No such facility : 'Non existing facility'");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				1L, "NEW-FIRST-NAME", "NEW-LAST-NAME", "NEW-TITLE","7475841472", "7478841471", "Non existing facility"
				);
		
		staffMemberService.create(viewModel);
		
	}
	
	@Test
	public void test_create_WithStaffMemberFirstNameLength_LessThan_03(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("firstName size must be between 3 and 50");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "NE", "NEW-LAST-NAME", "NEW-TITLE","7475841070", "7475841451", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStaffMemberFirstNameLength_Greater_Than_50(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("firstName size must be between 3 and 50");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "THIS IS A REALLY LONG FIRST NAME WHOSE NAME IS THIS ?", "NEW-LAST-NAME", "NEW-TITLE","2475841470", "1475841471", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStaffMemberLastNameLength_LessThan_03(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("lastName size must be between 3 and 50");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "NEW-FIRST-NAME-TEST", "NE", "NEW-TITLE","7475941470", "7475941471", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStaffMemberLastNameLength_GreaterThan_50(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("lastName size must be between 3 and 50");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "NEW-FIRST-NAME-TEST", "THIS IS A REALLY LONG LAST NAME WHOSE NAME IS THIS ?","7475741470", "7475841471", "NEW-TITLE", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStaffMemberTitleLength_LessThan_03(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("title size must be between 3 and 50");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "NEW-FIRST-NAME-TEST", "NEW-LAST-NAME-TEST", "NE", "6475841470", "3475841471", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStaffMemberTitleLength_GreaterThan_50(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("title size must be between 3 and 50");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "NEW-FIRST-NAME-TEST", "NEW-LAST-NAME-TEST", "THIS A REALLY LONG STAFF MEMBER TITLE, WHO DOES IT BELONG TO ?", "7475847470", "7435841471", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
	}
	
	@Test
	public void test_create_WithNewStaffMember(){
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				null, "NEW-FIRST-NAME", "NEW-LAST-NAME", "NEW-TITLE","7475341470", "7475881471", "Sunnyside"
				);
		
		viewModel = staffMemberService.create(viewModel);
		
		assertNotNull(viewModel.getId());
	}
	
	@Test
	public void test_update_WithNonExistingFacilityName(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No facility found with name 'This facility does not exist'");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				1L, "NEW-FIRST-NAME", "NEW-LAST-NAME", "NEW-TITLE","7475841479", "7475841473", "This facility does not exist"
				);
		
		viewModel = staffMemberService.update(viewModel);
	}
	
	@Test
	public void test_update_WithExistingFirstNameLastNameAndTitle(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A staff member with first name 'STAFF-MEMBER-7-FN'"
				+ ", last name 'STAFF-MEMBER-7-LN' and title 'STAFF-MEMBER-7-TITLE' already exists for facility with name 'Burnside'");
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				8L, "STAFF-MEMBER-7-FN", "STAFF-MEMBER-7-LN", "STAFF-MEMBER-7-TITLE","2475841470", "7475801471", "Burnside"
				);
		
		staffMemberService.update(viewModel);
	}
	
	@Test
	public void test_update_WithNewFirstNameNewLastNameAndNewTitle(){
		
		StaffMemberViewModel viewModel = TestUtil.aNewStaffMemberViewModel(
				1L, "NEW-STAFF-MEMBER-FN", "NEW-STAFF-MEMBER-LN", "NEW-STAFF-MEMBER-TITLE", "7465841470", "7475849471", "Sunnyside"
				);
		
		viewModel = staffMemberService.update(viewModel);
		
		assertEquals(staffMemberService.findOne(1L).getLastName(), viewModel.getLastName());
	}
	
}