package com.ss.schedulesys.web.view;

import com.ss.schedulesys.domain.Employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberVM {

	private Long id;
	
	private Employee employeeId;
	
	private String number;
	
	private String label;
	
	private String type;
}
