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
public class ScheduleViewModel {
	
	private Long id;
	
	private String employeeName;
	
	private String job;
	
	private String filledBy;
	
	private String lastModifiedBy;
	
	@Size(max = 100)
	private String scheduleComment;
	
	@Future
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonFormat(timezone = "America/New_York")
	private Date scheduleDate;
	
	private String facility;
	
	private String shift;
	
	private String scheduleStatus;
	
	private String schedulePostStatus;
	
	private Boolean timesheetReceived;
	
	private Double hours;
	
	private Double overtime;
	
	private TimeViewModel time;
}
