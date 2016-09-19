package com.rj.schedulesys.view.model;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

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
public class GetLicenseViewModel {

	private Long id;
	
	private LicenseTypeViewModel licenseType;
	
	private NurseViewModel nurse;
	
	@Future
	@JsonFormat(timezone = "America/New_York")
	@JsonSerialize(using = JsonDateSerializer.class)
	@NotNull(message = "was not provided")
	private Date expirationDate;
	
	private String number;
}
