package com.ss.schedulesys.web.vm;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.ss.schedulesys.config.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * View Model object for storing the user's key and password.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeyAndPasswordVM {
    
	@NotBlank
	private String key;
    
	@NotBlank
	@Size(min = Constants.PASSWORD_MIN_LENGTH, max = Constants.PASSWORD_MAX_LENGTH)
    private String password;
    
    public String toString(){
    	return "{key: " + key + " password: ********}";
    }
}