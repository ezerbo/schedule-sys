package com.ss.schedulesys.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"careCompanies"})
@EqualsAndHashCode(exclude = {"careCompanies"})
@Table(name = "insurance_company", catalog = "schedulesys_db", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class InsuranceCompany implements java.io.Serializable {

	private static final long serialVersionUID = 353160039355074585L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Column(name = "name", unique = true, nullable = false)
	private String name;
	
	@JsonIgnore
	@OneToMany(mappedBy = "insuranceCompany")
	private Set<CareCompany> careCompanies = new HashSet<CareCompany>(0);
}
