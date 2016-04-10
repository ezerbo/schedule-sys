package com.rj.sys.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the schedule_update database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="SCHEDULE_UPDATE")
public class ScheduleUpdate implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ScheduleUpdatePK id;
	
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name="UPDATE_TIME")
	private DateTime updateTime;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SCHEDULE_ID", referencedColumnName = "ID", insertable = false, updatable = false)
	private Schedule schedule;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
	private User user;

}