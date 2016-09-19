package com.rj.schedulesys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * The persistent class for the LICENSE database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="LICENSE")
public class License implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;

	@Temporal(TemporalType.DATE)
	@Column(name = "EXPIRATION_DATE", nullable = false)
	private Date expirationDate;

	@Column(name = "NUMBER", nullable = false, length = 254)
	private String number;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NURSE_ID")
	private Nurse nurse;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LICENSE_TYPE_ID", nullable = false)
	private LicenseType licenseType;

}