package com.rj.schedulesys.view.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTestUpdateViewModel {
	private Date completedDate;
	private Date expirationDate;
}
