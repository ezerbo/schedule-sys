package com.rj.schedulesys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The primary key class for the NURSE_TEST database table.
 * @author ezerbo
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class NurseTestPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name="TEST_ID", insertable=false, updatable=false, unique=true, nullable=false)
	private int testId;

	@Column(name="NURSE_ID", insertable=false, updatable=false, unique=true, nullable=false)
	private int nurseId;
	

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof NurseTestPK)) {
			return false;
		}
		NurseTestPK castOther = (NurseTestPK)other;
		return 
			(this.testId == castOther.testId)
			&& (this.nurseId == castOther.nurseId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.testId;
		hash = hash * prime + this.nurseId;
		
		return hash;
	}
}