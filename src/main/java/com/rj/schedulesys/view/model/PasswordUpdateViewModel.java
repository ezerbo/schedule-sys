package com.rj.schedulesys.view.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PasswordUpdateViewModel {
	
	@NotBlank
	private String oldPassword;

	@NotBlank
	@Size(min = 8, max = 50)
	@JsonProperty("password")
	private String newPassword;
}
