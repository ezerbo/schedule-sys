package com.rj.schedulesys.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the POSITION database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="POSITION")
public class Position implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID", unique=true, nullable=false)
	private Long id;
	
	@Column(name="NAME", nullable=false, length=50)
	private String name;
	
	@OneToMany(mappedBy="position")
	private List<Employee> employees;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="POSITION_TYPE_ID", nullable=false)
	private PositionType positionType;
	
	public Employee addEmployee(Employee employee) {
		getEmployees().add(employee);
		employee.setPosition(this);
		return employee;
	}

	public Employee removeEmployee(Employee employee) {
		getEmployees().remove(employee);
		employee.setPosition(null);
		return employee;
	}

}