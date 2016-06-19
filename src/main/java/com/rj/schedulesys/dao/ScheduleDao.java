package com.rj.schedulesys.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.Schedule;

@Repository
public class ScheduleDao extends GenericDao<Schedule> {
	
	public ScheduleDao() {
		setClazz(Schedule.class);
	}
	
	public List<Schedule> findByAssigneeId(Long id, Date scheduleDate){
		List<Schedule> schedules = entityManager.createQuery(
				"from Schedule s where s.assignee.id =:id and s.scheduleDate =:scheduleDate", Schedule.class)
				.setParameter("id", id)
				.setParameter("scheduleDate", scheduleDate)
				.getResultList();
		return schedules;
	}
	
	public Schedule findByIdAndAssigneeId(Long id, Long assigneeId){
		Schedule schedule = entityManager.createQuery(
				"from Schedule s where s.id =:id and s.assignee.id =:assigneeId", Schedule.class)
				.setParameter("id", id)
				.setParameter("assigneeId", assigneeId)
				.getSingleResult();
		return schedule;
	}
	
	public Schedule findByAssigneeNameAndShiftNameAndScheduleDate(String firstname, String lastname, String shiftName, Date scheduleDate){
		Schedule schedule = entityManager.createQuery(
				"from Schedule s where s.assignee.firstName =:firstname "
				+ "and s.assignee.lastName =:lastname "
				+ "and s.shift.shiftName =:shiftName "
				+ "and s.scheduleDate =:scheduleDate", Schedule.class)
				.setParameter("firstname", firstname)
				.setParameter("lastname", lastname)
				.setParameter("shiftName", shiftName)
				.setParameter("scheduleDate", scheduleDate)
				.getSingleResult();
		return schedule;
	}
	
	public List<Schedule> findAllBetweenDatesByFacilityId(Date startDate, Date endDate, Long facilityId){
		List<Schedule> schedules = entityManager.createQuery(
				"from Schedule s where s.facility.id =:facilityId and s.scheduleDate between :startDate and :endDate"
				, Schedule.class)
				.setParameter("facilityId", facilityId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		return schedules;
	}
	
	public Schedule findByIdAndFacilityId(Long id, Long facilityId){
		Schedule schedule = entityManager.createQuery(
				"from Schedule s where s.id =:id and s.facility.id =:facilityId"
				, Schedule.class)
				.setParameter("id", id)
				.setParameter("facilityId", facilityId)
				.getSingleResult();
		return schedule;
	}
	
	public List<Schedule> findByAssigneerId(Long id){
		List<Schedule> schedules = entityManager.createQuery(
				"from Schedule s where s.assigneer.id =:id", Schedule.class)
				.setParameter("id", id)
				.getResultList();
		return schedules;
	}
}