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
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"test", "testSubCategory", "employee"})
public class GetNurseTestViewModel {

	private TestViewModel test;
	
	private TestSubCategoryViewModel testSubCategory;
	
	private NurseViewModel employee;
	
	@NotBlank
	private String status;
	
	@JsonFormat(timezone = "America/New_York")
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date completedDate;
	
	@JsonFormat(timezone = "America/New_York")
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date expirationDate;
}
