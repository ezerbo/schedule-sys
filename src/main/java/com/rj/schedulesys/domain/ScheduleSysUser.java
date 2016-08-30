package com.rj.schedulesys.domain;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.validator.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;


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

	@Column(name="PASSWORDHASH", length=254)
	private String passwordhash;

	@Column(name="USERNAME", nullable=false, length=50)
	private String username;
	
	@Email
	@Column(name="EMAIL_ADDRESS", nullable = false, length = 100)
	private String emailAddress;
	
	@Column(name="ACTIVATION_TOKEN", nullable = false, length = 254)
	private String activationToken;
	
	@Column(name="IS_ACTIVATED", nullable = false)
	private Boolean isActivated;

	@OneToMany(mappedBy="scheduleSysUser")
	private List<Schedule> schedules;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ROLE_ID", nullable=false)
	private UserRole userRole;

	@OneToMany(mappedBy="scheduleSysUser")
	private List<ScheduleUpdate> scheduleUpdates;
	
	@PrePersist
	public void onPersist(){
		setIsActivated(Boolean.FALSE);
		setActivationToken(UUID.randomUUID().toString());
	}

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