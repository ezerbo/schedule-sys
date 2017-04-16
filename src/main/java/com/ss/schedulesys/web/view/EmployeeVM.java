package com.ss.schedulesys.web.view;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeVM {

	private Long id;
	
	private Long employeeTypeId;
	
	private Long positionId;
	
	private String firsname;
	
	private String lastname;
	
	private Date dateOfHire;
	
	private Date lastDayOfWork;
	
	private String comment;
	
	private boolean ebc;
	
	private boolean insvc;
	
	
}
