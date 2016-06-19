package com.rj.schedulesys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * The persistent class for the USER_ROLE database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="USER_ROLE")
public class UserRole implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID", unique=true, nullable=false)
	private Long id;

	@Column(name="USER_ROLE", nullable=false, length=50)
	private String userRole;

	@OneToMany(mappedBy="userRole")
	private List<ScheduleSysUser> scheduleSysUsers;


	public ScheduleSysUser addScheduleSysUser(ScheduleSysUser scheduleSysUser) {
		getScheduleSysUsers().add(scheduleSysUser);
		scheduleSysUser.setUserRole(this);
		return scheduleSysUser;
	}

	public ScheduleSysUser removeScheduleSysUser(ScheduleSysUser scheduleSysUser) {
		getScheduleSysUsers().remove(scheduleSysUser);
		scheduleSysUser.setUserRole(null);
		return scheduleSysUser;
	}

}