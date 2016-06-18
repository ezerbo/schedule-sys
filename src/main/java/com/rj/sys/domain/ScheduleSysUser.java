package com.rj.sys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * The persistent class for the SCHEDULE_SYS_USER database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="SCHEDULE_SYS_USER")
public class ScheduleSysUser implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID", unique=true, nullable=false)
	private Long id;

	@Column(name="PASSWORDHASH", nullable=false, length=254)
	private String passwordhash;

	@Column(name="USERNAME", nullable=false, length=50)
	private String username;

	@OneToMany(mappedBy="scheduleSysUser")
	private List<Schedule> schedules;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ROLE_ID", nullable=false)
	private UserRole userRole;

	@OneToMany(mappedBy="scheduleSysUser")
	private List<ScheduleUpdate> scheduleUpdates;


	public Schedule addSchedule(Schedule schedule) {
		getSchedules().add(schedule);
		schedule.setScheduleSysUser(this);
		return schedule;
	}

	public Schedule removeSchedule(Schedule schedule) {
		getSchedules().remove(schedule);
		schedule.setScheduleSysUser(null);
		return schedule;
	}

	public ScheduleUpdate addScheduleUpdate(ScheduleUpdate scheduleUpdate) {
		getScheduleUpdates().add(scheduleUpdate);
		scheduleUpdate.setScheduleSysUser(this);
		return scheduleUpdate;
	}

	public ScheduleUpdate removeScheduleUpdate(ScheduleUpdate scheduleUpdate) {
		getScheduleUpdates().remove(scheduleUpdate);
		scheduleUpdate.setScheduleSysUser(null);
		return scheduleUpdate;
	}

}