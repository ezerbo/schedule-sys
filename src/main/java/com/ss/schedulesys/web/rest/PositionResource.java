package com.ss.schedulesys.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ss.schedulesys.domain.Position;
import com.ss.schedulesys.service.PositionService;
import com.ss.schedulesys.web.rest.util.HeaderUtil;

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
     * POST  /positions : Create a new position.
     *
     * @param position the position to create
     * @return the ResponseEntity with status 201 (Created) and with body the new position, or with status 400 (Bad Request) if the position has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/positions")
    public ResponseEntity<Position> createPosition(@Valid @RequestBody Position position) throws URISyntaxException {
        log.info("REST request to save Position : {}", position);
        if (position.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("position", "idexists", "A new position cannot already have an ID")).body(null);
        }
        Position result = positionService.save(position);
        return ResponseEntity.created(new URI("/api/positions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("position", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /positions : Updates an existing position.
     *
     * @param position the position to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated position,
     * or with status 400 (Bad Request) if the position is not valid,
     * or with status 500 (Internal Server Error) if the position couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/positions")
    public ResponseEntity<Position> updatePosition(@Valid @RequestBody Position position) throws URISyntaxException {
        log.debug("REST request to update Position : {}", position);
        if (position.getId() == null) {
            return createPosition(position);
        }
        Position result = positionService.save(position);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("position", position.getId().toString()))
            .body(result);
    }
    
    /**
     * DELETE  /positions/:id : delete the "id" position.
     *
     * @param id the id of the position to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/positions/{id}")
    public ResponseEntity<Void> deleteLicense(@PathVariable Long id) {
        log.debug("REST request to delete Position : {}", id);
        positionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("position", id.toString())).build();
    }
	
	/**
    * GET  /positions : get all the positions.
    *
    * @param pageable the pagination information
    * @return the ResponseEntity with status 200 (OK) and the list of positions in body
    */
    @GetMapping("/positions")
    public ResponseEntity<List<Position>> getAllPositions()
        throws URISyntaxException {
        log.debug("REST request to get a page of Positions");
        List<Position> positions = positionService.getAll();
        return new ResponseEntity<>(positions, HttpStatus.OK);
    }

}
