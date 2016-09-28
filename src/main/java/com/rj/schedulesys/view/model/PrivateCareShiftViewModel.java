package com.rj.schedulesys.view.model;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateCareShiftViewModel {

	private Long id;
	
	@NotBlank
	@Size(max = 10)
	private String startTime;
	
	@NotBlank
	@Size(min = 3, max = 30)
	private String endTime;
}
