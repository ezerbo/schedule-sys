package com.rj.sys.view.model;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleUpdateViewModel {
	
	private Long id;
	
	private String employeeName;
	
	@Size(max = 100)
	private String scheduleComment;
	
	@Future
	private Date scheduleDate;
	
	private String facility;
	
	private String shift;
	
	private String scheduleStatus;
	
	private String schedulePostStatus;
	
	private Boolean timesheetReceived;
	
	private Double hours;
	
	private Long overtime;
}
