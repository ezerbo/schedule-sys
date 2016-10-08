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
	public PrivateCareSchedule findByCareGiverAndShiftAndDate(Long careGiverId, Long shiftId, Date date){
		PrivateCareSchedule schedule = null; 
		try {
			schedule = entityManager.createQuery(
					"from PrivateCareSchedule pcs where pcs.careGiver.id =:careGiverId "
							+ "and pcs.shift.id =:shiftId "
							+ "and pcs.scheduleDate =:scheduleDate", PrivateCareSchedule.class)
					.setParameter("careGiverId", careGiverId)
					.setParameter("shiftId", shiftId)
					.setParameter("scheduleDate", date)
					.getSingleResult();
		} catch (NoResultException e) {
			log.warn("No schedule found with careGiverId : {}, shiftId : {} and date : {}", careGiverId, shiftId, date);
		}
		return schedule;
	}
	
	public List<PrivateCareSchedule> findAllByDatesAndUser(Date startDate, Date endDate, String username){
		List<PrivateCareSchedule> schedules = entityManager.createQuery(
				"from PrivateCareSchedule pcs where pcs.scheduleDate between :startDate and :endDate and pcs.scheduleSysUser.username =:username"
				, PrivateCareSchedule.class)
				.setParameter("username", username)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		return schedules;
	}
	
	public List<PrivateCareSchedule> findAllBetweenDatesByPrivateCare(Date startDate, Date endDate, Long privateCareId){
		List<PrivateCareSchedule> schedules = entityManager.createQuery(
				"from PrivateCareSchedule pcs where pcs.privateCare.id =:privateCareId and pcs.scheduleDate between :startDate and :endDate"
				, PrivateCareSchedule.class)
				.setParameter("privateCareId", privateCareId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		return schedules;
	}
	
	public List<PrivateCareSchedule> findAllByPrivateCare(Long privateCareId){
		List<PrivateCareSchedule> schedules = entityManager.createQuery(
				"from PrivateCareSchedule pcs where fs.privateCaere.id =:privateCareId"
				, PrivateCareSchedule.class)
				.setParameter("privateCareId", privateCareId)
				.getResultList();
		return schedules;
	}
	
	public List<PrivateCareSchedule> findAllBetweenDatesByCareGiver(Date startDate, Date endDate, Long careGiverId){
		List<PrivateCareSchedule> schedules = entityManager.createQuery(
				"from PrivateCareSchedule pcs where pcs.careGiver.id =:careGiverId and pcs.scheduleDate between :startDate and :endDate"
				, PrivateCareSchedule.class)
				.setParameter("careGiverId", careGiverId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		return schedules;
	}
	
	public List<PrivateCareSchedule> findAllByCareGiver(Long careGiverId){
		List<PrivateCareSchedule> schedules = entityManager.createQuery(
				"from FacilitySchedule fs where fs.nurse.id =:nurseId"
				, PrivateCareSchedule.class)
				.setParameter("careGiverId", careGiverId)
				.getResultList();
		return schedules;
	}
	
}
