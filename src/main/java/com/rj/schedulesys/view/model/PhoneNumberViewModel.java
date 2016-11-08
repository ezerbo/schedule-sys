package com.rj.schedulesys.view.model;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberViewModel {
	
	private Long id;
	
	@NotBlank
	private String number;
	
	@NotBlank
	private String numberLabel;
	
	@NotBlank
	private String numberType;
}
