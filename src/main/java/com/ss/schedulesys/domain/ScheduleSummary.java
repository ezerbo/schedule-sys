package com.ss.schedulesys.domain;

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
}
