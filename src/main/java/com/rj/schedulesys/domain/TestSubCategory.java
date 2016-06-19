package com.rj.schedulesys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * The persistent class for the TEST_SUB_CATEGORY database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TEST_SUB_CATEGORY")
public class TestSubCategory implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID", unique=true, nullable=false)
	private Long id;

	@Column(name="NAME", nullable=false, length=50)
	private String name;

	@OneToMany(mappedBy="testSubCategory")
	private List<NurseTest> nurseTests;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TEST_ID", nullable=false)
	private Test test;

	public NurseTest addNurseTest(NurseTest nurseTest) {
		getNurseTests().add(nurseTest);
		nurseTest.setTestSubCategory(this);
		return nurseTest;
	}

	public NurseTest removeNurseTest(NurseTest nurseTest) {
		getNurseTests().remove(nurseTest);
		nurseTest.setTestSubCategory(null);
		return nurseTest;
	}
}