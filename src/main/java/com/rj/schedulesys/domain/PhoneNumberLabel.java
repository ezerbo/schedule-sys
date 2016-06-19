package com.rj.schedulesys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * The persistent class for the PHONE_NUMBER_LABEL database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="PHONE_NUMBER_LABEL")
public class PhoneNumberLabel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID", unique=true, nullable=false)
	private Long id;

	@Column(name="NAME", nullable=false, length=50)
	private String name;

	@OneToMany(mappedBy="phoneNumberLabel")
	private List<PhoneNumber> phoneNumbers;

	public PhoneNumber addPhoneNumber(PhoneNumber phoneNumber) {
		getPhoneNumbers().add(phoneNumber);
		phoneNumber.setPhoneNumberLabel(this);

		return phoneNumber;
	}

	public PhoneNumber removePhoneNumber(PhoneNumber phoneNumber) {
		getPhoneNumbers().remove(phoneNumber);
		phoneNumber.setPhoneNumberLabel(null);

		return phoneNumber;
	}

}