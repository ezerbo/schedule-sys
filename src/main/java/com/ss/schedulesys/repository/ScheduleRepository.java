package com.ss.schedulesys.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.schedulesys.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	/**
	 * 	Checks if an employee has a duplicate schedule
	 * 
	 * @param employeeId
	 * @param careCompanyId
	 * @param scheduleDate
	 * @param shiftStartTimeId
	 * @param shiftEndTimeId
	 * @return
	 */
	@Query("from Schedule s where s.employee.id = :employeeId and s.careCompany.id = :careCompanyId "
			+ " and s.shiftStartTime = :shiftStartTime and s.shiftEndTime = :shiftEndTime and s.scheduleDate = :scheduleDate")
	public Schedule find(@Param("employeeId") Long employeeId, @Param("careCompanyId") Long careCompanyId,
			@Param("scheduleDate") Date scheduleDate, @Param("shiftStartTime") Date shiftStartTime, @Param("shiftEndTime") Date shiftEndTime);
	
	
	@Query("from Schedule s where s.scheduleDate = :scheduleDate and s.careCompany.careCompanyType.name = :companyType and s.archived is false")
	public List<Schedule> findAllByDateAndCompanyType(@Param("scheduleDate") Date scheduleDate, @Param("companyType") String companyType);
	
	/**
	 * @param careCompanyId
	 * @return
	 */
	@Query("from Schedule s where s.careCompany.id = :careCompanyId and s.archived is :archived")
	public List<Schedule> findAllByCareCompany(@Param("careCompanyId") Long careCompanyId, @Param("archived") boolean archived, Sort sort);
	
	
	/**
	 *  Get schedules for an employee. 
	 *  
	 * @param employeeId
	 * @return schedules assigned to employee with id 'employeeId'
	 */
	@Query("from Schedule s where s.employee.id = :employeeId and s.archived is false")
	public List<Schedule> findAllByEmployee(@Param("employeeId") Long employeeId);
	
	/**
	 * @param scheduleDate
	 * @return
	 */
	@Query("from Schedule s where s.scheduleDate = :scheduleDate and s.archived is false")
	public List<Schedule> findAllByScheduleDate(@Param("scheduleDate") Date scheduleDate);
	
	/**
	 * @param startDate
	 * @param endDate
	 * @param companyType
	 * @return
	 */
	@Query("from Schedule s where s.scheduleDate between :startDate and :endDate and lower(s.careCompany.careCompanyType.name) = lower(:companyType) and s.archived is false")
	public List<Schedule> findAllByCompanyTypeAndScheduleDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("companyType") String companyType);
	
	/**
	 * @param scheduleDate
	 */
	@Query("from Schedule s where s.archived is false and s.scheduleDate < :scheduleDate and s.billed is true and s.paid is true and  s.timeSheetReceived is true and hoursWorked <> 0")
	public List<Schedule> oldSchedules(@Param("scheduleDate") Date scheduleDate);
}