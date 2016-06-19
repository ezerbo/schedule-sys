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
 * The persistent class for the FACILITY database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="FACILITY")
@ToString(exclude = {"schedules", "staffMembers"})
public class Facility implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID", unique=true, nullable=false)
	private Long id;

	@Column(name="ADDRESS", nullable=false, length=50)
	private String address;

	@Column(name="FAX", nullable=false, length=50)
	private String fax;

	@Column(name="NAME", nullable=false, length=50)
	private String name;

	@Column(name="PHONE_NUMBER", nullable=false, length=50)
	private String phoneNumber;

	@OneToMany(mappedBy="facility")
	private List<Schedule> schedules;

	@OneToMany(mappedBy="facility")
	private List<StaffMember> staffMembers;

	public StaffMember addStaffMember(StaffMember staffMember) {
		getStaffMembers().add(staffMember);
		staffMember.setFacility(this);
		return staffMember;
	}

	public StaffMember removeStaffMember(StaffMember staffMember) {
		getStaffMembers().remove(staffMember);
		staffMember.setFacility(null);
		return staffMember;
	}

}