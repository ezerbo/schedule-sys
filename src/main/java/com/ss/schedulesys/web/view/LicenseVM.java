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
public class LicenseVM {

	private Long id;
	
	private Long employeeId;
	
	private Long licenseTypeId;
	
	private String number;
	
	private Date expiry;
}
