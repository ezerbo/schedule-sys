package com.rj.sys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the PHONE_NUMBER database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="PHONE_NUMBER")
public class PhoneNumber implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID", unique=true, nullable=false)
	private Long id;

	@Column(name="NUMBER", nullable=false, length=50)
	private String number;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="EMPLOYEE_ID", nullable=false)
	private Employee employee;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PHONE_NUMBER_LABEL_ID", nullable=false)
	private PhoneNumberLabel phoneNumberLabel;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PHONE_NUMBER_TYPE_ID", nullable=false)
	private PhoneNumberType phoneNumberType;

}