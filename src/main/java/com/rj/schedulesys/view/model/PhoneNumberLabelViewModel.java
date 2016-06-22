package com.rj.schedulesys.view.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberLabelViewModel {
	
	private Long id;
	
	@NotBlank
	@Size(min = 3, max = 50)
	private String name;
}
