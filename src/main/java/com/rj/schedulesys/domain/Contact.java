package com.rj.schedulesys.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the CONTACT database table.
 * 
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="CONTACT")
public class Contact implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "FIRST_NAME", nullable = false, length = 50)
	private String firstName;
	
	@Column(name = "LAST_NAME", nullable = false, length = 50)
	private String lastName;
	
	@Column(name = "TITLE", nullable = false, length = 50)
	private String title;
	
	@Column(name = "PHONENUMBER", nullable = false, length = 10)
	private String phoneNumber;
	
	@Column(name = "FAX", nullable = false, length = 10)
	private String fax;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRIVATE_CARE_ID", nullable = false)
	private PrivateCare privateCare;
}
