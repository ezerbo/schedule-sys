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
public class ScheduleViewModel {
	private Long id;
	private Date createDate;
	private String scheduleComment;
	private Date scheduleDate;
	private Boolean timesheetReceived;
	private Long assigneeId;
	private String facility;
	private String schedulePostStatus;
	private String shift;
	private String scheduleStatus;
}
