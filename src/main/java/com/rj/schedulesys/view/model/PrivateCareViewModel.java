package com.rj.schedulesys.view.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateCareViewModel {

	private Long id;
	
	@NotBlank
	@Size(min = 3, max = 50)
	private String name;
	
	@NotBlank
	@Size(min = 3 , max = 50)
	private String address;
	
	@NotBlank
	private String fax;
	
	@NotBlank
	private String phoneNumber;
}
