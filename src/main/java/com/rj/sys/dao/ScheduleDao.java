package com.rj.sys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.Schedule;

@Repository
public class ScheduleDao extends GenericDao<Schedule> {
	
	public ScheduleDao() {
		setClazz(Schedule.class);
	}
	
	public List<Schedule> findByAssigneeId(Long id){
		List<Schedule> schedules = entityManager.createQuery(
				"from Schedule s where s.assignee.id =:id", Schedule.class)
				.setParameter("id", id)
				.getResultList();
		return schedules;
	}
	
	public List<Schedule> findByAssigneerId(Long id){
		List<Schedule> schedules = entityManager.createQuery(
				"from Schedule s where s.assigneer.id =:id", Schedule.class)
				.setParameter("id", id)
				.getResultList();
		return schedules;
	}
}
