package com.suyad.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "CO_NOTICES")
@Data
public class CoEntity 
{
	@Id
	@GeneratedValue
	private Integer coId;

	private String noticeStatus;
	
	private String noticePrintDate;

	private String noticeS3Url;

	private Integer edgTraceId;

	@OneToOne
	@JoinColumn(name = "case_num")
	private AppEntity app;
}
