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
public class ShiftViewModel {
	private Long id;
	private Date endTime;
	private String shiftName;
	private Date startTime;
}
