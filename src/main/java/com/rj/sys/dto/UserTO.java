package com.rj.sys.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTO {
	
	private String positionType;
	private String username;
	private String password;
	private String emailAddress;
	private String firstName;
	private String lastName;
	private String type;
	private String primaryPhoneNumber;
	private String secondaryPhoneNumber;
	private String otherPhoneNumber;
	private Date dateOfHire;
	private Date lastDateOfHire;
	private Date rehireDate;
	private Boolean ebc;
	private String cpr;
	
}
