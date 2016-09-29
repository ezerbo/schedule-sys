package com.rj.schedulesys.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * The persistent class for the PRIVATE_CARE_SCHEDULE_UPDATE database table.
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="PRIVATE_CARE_SCHEDULE_UPDATE")
@ToString(exclude = {"scheduleSysUser"})
public class PrivateCareScheduleUpdate implements Serializable{

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PrivateCareScheduleUpdatePK id;

	@Column(name = "UPDATE_TIME", nullable = false)
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime updateTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SCHEDULE_ID", nullable = false, insertable = false, updatable = false)
	private PrivateCareSchedule schedule;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false, insertable = false, updatable = false)
	private ScheduleSysUser scheduleSysUser;
	
	@PrePersist
	public void onPersist(){
		setUpdateTime(new DateTime());
	}
	
	@PreUpdate
	public void onUpdate(){
		setUpdateTime(new DateTime());
	}
}