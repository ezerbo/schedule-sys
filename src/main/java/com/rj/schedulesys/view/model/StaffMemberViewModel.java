package com.rj.schedulesys.view.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffMemberViewModel {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String title;
	private String facility;
	
}
