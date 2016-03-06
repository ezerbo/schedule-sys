package com.rj.sys.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the shift database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SHIFT")
public class Shift implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="END_TIME")
	private Date endTime;
	
	@Column(name="SHIFT_NAME")
	private String shiftName;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="START_TIME")
	private Date startTime;
	
	@OneToMany(mappedBy="shift")
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