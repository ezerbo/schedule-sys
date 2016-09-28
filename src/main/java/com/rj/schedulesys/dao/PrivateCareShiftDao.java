package com.rj.schedulesys.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.PrivateCareShift;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class PrivateCareShiftDao extends GenericDao<PrivateCareShift>{

	public PrivateCareShiftDao() {
		setClazz(PrivateCareShift.class);
	}
	
	/**
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public PrivateCareShift findByStartAndEndTime(String startTime, String endTime){
		PrivateCareShift shift = null;
		try{
			shift = entityManager.createQuery(
					"from PrivateCareShift pcs where pcs.startTime =:startTime and pcs.endTime =:endTime", PrivateCareShift.class)
					.setParameter("startTime", startTime)
					.setParameter("endTime", endTime)
					.getSingleResult();
		}catch(Exception e){
			log.warn("No shift found with start time : {} and end time : {}", startTime, endTime);
		}
		return shift;
	}
	
	public List<PrivateCareShift> findAll(){
		List<PrivateCareShift> shifts = entityManager.createQuery(
				"from PrivateCareShift pcs order by pcs.id asc", PrivateCareShift.class)
				.getResultList();
		return shifts;
	}
}
