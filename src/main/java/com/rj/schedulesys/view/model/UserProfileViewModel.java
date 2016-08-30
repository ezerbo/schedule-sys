package com.rj.schedulesys.view.model;

import org.hibernate.validator.constraints.NotBlank;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileViewModel {

	@NotBlank
	private String password;
	
	@NotBlank
	private String activationToken;
}
