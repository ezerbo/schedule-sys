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
public class CreatePrivateCareScheduleViewModel {

	private Long id;
	
	private Long employeeId;

	@NotNull
	private Long privateCareId;
	
	@NotNull
	private Long startShiftId;
	
	@NotNull
	private Long endShiftId;
	
	private String comment;
	
	@NotNull
	private Long scheduleStatusId;
	
	@NotNull
	private Date scheduleDate;
	
	private boolean paid;
	
	private boolean billed;
}
