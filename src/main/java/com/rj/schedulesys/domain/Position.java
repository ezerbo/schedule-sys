package com.rj.schedulesys.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


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