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
import com.rj.schedulesys.view.model.SchedulePostStatusViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class SchedulePostStatusServiceTest {

	private @Autowired SchedulePostStatusService schedulePostStatusService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void test_findByStatus_WithNonExistingStatus(){
		SchedulePostStatusViewModel viewModel = schedulePostStatusService.findByStatus("This status does not exists");
		assertNull(viewModel);
	}
	
	@Test
	public void test_findByStatus_WithExistingStatus(){
		SchedulePostStatusViewModel viewModel = schedulePostStatusService.findByStatus("ATTENDED");
		assertEquals(Long.valueOf(1), viewModel.getId());
	}
	
	@Test
	public void test_create_WithExistingStatus(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A schedule post status with status : ATTENDED already exists");
		
		SchedulePostStatusViewModel viewModel = TestUtil.aNewSchedulePostStatusViewModel(null, "ATTENDED");
		
		schedulePostStatusService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStatusLength_LessThan_03(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("status size must be between 3 and 20");
		
		SchedulePostStatusViewModel viewModel = TestUtil.aNewSchedulePostStatusViewModel(null, "AT");
		
		schedulePostStatusService.create(viewModel);
	}
	
	@Test
	public void test_create_WithStatusLength_GreaterThan_20(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("status size must be between 3 and 20");
		
		SchedulePostStatusViewModel viewModel = TestUtil.aNewSchedulePostStatusViewModel(null, "THIS SHOULD BE LONGER THAN 20 CHARACTERS");
		
		schedulePostStatusService.create(viewModel);
	}
	
	@Test
	public void test_create_WithNonExistingStatus(){
		
		SchedulePostStatusViewModel viewModel = TestUtil.aNewSchedulePostStatusViewModel(null, "NEW-STATUS");
		
		viewModel = schedulePostStatusService.create(viewModel);
		
		assertNotNull(viewModel.getId());
	}
	
	@Test
	public void test_update_WithNonExistingScheduleStatusId(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No schedule post status found with id : 0");
		
		SchedulePostStatusViewModel viewModel = TestUtil.aNewSchedulePostStatusViewModel(0L, "NEW-STATUS");
		
		schedulePostStatusService.update(viewModel);
	}
	
	@Test
	public void test_update_WithExistingScheduleStatusButExistingStatus(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A schedule post status with status : LATE already exists");
		
		SchedulePostStatusViewModel viewModel = TestUtil.aNewSchedulePostStatusViewModel(1L, "LATE");
		
		schedulePostStatusService.update(viewModel);
	}
	
	@Test
	public void test_delete_WithNonExistingSheduleStatusId(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No schedule post status found with id : 0");
		
		schedulePostStatusService.delete(0L);
	}
	
	@Test
	public void test_delete_WithExistingScheduleStatusThatHasSchedules(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No schedule post status found with id : 0");
		
		schedulePostStatusService.delete(0L);
	}
	
	@Test
	public void test_delete_WithExistingScheduleStatusThatHasNoSchedules(){
		schedulePostStatusService.delete(2L);
		assertNull(schedulePostStatusService.findOne(2L));
	}
}
