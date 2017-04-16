package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
