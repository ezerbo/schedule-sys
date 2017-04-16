package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.ScheduleSysUser;

public interface ScheduleSysUserRepository extends JpaRepository<ScheduleSysUser, Long> {

}
