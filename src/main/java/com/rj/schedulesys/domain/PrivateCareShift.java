package com.rj.schedulesys.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the PRIVATE_CARE_SHIFT database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="PRIVATE_CARE_SHIFT")
//@ToString(exclude = "schedules")
public class PrivateCareShift implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	
	@Column(name = "END_TIME", nullable = false)
	private String endTime;

	
	@Column(name = "START_TIME", nullable = false)
	private String startTime;

	//@OneToMany(mappedBy = "shift")
	//private List<FacilitySchedule> schedules;


}
