package com.ss.schedulesys.web.vm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareCompanyFilterModel {
	private String name;
	private String typeName;
	private String insuranceName;
	
}
