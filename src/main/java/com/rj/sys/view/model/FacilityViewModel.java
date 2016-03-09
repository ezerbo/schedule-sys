package com.rj.sys.view.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.validator.constraints.NotBlank;

import com.rj.sys.domain.annotation.PhoneNumber;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityViewModel {
	
	private int id;
	
	@NotBlank
	private String address;
	
	@NotBlank
	private String fax;
	
	@NotBlank
	private String name;
	
	@PhoneNumber
	private String phoneNumber;
	
}
