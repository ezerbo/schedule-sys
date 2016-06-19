package com.rj.schedulesys.view.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInViewModel {
	
	private String username;
	
	private String password;
	
	private boolean rememberMe;
	
}
