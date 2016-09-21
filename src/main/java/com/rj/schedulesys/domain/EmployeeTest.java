package com.rj.schedulesys.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * The persistent class for the EMPLOYEE_TEST database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="EMPLOYEE_TEST")
@ToString(exclude = {"employee", "test", "testSubCategory"})
public class EmployeeTest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private EmployeTestPK id;
	
	@Column(name = "STATUS")
	private String status;

	@Temporal(TemporalType.DATE)
	@Column(name = "COMPLETED_DATE")
	private Date completedDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "EMPLOYEE_ID", nullable = false, insertable = false, updatable = false)
	private Employee employee;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEST_SUB_CATEGORY_ID")
	private TestSubCategory testSubCategory;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEST_ID", nullable = false, insertable = false, updatable = false)
	private Test test;

}