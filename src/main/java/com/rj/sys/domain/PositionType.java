package com.rj.sys.domain;

import java.io.Serializable;
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
 * The persistent class for the facility database table.
 * 
 */

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "POSITION_TYPE")
public class PositionType implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="TYPE")
	private String type;
	
	@OneToMany(mappedBy="positionType")
	private List<Position> positions;
	
	public Position addPosition(Position position) {
		getPositions().add(position);
		position.setPositionType(this);

		return position;
	}

	public Position removeUser(Position position) {
		getPositions().remove(position);
		position.setPositionType(null);

		return position;
	}
}
