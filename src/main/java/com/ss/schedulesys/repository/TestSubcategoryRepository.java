package com.ss.schedulesys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ss.schedulesys.domain.TestSubcategory;

public interface TestSubcategoryRepository extends JpaRepository<TestSubcategory, Long> {

	public TestSubcategory findByName(String name);

	@Query("from TestSubcategory ts where ts.test.id = :testId")
	public List<TestSubcategory> findAllByTest(@Param("testId") Long testId);
}
