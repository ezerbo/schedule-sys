package com.ss.schedulesys.service;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.ScheduleSysApp;
import com.ss.schedulesys.domain.CareCompany;
import com.ss.schedulesys.domain.Employee;
import com.ss.schedulesys.domain.Schedule;
import com.ss.schedulesys.domain.SchedulePostStatus;
import com.ss.schedulesys.domain.ScheduleStatus;
import com.ss.schedulesys.domain.ScheduleSysUser;
import com.ss.schedulesys.repository.CareCompanyRepository;
import com.ss.schedulesys.repository.EmployeeRepository;
import com.ss.schedulesys.repository.SchedulePostStatusRepository;
import com.ss.schedulesys.repository.ScheduleRepository;
import com.ss.schedulesys.repository.ScheduleStatusRepository;
import com.ss.schedulesys.repository.ScheduleSysUserRepository;
import com.ss.schedulesys.repository.ScheduleUpdateRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScheduleSysApp.class,
	properties = {"spring.profiles.active=dev"}, webEnvironment = WebEnvironment.MOCK)
public class ScheduleServiceTest {
	
	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private ScheduleSysUserRepository userRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private CareCompanyRepository careCompanyRepository;
	
	@Autowired
	private ScheduleStatusRepository scheduleStatusRepository;
	
	@Autowired
	private SchedulePostStatusRepository schedulePostStatusRepository;
	
	@Autowired
	private ScheduleRepository scheduleRepository;
	
	@Autowired
	private ScheduleUpdateRepository scheduleUpdateRepository;
	
	private ScheduleSysUser user ;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Before
	public void init(){
		user = userRepository.findOne(1l);
	}
	
	@Test
	public void testCreateSchedule_withValidData(){
		Employee employee = employeeRepository.findOne(1l);
		CareCompany careCompany = careCompanyRepository.findOne(1l);
		ScheduleStatus scheduleStatus = scheduleStatusRepository.findOne(1l);
		SchedulePostStatus schedulePostStatus = schedulePostStatusRepository.findOne(1l);
		Schedule schedule = Schedule.builder()
				.employee(employee)
				.careCompany(careCompany)
				.scheduleStatus(scheduleStatus)
				.schedulePostStatus(schedulePostStatus)
				.scheduleDate(new Date())
				.shiftStartTime("10:00AM")
				.shiftEndTime("11:00AM")
				.comment("Comment on schedule")
				.build();
		Schedule createdSchedule = scheduleService.save(schedule, user);
		assertNotNull(createdSchedule.getId());
		assertEquals(false, createdSchedule.getTimeSheetReceived());
		assertEquals(Double.valueOf(0), createdSchedule.getHoursWorked());
		log.info("Created Schedule : {}", createdSchedule);
	}
	
	@Test
	public void testCreateSchedule_withInvalidShiftTimes(){
		
	}
	
	@Test
	public void testUpdateSchedule(){
		Schedule schedule = scheduleRepository.findOne(1l);
		ScheduleStatus scheduleStatus = scheduleStatusRepository.findOne(2l);
		schedule.status(scheduleStatus).billed(true).paid(true).timeSheetReceived(true).hoursWorked(8d).overtime(2d);
		int updateEntries = scheduleUpdateRepository.findAll().size();
		scheduleService.save(schedule, user);
		Schedule updatedSchedule = scheduleRepository.findOne(1l);
		assertEquals(true, updatedSchedule.getBilled());
		assertEquals(Double.valueOf(8), updatedSchedule.getHoursWorked());
		assertEquals(Double.valueOf(2), updatedSchedule.getOvertime());
		assertEquals(updateEntries + 1, scheduleUpdateRepository.findAll().size());
	}
	
	@Test
	public void passwordEncryption(){
		System.out.println(encoder.encode("secret"));
	}
	
}
