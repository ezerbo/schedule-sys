package com.rj.schedulesys.view.model;

import java.util.Date;

import org.hibernate.validator.constraints.NotBlank;

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
public class NurseTestViewModel {
	
	private Long testId;
	
	private Long testSubCategoryId;
	
	private Long nurseId;
	
	@NotBlank
	private String status;
	
	@JsonFormat(timezone = "America/New_York")
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date completedDate;
	
	@JsonFormat(timezone = "America/New_York")
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date expirationDate;
}
