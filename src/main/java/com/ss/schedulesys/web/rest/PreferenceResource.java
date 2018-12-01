package com.ss.schedulesys.web.rest;

import java.net.URISyntaxException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.schedulesys.domain.Preference;
import com.ss.schedulesys.service.PreferenceService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class PreferenceResource {

	private PreferenceService preferenceService;
	
	public PreferenceResource(PreferenceService preferenceService) {
		this.preferenceService = preferenceService;
	}
	
	/**
	 * @return
	 */
	@GetMapping("/preferences")
	public ResponseEntity<Preference> get() {
		log.debug("Getting preferences");
		return preferenceService.getOne()
				.map(preference -> ResponseEntity.ok(preference))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	/**
	 * @param preference
	 * @return
	 * @throws URISyntaxException 
	 */
	@PutMapping("/preferences")
	public ResponseEntity<Preference> save(@RequestBody Preference preference) throws URISyntaxException {
		log.info("Saving preference : {}", preference);
		if(preference.getId() == null)
			return new ResponseEntity<Preference>(HttpStatus.BAD_REQUEST);
		Preference result = preferenceService.save(preference);
		return ResponseEntity.ok()
	            .body(result);
	}
}
