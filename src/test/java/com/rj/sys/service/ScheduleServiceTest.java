package com.rj.sys.service;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.config.TestConfiguration;
import com.rj.sys.dao.ScheduleDao;
import com.rj.sys.view.model.ScheduleViewModel;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestConfiguration.class)
@TestPropertySource("classpath:test-application.properties")
public class ScheduleServiceTest {
	
	private @Autowired ScheduleDao scheduleDao ;
	private @Autowired ScheduleService scheduleService;
	
	
	@Test
	@Transactional
	public void test(){
		int sizeBeforeCreation = scheduleDao.findAll().size();
		ScheduleViewModel viewModel = ScheduleViewModel.builder()
				.assigneeId(2L)
				.facility("Brandywine")
				.scheduleComment("Comment on the schedule")
				.scheduleDate(new Date())
				.scheduleStatus("CONFIRMED")
				.shift("NIGHT")
				.timesheetReceived(false)
				.build();
		
		scheduleService.createSchedule(viewModel, 1L);
		
		int sizeAfterCreation = scheduleDao.findAll().size();
		assertEquals(sizeAfterCreation, sizeBeforeCreation + 1);
	}
}
