package com.rj.schedulesys.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The primary key class for the PRIVATE_SCHEDULE_UPDATE database table.
 * @author ezerbo
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PrivateCareScheduleUpdatePK implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name="SCHEDULE_ID", nullable=false)
	private Long scheduleId;

	@Column(name="USER_ID", nullable=false)
	private Long userId;


	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ScheduleUpdatePK)) {
			return false;
		}
		PrivateCareScheduleUpdatePK castOther = (PrivateCareScheduleUpdatePK)other;
		return 
			(this.scheduleId == castOther.scheduleId)
			&& (this.userId == castOther.userId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.scheduleId.intValue();
		hash = hash * prime + this.userId.intValue();
		
		return hash;
	}
}
