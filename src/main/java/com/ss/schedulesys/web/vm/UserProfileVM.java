package com.ss.schedulesys.web.vm;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.ss.schedulesys.config.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileVM {

	@NotBlank
	private String role;
	
	@NotBlank
	@Email
	private String emailAddress;
	
	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
	@NotBlank
	@Pattern(regexp = Constants.LOGIN_REGEX)
	private String username;
	
	@NotBlank
	private String password;
}
