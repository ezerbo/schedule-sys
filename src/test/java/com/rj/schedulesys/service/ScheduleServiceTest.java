package com.rj.schedulesys.service;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.rj.schedulesys.config.TestConfiguration;
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.CreateScheduleViewModel;
import com.rj.schedulesys.view.model.UpdateScheduleViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
public class ScheduleServiceTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Autowired
	private FacilityScheduleService scheduleService;
	
	@Test
	public void test_findAllByFacility(){
		assertEquals(3, scheduleService.findAllByFacility(9L).size());
	}
	
	@Test
	public void findAllBetweenDatesByFacility(){
		assertEquals(3, scheduleService.findAllBetweenDatesByFacility(
				new Date(), new Date(), 9L).size());
	}
	
	@Test
	public void test_update_WithScheduleThatHasNoEmployee(){
		UpdateScheduleViewModel viewModel = TestUtil.aNewUpdateScheduleViewModel(
				11L, 1L, 3L, 2L, 2L, 1L, 0., 0., false, new Date(), "Comment on the schedule");
		viewModel = scheduleService.update(viewModel, 1L);
		assertEquals(Long.valueOf(1), scheduleService.findOne(1L).getEmployee().getId());
	}
	
	@Test
	public void test_create_WithNonExistingFacility(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No facility found with id : 0");
		CreateScheduleViewModel viewModel = TestUtil.aNewCreateScheduleViewModel(
				null, 0L, 1L, 1L, 1L, new Date(), "Comment on the schedule");
		scheduleService.create(viewModel, 1L);
	}
	
	@Test
	public void test_create_WithNonExistingShift(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No shift found with id : 0");
		CreateScheduleViewModel viewModel = TestUtil.aNewCreateScheduleViewModel(
				null, 1L, 0L, 1L,1L, new Date(), "Comment on the schedule");
		scheduleService.create(viewModel, 1L);
	}
	
	@Test
	public void test_create_WithNonExistingScheduleStatus(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No schedule status found with id : 0");
		CreateScheduleViewModel viewModel = TestUtil.aNewCreateScheduleViewModel(
				null, 1L, 1L, 0L, 1L, new Date(), "Comment on the schedule");
		scheduleService.create(viewModel, 1L);
	}
	
	@Test
	public void test_create_WithCONFIRMEDStatusButNoEmployee(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("The schedule is of status 'CONFIRMED' but no nurse or care giver is provided");
		CreateScheduleViewModel viewModel = TestUtil.aNewCreateScheduleViewModel(
				null, 1L, 1L, 1L, 1L, new Date(), "Comment on the schedule");
		scheduleService.create(viewModel, 1L);
	}
	
	@Test
	public void test_create_WithNonExistingEmployee(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No employee found with id : 0");
		CreateScheduleViewModel viewModel = TestUtil.aNewCreateScheduleViewModel(
				0L, 1L, 1L, 1L, 1L, new Date(), "Comment on the schedule");
		scheduleService.create(viewModel, 1L);
	}
	
	@Test
	public void test_create_WithDuplicateShift(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Nurse with id : 1 already has a shift on " + new Date());
		CreateScheduleViewModel viewModel = TestUtil.aNewCreateScheduleViewModel(
				1L, 1L, 1L, 1L, 1L, new Date(), "Comment on the schedule");
		scheduleService.create(viewModel, 1L);
	}
	
	@Test
	public void test_create_WithValidData(){
		CreateScheduleViewModel viewModel = TestUtil.aNewCreateScheduleViewModel(
				5L, 1L, 4L, 1L, 1L, new Date(), "Comment on the schedule");
		viewModel = scheduleService.create(viewModel, 1L);
		assertNotNull(viewModel.getId());
	}
	
	@Test
	public void test_update_WithNonExistingSchedule(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No schedule found with id : 0");
		UpdateScheduleViewModel viewModel = TestUtil.aNewUpdateScheduleViewModel(
				0L, 1L, 1L, 1L, 1L, 1L, 0., 0., false, new Date(), "Comment on the schedule");
		scheduleService.update(viewModel, 1L);
	}
	
	@Test
	public void test_update_WithTimesheetReceivedButNoEmployee(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No time sheet can be provided when the schedule has no employee or is not confirmed");
		UpdateScheduleViewModel viewModel = TestUtil.aNewUpdateScheduleViewModel(
				1L, null, 1L, 1L, 1L, null, 0., 0., true, new Date(), "Comment on the schedule");
		scheduleService.update(viewModel, 1L);
	}
	
	@Test
	public void test_update_WithTimesheetReceivedButNoHoursWorked(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Time sheet is received but the actual hours worked was not submitted");
		UpdateScheduleViewModel viewModel = TestUtil.aNewUpdateScheduleViewModel(
				1L, 1L, 1L, 2L, 1L, 1L, 0., 0., true, new Date(), "Comment on the schedule");
		scheduleService.update(viewModel, 1L);
	}
	
	@Test
	@Ignore
	public void test_update_WithSchedulePostStatusButNoEmployee(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Schedule post status 'ATTENDED' submitted but the schedule is not assigned to any employee");
		UpdateScheduleViewModel viewModel = TestUtil.aNewUpdateScheduleViewModel(
				1L, null, 1L, 2L, 1L, 1L, 8D, 0., false, new Date(), "Comment on the schedule");
		viewModel = scheduleService.update(viewModel, 1L);
	}
	
	@Test
	public void test_update_WithValidData(){
		assertEquals(Long.valueOf(2), scheduleService.findOne(1L).getShift().getId());
		UpdateScheduleViewModel viewModel = TestUtil.aNewUpdateScheduleViewModel(
				1L, 1L, 1L, 4L, 1L, 1L, 8D, 0., true, new Date(), "Comment on the schedule");
		viewModel = scheduleService.update(viewModel, 1L);
		assertEquals(Long.valueOf(4), scheduleService.findOne(1L).getShift().getId());
	}
	
	@Test
	public void test_delete_WithNonExistingSchedule(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No schedule found with id : 0");
		scheduleService.delete(0L);
	}
	
	@Test
	public void test_delete_WithExistingSchedule(){
		assertNotNull(scheduleService.findOne(8L));
		scheduleService.delete(8L);
		assertNull(scheduleService.findOne(8L));
	}
}