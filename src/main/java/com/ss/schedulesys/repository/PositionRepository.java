package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.Position;

public interface PositionRepository extends JpaRepository<Position, Long> {

	public Position findByName(String name);
}