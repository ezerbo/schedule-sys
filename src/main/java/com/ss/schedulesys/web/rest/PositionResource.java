package com.ss.schedulesys.web.rest;

import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.schedulesys.domain.Position;
import com.ss.schedulesys.service.PositionService;

import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for managing position.
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class PositionResource {

	private PositionService positionService;
	
	@Autowired
	public PositionResource(PositionService positionService) {
		this.positionService = positionService;
	}
	
	/**
    * GET  /positions : get all the positions.
    *
    * @param pageable the pagination information
    * @return the ResponseEntity with status 200 (OK) and the list of licenses in body
    */
    @GetMapping("/positions")
    public ResponseEntity<List<Position>> getAllPositions()
        throws URISyntaxException {
        log.debug("REST request to get a page of Positions");
        List<Position> positions = positionService.getAll();
        return new ResponseEntity<>(positions, HttpStatus.OK);
    }

}
