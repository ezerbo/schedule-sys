package com.rj.schedulesys.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.FacilitySchedule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class FacilityScheduleDao extends GenericDao<FacilitySchedule> {
	
	public FacilityScheduleDao() {
		setClazz(FacilitySchedule.class);
	}
	
	/**
	 * @param nurseId
	 * @param shiftId
	 * @param date
	 * @return
	 */
	public FacilitySchedule findByNurseAndShiftAndDate(Long nurseId, Long shiftId, Date date){
		FacilitySchedule schedule = null; 
		try {
			schedule = entityManager.createQuery(
					"from FacilitySchedule fs where fs.nurse.id =:nurseId "
							+ "and fs.shift.id =:shiftId "
							+ "and fs.scheduleDate =:scheduleDate", FacilitySchedule.class)
					.setParameter("nurseId", nurseId)
					.setParameter("shiftId", shiftId)
					.setParameter("scheduleDate", date)
					.getSingleResult();
		} catch (NoResultException e) {
			log.warn("No schedule found with nurseId : {}, shiftId : {} and date : {}", nurseId, shiftId, date);
		}
		return schedule;
	}
	
	public List<FacilitySchedule> findAllByDatesAndUser(Date startDate, Date endDate, String username){
		List<FacilitySchedule> schedules = entityManager.createQuery(
				"from FacilitySchedule fs where fs.scheduleDate between :startDate and :endDate "
				+ "and fs.scheduleSysUser.username =:username  order by fs.scheduleDate asc"
				, FacilitySchedule.class)
				.setParameter("username", username)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		return schedules;
	}
	
	public List<FacilitySchedule> findAllBetweenDatesByFacility(Date startDate, Date endDate, Long facilityId){
		List<FacilitySchedule> schedules = entityManager.createQuery(
				"from FacilitySchedule fs where fs.facility.id =:facilityId "
				+ "and fs.scheduleDate between :startDate and :endDate  order by fs.scheduleDate asc"
				, FacilitySchedule.class)
				.setParameter("facilityId", facilityId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		return schedules;
	}
	
	public List<FacilitySchedule> findAllByFacility(Long facilityId){
		List<FacilitySchedule> schedules = entityManager.createQuery(
				"from FacilitySchedule fs where fs.facility.id =:facilityId  order by fs.scheduleDate asc"
				, FacilitySchedule.class)
				.setParameter("facilityId", facilityId)
				.getResultList();
		return schedules;
	}
	
	public List<FacilitySchedule> findAllBetweenDatesByNurse(Date startDate, Date endDate, Long nurseId){
		List<FacilitySchedule> schedules = entityManager.createQuery(
				"from FacilitySchedule fs where fs.nurse.id =:nurseId "
				+ "and fs.scheduleDate between :startDate and :endDate  order by fs.scheduleDate asc"
				, FacilitySchedule.class)
				.setParameter("nurseId", nurseId)
				.setParameter("startDate", startDate)
				.setParameter("endDate", endDate)
				.getResultList();
		return schedules;
	}
	
	public List<FacilitySchedule> findAllByNurse(Long nurseId){
		List<FacilitySchedule> schedules = entityManager.createQuery(
				"from FacilitySchedule fs where fs.nurse.id =:nurseId  order by fs.scheduleDate asc"
				, FacilitySchedule.class)
				.setParameter("nurseId", nurseId)
				.getResultList();
		return schedules;
	}
	
}