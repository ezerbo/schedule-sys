package com.rj.schedulesys.view.model;

import javax.validation.constraints.Pattern;

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
	@Pattern(
			regexp = "\\(\\d{3}\\)-\\d{3}-\\d{4}"
			, message = "${validatedValue} is not a valid phone number"
			)
	private String number;
	
	@NotBlank
	private String numberLabel;
	
	@NotBlank
	private String numberType;
}
