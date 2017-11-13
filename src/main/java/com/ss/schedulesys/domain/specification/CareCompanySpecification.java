package com.ss.schedulesys.domain.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.ss.schedulesys.domain.CareCompany;
import com.ss.schedulesys.domain.CareCompanyType;
import com.ss.schedulesys.domain.SearchCriteria;

public class CareCompanySpecification implements Specification<CareCompany> {

	private SearchCriteria criteria;
	
	public CareCompanySpecification(SearchCriteria criteria) {
		this.criteria = criteria;
	}
	
	@Override
	public Predicate toPredicate(Root<CareCompany> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class)
                return cb.like(cb.lower(root.<String>get(criteria.getKey())), "%" + criteria.getValue().toLowerCase() + "%");
            if(root.get(criteria.getKey()).getJavaType() == CareCompanyType.class)
                return cb.like(cb.lower(root.get(criteria.getKey()).get("name")), "%" + criteria.getValue().toLowerCase() + "%");
            
		}
		return null;
	}

}
