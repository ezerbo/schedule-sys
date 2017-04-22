package com.ss.schedulesys.repository;

import java.util.List;
import java.util.Optional;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.ScheduleSysUser;

public interface ScheduleSysUserRepository extends JpaRepository<ScheduleSysUser, Long> {

	public Optional<ScheduleSysUser> findOneByUsername(String username);

	public Optional<ScheduleSysUser> findOneByActivationKey(String key);

	public Optional<ScheduleSysUser> findOneByResetKey(String key);

	public Optional<ScheduleSysUser> findOneByEmailAddress(String mail);

	public List<ScheduleSysUser> findAllByActivatedIsFalseAndCreateDateBefore(DateTime minusDays);
}