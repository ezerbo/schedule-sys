package com.rj.sys.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.Schedule;

@Repository
public class ScheduleDao extends GenericDao<Schedule> {
	
	public ScheduleDao() {
		setClazz(Schedule.class);
	}
	
	public List<Schedule> findByAssigneeId(Long id, Date scheduleDate){
		List<Schedule> schedules = entityManager.createQuery(
				"from Schedule s where s.assignee.id =:id and s.scheduleDate =:scheduleDate", Schedule.class)
				.setParameter("id", id)
				.setParameter("scheduleDate",scheduleDate)
				.getResultList();
		return schedules;
	}
	
	public Schedule findByAssigneeIdAndShiftNameAndScheduleDate(Long id, String shiftName, Date scheduleDate){
		Schedule schedule = entityManager.createQuery(
				"from Schedule s where s.assignee.id =:id "
				+ "and s.shift.name =:shiftName "
				+ "and s.scheduleDate =:scheduleDate", Schedule.class)
				.setParameter("id", id)
				.setParameter("shift", shiftName)
				.setParameter("scheduleDate", scheduleDate)
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
