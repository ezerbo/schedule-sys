package com.ss.schedulesys.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ezerbo
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleUpdateId implements java.io.Serializable {

	private static final long serialVersionUID = -8360989746520392638L;
	
	@Column(name = "schedule_id", nullable = false)
	private Long scheduleId;
	@Column(name = "user_id", nullable = false)
	private Long userId;


	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ScheduleUpdateId))
			return false;
		ScheduleUpdateId castOther = (ScheduleUpdateId) other;

		return (this.getScheduleId() == castOther.getScheduleId()) && (this.getUserId() == castOther.getUserId());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getScheduleId().intValue();
		result = 37 * result + this.getUserId().intValue();
		return result;
	}

}
