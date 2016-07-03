package com.rj.schedulesys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * The persistent class for the NURSE_TEST database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="NURSE_TEST")
public class NurseTest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private NurseTestPK id;
	
	@Column(name = "STATUS")
	private String status;

	@Temporal(TemporalType.DATE)
	@Column(name = "COMPLETED_DATE")
	private Date completedDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NURSE_ID", nullable = false, insertable = false, updatable = false)
	private Nurse nurse;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEST_SUB_CATEGORY_ID")
	private TestSubCategory testSubCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEST_ID", nullable = false, insertable = false, updatable = false)
	private Test test;

}