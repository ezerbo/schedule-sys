package com.rj.sys.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the user_test database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="USER_TEST")
public class UserTest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private UserTestPK id;
	
	@Temporal(TemporalType.DATE)
	@Column(name="COMPLETED_DATE")
	private Date completedDate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="EXPIRY_DATE")
	private Date expiryDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private Test test;
	
	@ManyToOne(fetch=FetchType.LAZY)
	private User user;
	
}