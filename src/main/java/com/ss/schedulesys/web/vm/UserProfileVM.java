package com.ss.schedulesys.web.vm;

import javax.validation.constraints.Max;
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
	
	@Email
	@NotBlank
	private String emailAddress;
	
	@NotBlank
	@Max(100)
	private String firstName;
	
	@NotBlank
	@Max(100)
	private String lastName;
	
	@NotBlank
	@Pattern(regexp = Constants.LOGIN_REGEX)
	private String username;
	
}
