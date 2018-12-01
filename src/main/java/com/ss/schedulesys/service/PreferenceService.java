package com.ss.schedulesys.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ss.schedulesys.domain.Preference;
import com.ss.schedulesys.repository.PreferenceRepository;
import com.ss.schedulesys.service.errors.ScheduleSysException;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@Transactional
public class PreferenceService {

	private PreferenceRepository preferenceRepository;
	
	public PreferenceService(PreferenceRepository preferenceRepository) {
		this.preferenceRepository = preferenceRepository;
	}
	
	/**
	 * @return
	 */
	@Transactional(readOnly = true)
	public Optional<Preference> getOne() {
		log.debug("Getting single record of preferences");
		return preferenceRepository.findAll()
				.stream()
				.findFirst();
				
	}
	
	/**
	 * @param preference
	 * @return
	 */
	public Preference save(Preference preference) {
		log.debug("Saving preference : {}", preference);
		Preference oldPreference = preferenceRepository.getOne(preference.getId());
		if(oldPreference == null)
			throw new ScheduleSysException(String.format("No preference found with id : %s", preference.getId()));
		Preference result = preferenceRepository.save(preference);
		return result;
	}
}
