package com.ss.schedulesys.web.vm;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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

	private Long id;
	
	@NotBlank
	private String role;
	
	@Email
	@NotBlank
	private String emailAddress;
	
	@NotBlank
	@Size(min = 2, max = 50)
	private String firstName;
	
	@NotBlank
	@Size(min = 2, max = 50)
	private String lastName;
	
	@NotBlank
	@Size(min = 6, max = 20)
	@Pattern(regexp = Constants.LOGIN_REGEX)
	private String username;
	
	private boolean activated;
	
}
