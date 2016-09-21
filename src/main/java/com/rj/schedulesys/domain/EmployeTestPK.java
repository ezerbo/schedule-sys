package com.rj.schedulesys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The primary key class for the EMPLOYEE_TEST database table.
 * @author ezerbo
 */
@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class EmployeTestPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name="TEST_ID", insertable=false, updatable=false, nullable=false)
	private Long testId;

	@Column(name="employee_ID", insertable=false, updatable=false, nullable=false)
	private Long employeeId;
	

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof EmployeTestPK)) {
			return false;
		}
		EmployeTestPK castOther = (EmployeTestPK)other;
		return 
			(this.testId == castOther.testId)
			&& (this.employeeId == castOther.employeeId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.testId.intValue();
		hash = hash * prime + this.employeeId.intValue();
		
		return hash;
	}
}