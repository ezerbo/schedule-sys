package com.rj.sys.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	
	public final static String NURSE_POSITION = "NURSE";
	public final static String CAREGIVER_POSITION = "CAREGIVER";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TYPE_ID")
	private PositionType positionType;
	
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