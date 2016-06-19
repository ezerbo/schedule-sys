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

import com.rj.schedulesys.dao.ScheduleDao;
import com.rj.schedulesys.service.ScheduleService;
import com.rj.schedulesys.view.model.ScheduleViewModel;
import com.rj.sys.config.TestConfiguration;

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
				.employeeName("test-fn,test-ln")
				.scheduleComment("Comment on the schedule")
				.scheduleDate(new Date(System.currentTimeMillis() + (24*60*60*1000)))
				.facility("Sunnyside")
				.scheduleStatus("CONFIRMED")
				.shift("NIGHT")
				.build();
		
		scheduleService.createSchedule(viewModel, 1L);
		
		int sizeAfterCreation = scheduleDao.findAll().size();
		assertEquals(sizeAfterCreation, sizeBeforeCreation + 1);
	}
}
