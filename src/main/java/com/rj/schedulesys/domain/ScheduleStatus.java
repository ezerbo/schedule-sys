package com.rj.schedulesys.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the SCHEDULE_STATUS database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="SCHEDULE_STATUS")
public class ScheduleStatus implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "STATUS", nullable = false, length = 20)
	private String status;
	
	@OneToMany(mappedBy="scheduleStatus")
	private List<FacilitySchedule> schedules;
	
	public FacilitySchedule addSchedule(FacilitySchedule schedule) {
		getSchedules().add(schedule);
		schedule.setScheduleStatus(this);
		return schedule;
	}

	public FacilitySchedule removeSchedule(FacilitySchedule schedule) {
		getSchedules().remove(schedule);
		schedule.setScheduleStatus(null);
		return schedule;
	}

}