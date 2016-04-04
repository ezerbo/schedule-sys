package com.rj.sys.view.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTestViewModel {
	private Long userId;
	private String testTypeName;
	private Date completedDate;
	private Date expirationDate;
}
