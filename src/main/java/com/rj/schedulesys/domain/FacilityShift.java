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

import org.hibernate.annotations.Type;
import org.joda.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * The persistent class for the FACILITY_SHIFT database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="FACILITY_SHIFT")
@ToString(exclude = "schedules")
public class FacilityShift implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "NAME", nullable = false, length = 30)
	private String name;
	
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
	@Column(name = "END_TIME", nullable = false)
	private LocalTime endTime;

	
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalTime")
	@Column(name = "START_TIME", nullable = false)
	private LocalTime startTime;

	@OneToMany(mappedBy = "shift")
	private List<FacilitySchedule> schedules;

	public FacilitySchedule addSchedule(FacilitySchedule schedule) {
		getSchedules().add(schedule);
		schedule.setShift(this);
		return schedule;
	}

	public FacilitySchedule removeSchedule(FacilitySchedule schedule) {
		getSchedules().remove(schedule);
		schedule.setShift(null);
		return schedule;
	}

}