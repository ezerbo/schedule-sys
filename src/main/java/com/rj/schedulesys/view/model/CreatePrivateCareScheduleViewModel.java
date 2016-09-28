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
	
	private Long careGiverId;

	@NotNull
	private Long privateCareId;
	
	@NotNull
	private Long shiftId;
	
	private String comment;
	
	@NotNull
	private Long scheduleStatusId;
	
	@NotNull
	private Date scheduleDate;
}
