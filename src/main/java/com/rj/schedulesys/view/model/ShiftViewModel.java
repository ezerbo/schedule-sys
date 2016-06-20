package com.rj.schedulesys.view.model;

import java.time.LocalTime;

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
public class ShiftViewModel {
	
	private Long id;
	
	private LocalTime startTime;
	
	@NotBlank
	@Size(min = 3, max = 30)
	private String name;
	
	private LocalTime endTime;
	
}
