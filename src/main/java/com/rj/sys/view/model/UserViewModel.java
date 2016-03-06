package com.rj.sys.view.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserViewModel {
	
	private Long id;
	private String cpr;
	private Date dateOfHire;
	private boolean ebc;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private Date lastDateOfHire;
	private String otherPhoneNumber;
	private String password;
	private String primaryPhoneNumber;
	private Date rehireDate;
	private String secondaryPhoneNumber;
	private String username;
	private String position;
	private String userType;

}
