package com.rj.sys.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the schedule database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SCHEDULE")
public class Schedule implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="CREATE_DATE")
	private Date createDate;
	
	@Column(name="IS_DELETED")
	private boolean isDeleted;
	
	@Column(name="SCHEDULE_COMMENT")
	private String scheduleComment;
	
	@Temporal(TemporalType.DATE)
	@Column(name="SCHEDULE_DATE")
	private Date scheduleDate;
	
	@Column(name="TIMESHEET_RECEIVED")
	private Boolean timesheetReceived;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ASSIGNEE_ID")
	private User assignee;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Facility facility;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="POST_STATUS_ID")
	private SchedulePostStatus schedulePostStatus;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Shift shift;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="STATUS_ID")
	private ScheduleStatus scheduleStatus;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USER_ID")
	private User assigner;
	
	@OneToMany(mappedBy="schedule")
	private List<ScheduleUpdate> scheduleUpdates;
	
	@PrePersist
	public void onCreate(){
		setCreateDate(new Date());
	}
	
	public ScheduleUpdate addScheduleUpdate(ScheduleUpdate scheduleUpdate) {
		getScheduleUpdates().add(scheduleUpdate);
		scheduleUpdate.setSchedule(this);

		return scheduleUpdate;
	}
	
	public ScheduleUpdate removeScheduleUpdate(ScheduleUpdate scheduleUpdate) {
		getScheduleUpdates().remove(scheduleUpdate);
		scheduleUpdate.setSchedule(null);

		return scheduleUpdate;
	}
	
}