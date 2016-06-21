package com.rj.schedulesys.dao;

import org.joda.time.LocalTime;
import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.Shift;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ShiftDao extends GenericDao<Shift> {
	
	public ShiftDao() {
		setClazz(Shift.class);
	}
	
	/**
	 * @param name
	 * @return
	 */
	public Shift findByName(String name){
		
		Shift shift = null;
		
		try{
			shift = entityManager.createQuery(
					"from Shift s where s.name =:name", Shift.class)
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
	public Shift findByStartAndEndTime(LocalTime startTime, LocalTime endTime){
		
		Shift shift = null;
		
		try{
			shift = entityManager.createQuery(
					"from Shift s where s.startTime =:startTime and s.endTime =:endTime", Shift.class)
					.setParameter("startTime", startTime)
					.setParameter("endTime", endTime)
					.getSingleResult();
		}catch(Exception e){
			log.warn("No shift found with start time : {} and end time : {}", startTime, endTime);
		}
		
		return shift;
	}
}
