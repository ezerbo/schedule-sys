package com.rj.schedulesys.domain;

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
 * The persistent class for the PRIVATE_CARE database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="PRIVATE_CARE")
@ToString(exclude = {"contacts"})
public class PrivateCare implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Column(name = "ADDRESS", nullable = false, length = 50)
	private String address;

	@Column(name = "FAX", nullable = false, length = 50)
	private String fax;

	@Column(name = "NAME", nullable = false, length = 50)
	private String name;

	@Column(name = "PHONE_NUMBER", nullable = false, length = 50)
	private String phoneNumber;
	
	@OneToMany(mappedBy = "privateCare")
	private List<Contact> contacts;
}
