package com.ss.schedulesys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.schedulesys.domain.Test;

public interface TestRepository extends JpaRepository<Test, Long> {

	public Test findByNameIgnoreCase(String name);
	
	@Query("from Test t where LOWER(t.name) LIKE LOWER(CONCAT('%', :query, '%'))")
	public List<Test> searchByName(@Param("query") String query);
}
