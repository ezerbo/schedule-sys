package com.rj.schedulesys.view.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSysUserViewModel {
	
	private Long id;
	
	@NotBlank
	@Size(min = 6, max = 50)
	private String username;
	
	@Email
	@NotBlank
	@Size(min = 3, max = 100)
	private String emailAddress;
	
	@NotBlank
	@Size(max = 50)
	private String userRole;
	
	private Boolean isActivated;
}
