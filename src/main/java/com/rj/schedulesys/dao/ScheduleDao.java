package com.rj.schedulesys.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.Schedule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ScheduleDao extends GenericDao<Schedule> {
	
	public ScheduleDao() {
		setClazz(Schedule.class);
	}
	
	/**
	 * @param employeeId
	 * @param shiftId
	 * @param date
	 * @return
	 */
	public Schedule findByEmployeeAndShiftAndDate(Long employeeId, Long shiftId, Date date){
		Schedule schedule = null; 

		try {
			schedule = entityManager.createQuery(
					"from Schedule s where s.employee.id =:employeeId "
							+ "and s.shift.id =:shiftId "
							+ "and s.scheduleDate =:scheduleDate", Schedule.class)
					.setParameter("employeeId", employeeId)
					.setParameter("shiftId", shiftId)
					.setParameter("scheduleDate", date)
					.getSingleResult();
		} catch (NoResultException e) {
			log.warn("No schedule found with employeeId : {}, shiftId : {} and date : {}", employeeId, shiftId, date);
		}
		
		return schedule;
	}
	
	public List<Schedule> findAllBetweenDatesByFacility(Date startDate, Date endDate, Long facilityId){
		List<Schedule> schedules = entityManager.createQuery(
				"from Schedule s where s.facility.id =:facilityId and s.scheduleDate between :startDate and :endDate"
				, Schedule.class)
				.setParameter("facilityId", facilityId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		return schedules;
	}
	
	public List<Schedule> findAllByFacility(Long facilityId){
		List<Schedule> schedules = entityManager.createQuery(
				"from Schedule s where s.facility.id =:facilityId"
				, Schedule.class)
				.setParameter("facilityId", facilityId)
				.getResultList();
		return schedules;
	}
	
}