package com.rj.schedulesys.view.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

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
	
	@NotBlank
	@Size(min = 3, max = 50)
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	
	@NotBlank
	@Size(max = 50)
	private String userRole;
}
