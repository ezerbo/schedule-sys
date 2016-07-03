package com.rj.schedulesys.dao;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.Employee;

@Repository
public class EmployeeDao extends GenericDao<Employee> {

	public EmployeeDao() {
		setClazz(Employee.class);
	}
}