package com.rj.schedulesys.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the SCHEDULE_POST_STATUS database table.
 * 
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="SCHEDULE_POST_STATUS")
public class SchedulePostStatus implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "STATUS", nullable = false, length = 20)
	private String status;
	
	@OneToMany(mappedBy="schedulePostStatus")
	private List<FacilitySchedule> schedules;
	
	public FacilitySchedule addSchedule(FacilitySchedule schedule) {
		getSchedules().add(schedule);
		schedule.setSchedulePostStatus(this);
		return schedule;
	}

	public FacilitySchedule removeSchedule(FacilitySchedule schedule) {
		getSchedules().remove(schedule);
		schedule.setSchedulePostStatus(null);
		return schedule;
	}

}