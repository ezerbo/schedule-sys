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
import com.rj.schedulesys.view.model.ScheduleStatusViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class ScheduleStatusServiceTest {

	private @Autowired ScheduleStatusService scheduleStatusService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void test_findByStatus_WithNonExistingStatus(){
		ScheduleStatusViewModel viewModel = scheduleStatusService.findByStatus("This status does not exists");
		assertNull(viewModel);
	}
	
	@Test
	public void test_findByStatus_WithExistingStatus(){
		ScheduleStatusViewModel viewModel = scheduleStatusService.findByStatus("CONFIRMED");
		assertEquals(Long.valueOf(1), viewModel.getId());
	}
	
	@Test
	public void test_create_WithExistingStatus(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A schedule status with status : CONFIRMED already exists");
		
		ScheduleStatusViewModel viewModel = TestUtil.aNewScheduleStatusViewModel(null, "CONFIRMED");
		
		scheduleStatusService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStatusLength_LessThan_03(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("status size must be between 3 and 20");
		
		ScheduleStatusViewModel viewModel = TestUtil.aNewScheduleStatusViewModel(null, "CO");
		
		scheduleStatusService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStatusLength_GreaterThan_20(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("status size must be between 3 and 20");
		
		ScheduleStatusViewModel viewModel = TestUtil.aNewScheduleStatusViewModel(null, "THIS SHOULD BE LONGER THAN 20 CHARACTERS");
		
		scheduleStatusService.create(viewModel);
	}
	
	@Test
	public void test_create_WithNonExistingStatus(){
		
		ScheduleStatusViewModel viewModel = TestUtil.aNewScheduleStatusViewModel(null, "NEW-STATUS");
		
		viewModel = scheduleStatusService.create(viewModel);
		
		assertNotNull(viewModel.getId());
	}
	
	@Test
	public void test_update_WithNonExistingScheduleStatusId(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No schedule status found with id : 0");
		
		ScheduleStatusViewModel viewModel = TestUtil.aNewScheduleStatusViewModel(0L, "NEW-STATUS");
		
		scheduleStatusService.update(viewModel);
	}
	
	@Test
	public void test_update_WithExistingScheduleStatusButExistingStatus(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A schedule status with status : NOT CONFIRMED already exists");
		
		ScheduleStatusViewModel viewModel = TestUtil.aNewScheduleStatusViewModel(1L, "NOT CONFIRMED");
		
		scheduleStatusService.update(viewModel);
	}
	
	@Test
	public void test_delete_WithNonExistingSheduleStatusId(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No schedule status found with id : 0");
		
		scheduleStatusService.delete(0L);
	}
	
	@Test
	public void test_delete_WithExistingScheduleStatusThatHasSchedules(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No schedule status found with id : 0");
		
		scheduleStatusService.delete(0L);
	}
	
	@Test
	public void test_delete_WithExistingScheduleStatusThatHasNoSchedules(){
		scheduleStatusService.delete(2L);
		assertNull(scheduleStatusService.findById(2L));
	}
	
}