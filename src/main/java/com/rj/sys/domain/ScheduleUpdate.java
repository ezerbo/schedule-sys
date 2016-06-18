package com.rj.sys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * The persistent class for the SCHEDULE_UPDATE database table.
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

	@Column(name="`COMMENT`", nullable=false, length=254)
	private String comment;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="UPDATE_TIME", nullable=false)
	private Date updateTime;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="USER_ID", nullable=false, insertable=false, updatable=false)
	private ScheduleSysUser scheduleSysUser;

}