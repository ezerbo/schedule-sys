package com.rj.schedulesys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.List;


/**
 * The persistent class for the SHIFT database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="SHIFT")
public class Shift implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "END_TIME", nullable = false)
	private Time endTime;

	@Column(name = "NAME", nullable = false, length = 30)
	private String name;

	@Column(name = "START_TIME", nullable = false)
	private Time startTime;

	@OneToMany(mappedBy = "shift")
	private List<Schedule> schedules;

	public Schedule addSchedule(Schedule schedule) {
		getSchedules().add(schedule);
		schedule.setShift(this);
		return schedule;
	}

	public Schedule removeSchedule(Schedule schedule) {
		getSchedules().remove(schedule);
		schedule.setShift(null);
		return schedule;
	}

}