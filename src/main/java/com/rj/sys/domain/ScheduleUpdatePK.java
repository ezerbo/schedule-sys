package com.rj.sys.domain;

import java.io.Serializable;

import javax.persistence.*;

import lombok.NoArgsConstructor;

/**
 * The primary key class for the schedule_update database table.
 * 
 */

@Embeddable
@NoArgsConstructor
public class ScheduleUpdatePK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="SCHEDULE_ID", insertable=false, updatable=false)
	private int scheduleId;
	
	@Column(name="USER_ID", insertable=false, updatable=false)
	private int userId;

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ScheduleUpdatePK)) {
			return false;
		}
		ScheduleUpdatePK castOther = (ScheduleUpdatePK)other;
		return 
			(this.scheduleId == castOther.scheduleId)
			&& (this.userId == castOther.userId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.scheduleId;
		hash = hash * prime + this.userId;
		
		return hash;
	}
}