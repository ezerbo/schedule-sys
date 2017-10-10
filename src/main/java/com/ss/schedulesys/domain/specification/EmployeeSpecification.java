package com.ss.schedulesys.domain.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.ss.schedulesys.domain.Employee;
import com.ss.schedulesys.domain.EmployeeType;
import com.ss.schedulesys.domain.Position;
import com.ss.schedulesys.domain.SearchCriteria;

public class EmployeeSpecification implements Specification<Employee> {

	private SearchCriteria criteria;
	
	public EmployeeSpecification(SearchCriteria criteria) {
		this.criteria = criteria;
	}
	
	@Override
	public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class)
                return cb.like(cb.lower(root.<String>get(criteria.getKey())), "%" + criteria.getValue().toLowerCase() + "%");
            if(root.get(criteria.getKey()).getJavaType() == EmployeeType.class || root.get(criteria.getKey()).getJavaType() == Position.class)
                return cb.like(cb.lower(root.get(criteria.getKey()).get("name")), "%" + criteria.getValue().toLowerCase() + "%");
            
		}
		return null;
	}

}
