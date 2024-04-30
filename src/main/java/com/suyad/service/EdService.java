package com.suyad.service;

import com.suyad.bindings.EligInfo;

public interface EdService 
{
	public EligInfo determineEligibility(Integer caseNum);

	public boolean generateCo(Integer caseNum);
}
