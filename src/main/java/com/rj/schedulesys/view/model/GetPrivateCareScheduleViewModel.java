package com.rj.schedulesys.view.model;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.rj.schedulesys.util.JsonDateSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetPrivateCareScheduleViewModel {
	
	private Long id;

	private EmployeeViewModel employee;

	private ScheduleSysUserViewModel filledBy;

	private ScheduleSysUserViewModel lastModifiedBy;

	@Size(max = 254)
	private String scheduleComment;

	@Future
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonFormat(timezone = "America/New_York")
	private Date scheduleDate;

	private PrivateCareViewModel privateCare;

	private PrivateCareShiftViewModel shift;

	private ScheduleStatusViewModel scheduleStatus;

	private Boolean timesheetReceived;
}
