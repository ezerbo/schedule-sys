package com.ss.schedulesys.web.vm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeFilterModel {
	private String firstName;
	private String lastName;
	private String positionName;
	private String employeeTypeName;
}
