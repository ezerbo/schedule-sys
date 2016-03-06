package com.rj.sys.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The primary key class for the user_test database table.
 * 
 */
@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class UserTestPK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name="TEST_ID", insertable=false, updatable=false)
	private int testId;
	
	@Column(name="USER_ID", insertable=false, updatable=false)
	private int userId;

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof UserTestPK)) {
			return false;
		}
		UserTestPK castOther = (UserTestPK)other;
		return 
			(this.testId == castOther.testId)
			&& (this.userId == castOther.userId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.testId;
		hash = hash * prime + this.userId;
		
		return hash;
	}
}