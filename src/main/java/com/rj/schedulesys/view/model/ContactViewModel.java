package com.rj.schedulesys.view.model;

import javax.validation.constraints.NotNull;
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
public class ContactViewModel {

	private Long id;
	
	@NotBlank
	@Size(min = 3, max = 50)
	private String firstName;
	
	@NotBlank
	@Size(min = 3, max = 50)
	private String lastName;
	
	@NotBlank
	@Size(min = 3, max = 50)
	private String title;
	
	@NotBlank
	@Size(max = 10)
	private String phoneNumber;
	
	@NotBlank
	@Size(max = 10)
	private String fax;
	
	@NotNull
	private Long privateCareId;
}
