package com.ss.schedulesys.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ss.schedulesys.domain.Preference;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

}
