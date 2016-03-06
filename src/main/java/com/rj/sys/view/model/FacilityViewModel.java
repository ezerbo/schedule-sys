package com.rj.sys.view.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacilityViewModel {
	
	private int id;
	private String address;
	private String fax;
	private String name;
	private String phoneNumber;
	
}
