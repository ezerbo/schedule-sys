package com.rj.schedulesys.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * The persistent class for the TEST database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TEST")
@ToString(exclude = {"employeeTests", "testSubCategories"})
public class Test implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name= "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "ALLOW_NOT_APPLICABLE", nullable = false)
	private Boolean allowNotApplicable;

	@Column(name = "HAS_COMPLETED_DATE", nullable = false)
	private Boolean hasCompletedDate;

	@Column(name = "HAS_EXPIRATION_DATE", nullable = false)
	private Boolean hasExpirationDate;

	@Column(name = "NAME", nullable = false, length = 50)
	private String name;

	@OneToMany(mappedBy = "test")
	private List<EmployeeTest> employeeTests;

	@OneToMany(mappedBy = "test")
	private List<TestSubCategory> testSubCategories = new ArrayList<>();


	public EmployeeTest addNurseTest(EmployeeTest nurseTest) {
		getEmployeeTests().add(nurseTest);
		nurseTest.setTest(this);
		return nurseTest;
	}

	public EmployeeTest removeNurseTest(EmployeeTest nurseTest) {
		getEmployeeTests().remove(nurseTest);
		nurseTest.setTest(null);
		return nurseTest;
	}

	public TestSubCategory addTestSubCategory(TestSubCategory testSubCategory) {
		getTestSubCategories().add(testSubCategory);
		testSubCategory.setTest(this);
		return testSubCategory;
	}

	public TestSubCategory removeTestSubCategory(TestSubCategory testSubCategory) {
		getTestSubCategories().remove(testSubCategory);
		testSubCategory.setTest(null);
		return testSubCategory;
	}

}