package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.Test;

public interface TestRepository extends JpaRepository<Test, Long> {

	public Test findByName(String name);
}
