package com.rj.sys.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	
	@Column(name="UPDATE_COMMENT")
	private String updateComment;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME")
	private Date updateTime;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Schedule schedule;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;

}