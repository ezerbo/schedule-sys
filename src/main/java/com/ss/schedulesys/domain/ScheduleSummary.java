package com.ss.schedulesys.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSummary {
	private Long careCompanyId;
	private String careCompanyName;
	private String careCompanyType;
	private Long shiftsScheduled;
	private Date scheduleDate;
}
