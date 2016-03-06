package com.rj.sys.dto;

import java.util.Date;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleTO {
	
	private int id;
	private String scheduleComment;
	private Date scheduleDate;
	private boolean timesheetReceived;
	private String facilityName;
	private String schedulePostStatus;
	private String shift;
	private String scheduleStatus;
	private String assignee;
	
}
