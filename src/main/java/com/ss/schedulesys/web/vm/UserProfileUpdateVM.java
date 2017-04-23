package com.ss.schedulesys.web.vm;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateVM {
	
	@NotNull
	private Long id;
	
	private String role;
	
	private String emailAddress;
	
	private String firstName;
	
	private String lastName;
	
	private String username;
	
	private boolean activated;
	
}
