package com.rj.schedulesys.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.PrivateCareSchedule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PrivateCareScheduleDao extends GenericDao<PrivateCareSchedule>{

	public PrivateCareScheduleDao() {
		setClazz(PrivateCareSchedule.class);
	}
	
	/**
	 * @param careGiverId
	 * @param shiftId
	 * @param date
	 * @return
	 */
	public PrivateCareSchedule findByEmployeeAndShiftAndDate(Long employeeId, Long startShiftId, Long endShiftId, Date date){
		PrivateCareSchedule schedule = null; 
		try {
			schedule = entityManager.createQuery(
					"from PrivateCareSchedule pcs where pcs.employee.id =:employeeId "
							+ "and pcs.startShift.id =:startShiftId "
							+ "and pcs.endShift.id =:endShiftId "
							+ "and pcs.scheduleDate =:scheduleDate", PrivateCareSchedule.class)
					.setParameter("employeeId", employeeId)
					.setParameter("startShiftId", startShiftId)
					.setParameter("endShiftId", endShiftId)
					.setParameter("scheduleDate", date)
					.getSingleResult();
		} catch (NoResultException e) {
			log.warn("No schedule found with employeeId : {}, startShiftId : {} , endShiftId : {}and date : {}",
					employeeId, startShiftId, endShiftId, date);
		}
		return schedule;
	}
	
	public List<PrivateCareSchedule> findAllByDatesAndUser(Date startDate, Date endDate, String username){
		List<PrivateCareSchedule> schedules = entityManager.createQuery(
				"from PrivateCareSchedule pcs where pcs.scheduleDate between :startDate "
				+ "and :endDate and pcs.scheduleSysUser.username =:username order by pcs.scheduleDate asc"
				, PrivateCareSchedule.class)
				.setParameter("username", username)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		return schedules;
	}
	
	public List<PrivateCareSchedule> findAllBetweenDatesByPrivateCare(Date startDate, Date endDate, Long privateCareId){
		List<PrivateCareSchedule> schedules = entityManager.createQuery(
				"from PrivateCareSchedule pcs where pcs.privateCare.id =:privateCareId "
				+ "and pcs.scheduleDate between :startDate and :endDate order by pcs.scheduleDate asc"
				, PrivateCareSchedule.class)
				.setParameter("privateCareId", privateCareId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		return schedules;
	}
	
	public List<PrivateCareSchedule> findAllByPrivateCare(Long privateCareId){
		List<PrivateCareSchedule> schedules = entityManager.createQuery(
				"from PrivateCareSchedule pcs where fs.privateCare.id =:privateCareId order by pcs.scheduleDate asc"
				, PrivateCareSchedule.class)
				.setParameter("privateCareId", privateCareId)
				.getResultList();
		return schedules;
	}
	
	public List<PrivateCareSchedule> findAllBetweenDatesByEmployeeId(Date startDate, Date endDate, Long employeeId){
		List<PrivateCareSchedule> schedules = entityManager.createQuery(
				"from PrivateCareSchedule pcs where pcs.employee.id =:employeeId "
				+ "and pcs.scheduleDate between :startDate and :endDate order by pcs.scheduleDate asc"
				, PrivateCareSchedule.class)
				.setParameter("employeeId", employeeId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		return schedules;
	}
	
	public List<PrivateCareSchedule> findAllByEmployeeId(Long employeeId){
		List<PrivateCareSchedule> schedules = entityManager.createQuery(
				"from PrivateCareSchedule pcs where pcs.employee.id =:employeeId order by pcs.scheduleDate asc"
				, PrivateCareSchedule.class)
				.setParameter("employeeId", employeeId)
				.getResultList();
		return schedules;
	}
	
}
