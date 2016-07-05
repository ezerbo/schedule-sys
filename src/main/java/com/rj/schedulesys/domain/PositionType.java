package com.rj.schedulesys.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the POSITION_TYPE database table.
 * 
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "POSITION_TYPE")
public class PositionType {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "NAME", unique = true, nullable = false)
	private String name;
	
	@OneToMany(mappedBy = "positionType")
	private List<Position> positions;
	
	public Position addPosition(Position position) {
		getPositions().add(position);
		position.setPositionType(this);
		return position;
	}

	public Position removePosition(Position position) {
		getPositions().remove(position);
		position.setPositionType(null);
		return position;
	}
	
}
