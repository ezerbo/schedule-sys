package com.ss.schedulesys.web.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LicenseTypeVM {

	private Long id;
	
	private String name;
}
