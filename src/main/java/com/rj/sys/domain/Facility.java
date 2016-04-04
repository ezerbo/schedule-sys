package com.rj.sys.domain;

import java.io.Serializable;
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


/**
 * The persistent class for the facility database table.
 * 
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FACILITY")
public class Facility implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="ADDRESS")
	private String address;
	
	@Column(name="FAX")
	private String fax;
	
	@Column(name="NAME")
	private String name;
	
	@Column(name="PHONE_NUMBER")
	private String phoneNumber;
	
	@Column(name="ISDELETED")
	private Boolean isDeleted;
	
	@OneToMany(mappedBy="facility")
	private List<Schedule> schedules;
	
	@OneToMany(mappedBy="facility")
	private List<StaffMember> staffMembers;

	public Schedule addSchedule(Schedule schedule) {
		getSchedules().add(schedule);
		schedule.setFacility(this);

		return schedule;
	}

	public Schedule removeSchedule(Schedule schedule) {
		getSchedules().remove(schedule);
		schedule.setFacility(null);

		return schedule;
	}

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