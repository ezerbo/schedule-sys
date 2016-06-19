package com.rj.schedulesys.view.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSysUserViewModel {
	private Long id;
	private String username;
	private String password;
	private String userRole;
}
