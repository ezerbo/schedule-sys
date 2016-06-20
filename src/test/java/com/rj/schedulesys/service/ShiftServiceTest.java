package com.rj.schedulesys.service;

import static org.junit.Assert.*;

import java.time.LocalTime;

import javax.transaction.Transactional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rj.schedulesys.config.TestConfiguration;
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.ShiftViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class ShiftServiceTest {

	private @Autowired ShiftService shiftService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	@DirtiesContext
	public void test_findAll(){
		assertEquals(3, shiftService.findAll().size());
	}
	
	@Test
	public void test_findOne_WithNonExistingShift(){
		ShiftViewModel viewModel = shiftService.findOne(0L);
		assertNull(viewModel);
	}
	
	@Test
	public void test_findOne_WithExistingShift(){
		ShiftViewModel viewModel = shiftService.findOne(1L);
		assertEquals("NIGHT", viewModel.getName());
	}
	
	@Test
	public void test_findByName_WithNonExistingShift(){
		ShiftViewModel viewModel = shiftService.findByName("This shift does not exist");
		assertNull(viewModel);
	}
	
	@Test
	public void test_findByName_WithExistingShift(){
		ShiftViewModel viewModel = shiftService.findByName("DAY");
		assertEquals(Long.valueOf(2), viewModel.getId());
	}
	
	@Test
	public void test_delete_WithNonExistingShift(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No shift found with id : 0");
		shiftService.delete(0L);
	}
	
	@Test
	public void test_delete_WithExistingShiftThatHasSchedule(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Shift with id : 1 can not be deleted");
		shiftService.delete(1L);
	}
	
	@Test
	public void test_delete_WithExistingShiftThatHasNoSchedule(){
		shiftService.delete(3L);
		assertNull(shiftService.findOne(3L));
	}
	
	@Test
	public void test_create_WithExistingShift(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A shift with name : DAY alredy exists");
		
		ShiftViewModel viewModel = TestUtil.aNewShiftViewModel(
				null, "DAY", LocalTime.now(), LocalTime.NOON
				);
		
		shiftService.create(viewModel);
		
	}
	
	@Test
	public void test_create_WithSameStartAndEndTime(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Shifts start and end time must be different");
		
		ShiftViewModel viewModel = TestUtil.aNewShiftViewModel(
				null, "Same random name", LocalTime.NOON, LocalTime.NOON
				);
		
		shiftService.create(viewModel);
	}
	
	@Test
	public void test_create_WithShiftNameLenth_LessThan_03(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("name size must be between 3 and 30");
		
		ShiftViewModel viewModel = TestUtil.aNewShiftViewModel(
				null, "Sa", LocalTime.now(), LocalTime.NOON
				);
		
		shiftService.create(viewModel);
	}
	
	@Test
	public void test_create_WithShiftNameLenth_GreaterThan_30(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("name size must be between 3 and 30");
		
		ShiftViewModel viewModel = TestUtil.aNewShiftViewModel(
				null, "This name is really long, its over 30 characters", LocalTime.now(), LocalTime.NOON
				);
		
		shiftService.create(viewModel);
	}
	
	@Test
	public void test_create_WithValidData(){
		
		ShiftViewModel viewModel = TestUtil.aNewShiftViewModel(
				null, "Same random name", LocalTime.now(), LocalTime.NOON
				);
		
		viewModel = shiftService.create(viewModel);
		
		assertNotNull(viewModel.getId());
	}
	
	@Test
	public void test_update_WithNonExistingShift(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No shift found with id : 0");
		
		ShiftViewModel viewModel = TestUtil.aNewShiftViewModel(
				0L, "Same random name", LocalTime.now(), LocalTime.NOON
				);
		
		viewModel = shiftService.update(viewModel);
	}
	
	@Test
	public void test_update_WithExistingShiftButAlreadyExistingName(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A shift with name : DAY already exists");
		
		ShiftViewModel viewModel = TestUtil.aNewShiftViewModel(
				1L, "DAY", LocalTime.now(), LocalTime.NOON
				);
		
		viewModel = shiftService.update(viewModel);
	}
	
	@Test
	public void test_update_WithExistingShiftButSameStartAndEndTime(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Shifts start and end time must be different");
		
		ShiftViewModel viewModel = TestUtil.aNewShiftViewModel(
				1L, "NIGHT", LocalTime.NOON, LocalTime.NOON
				);
		
		viewModel = shiftService.update(viewModel);
	}
	
	@Test
	public void test_update_WithValidData(){
		
		ShiftViewModel viewModel = TestUtil.aNewShiftViewModel(
				3L, "SOMETHING", LocalTime.now(), LocalTime.NOON
				);
		
		viewModel = shiftService.update(viewModel);
		
		assertEquals("SOMETHING", shiftService.findOne(3L).getName());
	}
	
	
}