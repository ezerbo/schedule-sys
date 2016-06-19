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
public class StaffMemberViewModel {
	
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
	private String facilityName;
	
}
