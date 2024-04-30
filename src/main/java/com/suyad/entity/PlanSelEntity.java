package com.suyad.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class PlanSelEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer planSelId;

	private Integer planId;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private UserEntity user;

	@OneToOne
	@JoinColumn(name = "case_num")
	private AppEntity app;
}
