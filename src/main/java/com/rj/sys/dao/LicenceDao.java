package com.rj.sys.dao;

import org.springframework.stereotype.Repository;

import com.rj.sys.domain.License;

@Repository
public class LicenceDao extends GenericDao<License> {
	
	public LicenceDao() {
		setClazz(License.class);
	}
}
