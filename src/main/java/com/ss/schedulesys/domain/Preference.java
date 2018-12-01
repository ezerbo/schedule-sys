package com.ss.schedulesys.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ezerbo
 *
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "preference", catalog = "schedulesys_db")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Preference implements Serializable {

	private static final long serialVersionUID = 7343244036153413688L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Column(name = "schedule_arch_days", nullable = false)
	private Long scheduleArchDays;
}
