package com.rj.schedulesys.view.model;

import javax.validation.constraints.Max;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleViewModel {
	
	private Long id;
	
	@NotBlank
	@Max(value = 50)
	private String userRole;
}
