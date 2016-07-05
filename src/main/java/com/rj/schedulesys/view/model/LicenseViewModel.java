package com.rj.schedulesys.view.model;

import java.util.Date;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

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
public class LicenseViewModel {
	
	private Long id;
	
	@NotNull
	private Long nurseId;
	
	@Future
	@JsonFormat(timezone = "America/New_York")
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date expirationDate;
	
	@NotBlank
	private String number;
	
}
