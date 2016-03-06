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
import lombok.ToString;


/**
 * The persistent class for the position database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "POSITION")
@ToString(exclude = {"users"})
public class Position implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public final static String N_POSITION = "NURSE";
	public final static String CG_POSITION = "CAREGIVER";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	private String name;
	
	@Column(name="POSITION_TYPE")
	private String positionType;
	
	@OneToMany(mappedBy="position")
	private List<User> users;
	
	public User addUser(User user) {
		getUsers().add(user);
		user.setPosition(this);

		return user;
	}
	
	public User removeUser(User user) {
		getUsers().remove(user);
		user.setPosition(null);
		
		return user;
	}
	
}