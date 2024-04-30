package com.suyad.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ELIG_DTLS")
@Data
public class EligDtlsEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eligId;
	
	private String planStatus;

	private Double benefitAmt;

	private LocalDate eligStartDate;

	private LocalDate eligEndDate;

	private String denialRsn;
	
	@OneToOne
	@JoinColumn(name = "case_num")
	private AppEntity app;
	
	@OneToOne
	@JoinColumn(name = "plan_id")
	private PlanEntity plan;
}
