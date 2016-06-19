package com.rj.schedulesys.view.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rj.schedulesys.annotation.PhoneNumber;
import com.rj.schedulesys.util.JsonDateSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect
public class EmployeeViewModel {
	private Long id;
	private Boolean cpr;
	private Boolean ebc;
	private String firstName;
	private String lastName;
	private String emailAddress;
	private String position;
	private String positionType;
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date rehireDate;
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date dateOfHire;
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date lastDateOfHire;
	@PhoneNumber
	private String otherPhoneNumber;
	@PhoneNumber
	private String primaryPhoneNumber;
	@PhoneNumber
	private String secondaryPhoneNumber;
}