package com.rj.schedulesys.view.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateScheduleViewModel {

	private Long id;
	
	private Long employeeId;

	@NotNull
	private Long facilityId;
	
	@NotNull
	private Long shiftId;
	
	private String comment;
	
	@NotNull
	private Long scheduleStatusId;
	
	private Long schedulePostStatusId;
	
	@NotNull
	private Date scheduleDate;
}
