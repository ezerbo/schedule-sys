package com.rj.schedulesys.view.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchedulePostStatusViewModel {
	private Long id;
	private String status;
}
