package com.rj.sys.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * The persistent class for the user database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER")
@ToString(exclude = {
		"assignersSchedules","assigneesSchedules"
		,"licenses","scheduleUpdates","userTests"
		,"userType","position"})
public class User implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public final static String ADMIN_USER = "ADNIN";
	public final static String SIMPLE_USER = "NON_ADMIN";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="CPR")
	private String cpr;
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATE_OF_HIRE")
	private Date dateOfHire;
	
	@Column(name="EBC")
	private Boolean ebc;
	
	@Column(name="FIRST_NAME")
	private String firstName;
	
	@Column(name="LAST_NAME")
	private String lastName;
	
	@Column(name="EMAIL_ADDRESS")
	private String emailAddress;
	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_DATE_OF_HIRE")
	private Date lastDateOfHire;
	
	@Column(name="OTHER_PHONE_NUMBER")
	private String otherPhoneNumber;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="PRIMARY_PHONE_NUMBER")
	private String primaryPhoneNumber;
	
	@Temporal(TemporalType.DATE)
	@Column(name="REHIRE_DATE")
	private Date rehireDate;
	
	@Column(name="SECONDARY_PHONE_NUMBER")
	private String secondaryPhoneNumber;
	
	@Column(name="USERNAME")
	private String username;
	
	@Column(name="ISDELETED")
	private boolean isDeleted;
	
	@OneToMany(mappedBy="user")
	private List<License> licenses;
	
	@OneToMany(mappedBy="assigner")
	private List<Schedule> assignersSchedules;
	
	@OneToMany(mappedBy="assignee")
	private List<Schedule> assigneesSchedules;
	
	@OneToMany(mappedBy="user")
	private List<ScheduleUpdate> scheduleUpdates;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Position position;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TYPE_ID")
	private UserType userType;
	
	@OneToMany(mappedBy="user")
	private List<UserTest> userTests;

	
	public License addLicens(License license) {
		getLicenses().add(license);
		license.setUser(this);

		return license;
	}

	public License removeLicense(License license) {
		getLicenses().remove(license);
		license.setUser(null);

		return license;
	}

	public Schedule addSchedules1(Schedule schedules1) {
		getAssignersSchedules().add(schedules1);
		schedules1.setAssigner(this);

		return schedules1;
	}

	public Schedule removeSchedules1(Schedule schedules1) {
		getAssignersSchedules().remove(schedules1);
		schedules1.setAssigner(null);

		return schedules1;
	}

	public Schedule addSchedules2(Schedule schedules2) {
		getAssigneesSchedules().add(schedules2);
		schedules2.setAssignee(this);

		return schedules2;
	}

	public Schedule removeSchedules2(Schedule schedules2) {
		getAssigneesSchedules().remove(schedules2);
		schedules2.setAssignee(null);

		return schedules2;
	}

	public ScheduleUpdate addScheduleUpdate(ScheduleUpdate scheduleUpdate) {
		getScheduleUpdates().add(scheduleUpdate);
		scheduleUpdate.setUser(this);

		return scheduleUpdate;
	}

	public ScheduleUpdate removeScheduleUpdate(ScheduleUpdate scheduleUpdate) {
		getScheduleUpdates().remove(scheduleUpdate);
		scheduleUpdate.setUser(null);

		return scheduleUpdate;
	}

	public UserTest addUserTest(UserTest userTest) {
		getUserTests().add(userTest);
		userTest.setUser(this);

		return userTest;
	}

	public UserTest removeUserTest(UserTest userTest) {
		getUserTests().remove(userTest);
		userTest.setUser(null);

		return userTest;
	}

}