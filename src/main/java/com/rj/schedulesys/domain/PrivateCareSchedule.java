package com.rj.schedulesys.domain;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The persistent class for the PRIVATE_CARE_SCHEDULE database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRIVATE_CARE_SCHEDULE")
@ToString(exclude = {"careGiver"/*,"facility"*/, "shift"
		, "scheduleSysUser", "scheduleStatus"})
public class PrivateCareSchedule implements Serializable{
	
	private static final long serialVersionUID  =  1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE", nullable = false)
	private Date createDate;

//	@Column(name = "HOURS")
//	private Double hours;
//
//	@Column(name = "OVERTIME")
//	private Double overtime;

	@Column(name = "SCHEDULE_COMMENT", length = 254)
	private String scheduleComment;

	@Temporal(TemporalType.DATE)
	@Column(name = "SCHEDULE_DATE", nullable = false)
	private Date scheduleDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATUS_ID")
	private ScheduleStatus scheduleStatus;

	@Column(name = "TIMESHEET_RECEIVED")
	private Boolean timesheetReceived;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CARE_GIVER_ID")
	private CareGiver careGiver;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRIVATE_CARE_ID", nullable = false)
	private PrivateCare privateCare;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SHIFT_ID", nullable = false)
	private PrivateCareShift shift;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	private ScheduleSysUser scheduleSysUser;
	
	@OneToMany(mappedBy = "schedule", orphanRemoval = true)
	List<FacilityScheduleUpdate> facilityScheduleUpdates;//TODO Replace by private care schedule update.

}
