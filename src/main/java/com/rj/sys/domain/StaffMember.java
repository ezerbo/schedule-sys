package com.rj.sys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the STAFF_MEMBER database table.
 * 
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="STAFF_MEMBER")
public class StaffMember implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID", unique=true, nullable=false)
	private Long id;

	@Column(name="FIRST_NAME", nullable=false, length=50)
	private String firstName;

	@Column(name="LAST_NAME", nullable=false, length=50)
	private String lastName;

	@Column(name="TITLE", nullable=false, length=50)
	private String title;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FACILITY_ID", nullable=false)
	private Facility facility;

}