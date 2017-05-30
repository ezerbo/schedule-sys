package com.ss.schedulesys.repository.specs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.ss.schedulesys.domain.EmployeeType;
import com.ss.schedulesys.domain.EmployeeType_;

public class EmployeeSpecifications {
	
	public static Specification<EmployeeType> isNurse() {
		return new Specification<EmployeeType> (){

			@Override
			public Predicate toPredicate(Root<EmployeeType> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.equal(root.get(EmployeeType_.name), "Nurse");
			}
			
		};
	}
}
