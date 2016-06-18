package com.rj.sys.util;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.rj.sys.view.model.ScheduleSysUserViewModel;

public class TestUtil {
	
	private TestUtil(){
		//Utility class, not to be instantiated  
	}
	
	public static byte [] convertObjectToJsonBytes(Object object) throws IOException{
		if(object == null){
			throw new IllegalArgumentException("Please provided object to be converted to Json");
		}
		return new ObjectMapper().writeValueAsBytes(object);
	}
	
	public static ScheduleSysUserViewModel aNewScheduleSysUserViewModel(Long id, String username, String password, String userRole){
		ScheduleSysUserViewModel viewModel = ScheduleSysUserViewModel.builder()
				.id(id)
				.username(username)
				.password(password)
				.userRole(userRole)
				.build();
		return viewModel;
	}

}
