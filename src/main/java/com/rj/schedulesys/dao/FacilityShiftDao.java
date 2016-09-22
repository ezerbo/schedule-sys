package com.rj.schedulesys.dao;

import org.joda.time.LocalTime;
import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.FacilityShift;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class FacilityShiftDao extends GenericDao<FacilityShift> {
	
	public FacilityShiftDao() {
		setClazz(FacilityShift.class);
	}
	
	/**
	 * @param name
	 * @return
	 */
	public FacilityShift findByName(String name){
		FacilityShift shift = null;
		try{
			shift = entityManager.createQuery(
					"from FacilityShift fs where fs.name =:name", FacilityShift.class)
					.setParameter("name", name)
					.getSingleResult();
		}catch(Exception e){
			log.warn("No shift found with name : {}", name);
		}
		return shift;
	}
	
	/**
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public FacilityShift findByStartAndEndTime(LocalTime startTime, LocalTime endTime){
		FacilityShift shift = null;
		try{
			shift = entityManager.createQuery(
					"from FacilityShift fs where fs.startTime =:startTime and fs.endTime =:endTime", FacilityShift.class)
					.setParameter("startTime", startTime)
					.setParameter("endTime", endTime)
					.getSingleResult();
		}catch(Exception e){
			log.warn("No shift found with start time : {} and end time : {}", startTime, endTime);
		}
		return shift;
	}
}
