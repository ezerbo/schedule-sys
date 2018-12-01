package com.ss.schedulesys.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service_request", catalog = "schedulesys_db")
public class ServiceRequest implements java.io.Serializable {

	private static final long serialVersionUID = -1864422494742504917L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@NotNull
	@Column(name = "name", nullable = false)
	private String name;
	
	@NotNull
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;
	
	@Email
	@Column(name = "email_address", nullable = false)
	private String emailAddress;
	
	@Column(name = "ccc")
	private boolean ccc;
	
	@Column(name = "lis")
	private boolean lis;
	
	@Column(name = "bad")
	private boolean bad;
	
	@Column(name = "aw")
	private boolean aw;
	
	@Column(name = "lh")
	private boolean lh;
	
	@Column(name = "mp")
	private boolean mp;
	
	@Column(name = "laundry")
	private boolean laundry;
	
	@Column(name = "se")
	private boolean se;
	
	@Column(name = "mr")
	private boolean mr;
	
	@Column(name = "lt")
	private boolean lt;
	
	@Column(name = "ccg")
	private boolean ccg;
	
	@Column(name = "ae")
	private boolean ae;
	
	@Column(name = "cpp")
	private boolean cpp;
	
	@Column(name = "prlc")
	private boolean prlc;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date", nullable = false)
	private Date startDate;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_date", nullable = false)
	private Date endDate;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time", nullable = false)
	private Date startTime;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time", nullable = false)
	private Date endTime;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "request_date", nullable = false)
	private Date requestDate;
	
	@Column(name = "comment")
	private String comment;
}