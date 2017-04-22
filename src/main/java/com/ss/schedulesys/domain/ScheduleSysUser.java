package com.ss.schedulesys.domain;
// Generated Feb 11, 2017 7:27:59 PM by Hibernate Tools 5.0.0.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * ScheduleSysUser generated by hbm2java
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"schedules", "scheduleUpdates"})
@EqualsAndHashCode(exclude = {"schedules", "scheduleUpdates"})
@Table(name = "schedule_sys_user", catalog = "schedulesys_db", uniqueConstraints = {
		@UniqueConstraint(columnNames = "email_address"), @UniqueConstraint(columnNames = "username") })
public class ScheduleSysUser implements java.io.Serializable {

	private static final long serialVersionUID = -1194882658721457752L;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(name = "fk_schedulesysuser_role"))
	private UserRole userRole;
	
	@NotNull
	@Column(name = "email_address", unique = true, nullable = false, length = 100)
	private String emailAddress;
	
	@NotNull
	@Column(name = "first_name", nullable = false, length = 100)
	private String firstName;
	
	@NotNull
	@Column(name = "last_name", nullable = false, length = 100)
	private String lastName;
	
	@Column(name = "username", unique = true, nullable = false, length = 100)
	private String username;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "password", nullable = false, length = 200)
	private String password;
	
	@NotNull
	@Column(name = "activated", nullable = false)
	private boolean activated;
	
	@JsonIgnore
	@Column(name = "activation_key", length = 200)
	private String activationKey;
	
	@JsonIgnore
	@Size(max = 20)
	@Column(name = "reset_key", length = 20)
	private String resetKey;
	
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "reset_date", nullable = true)
	private DateTime resetDate;
	
	@NotNull
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	@Column(name = "create_date", nullable = false)
	private DateTime createDate;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scheduleSysUser")
	private Set<Schedule> schedules = new HashSet<Schedule>(0);
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "scheduleSysUser")
	private Set<ScheduleUpdate> scheduleUpdates = new HashSet<ScheduleUpdate>(0);
	
	@PrePersist
	public void onCreate(){
		createDate = new DateTime();
	}
	
	public ScheduleSysUser password(String password){
		this.password = password;
		return this;
	}
	
	public ScheduleSysUser activationKey(String activationKey){
		this.activationKey = activationKey;
		return this;
	}
	
	public ScheduleSysUser resetKey(String resetKey){
		this.resetKey = resetKey;
		return this;
	}
	
	public ScheduleSysUser resetDate(DateTime resetDate){
		this.resetDate = resetDate;
		return this;
	}

}