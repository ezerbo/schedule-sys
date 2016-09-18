package com.rj.schedulesys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


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
@ToString(exclude = {"nurseTests", "testSubCategories"})
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
	private List<NurseTest> nurseTests;

	@OneToMany(mappedBy = "test")
	private List<TestSubCategory> testSubCategories;


	public NurseTest addNurseTest(NurseTest nurseTest) {
		getNurseTests().add(nurseTest);
		nurseTest.setTest(this);
		return nurseTest;
	}

	public NurseTest removeNurseTest(NurseTest nurseTest) {
		getNurseTests().remove(nurseTest);
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