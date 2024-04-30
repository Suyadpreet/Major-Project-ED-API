package com.suyad.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.suyad.bindings.EligInfo;
import com.suyad.service.EdService;

@RestController
public class EdRestController 
{
	@Autowired
	private EdService eligService;

	@GetMapping("/elig/{caseNum}")
	public ResponseEntity<EligInfo> determineElig(@PathVariable Integer caseNum) {
		EligInfo response = eligService.determineEligibility(caseNum);
		return new ResponseEntity<EligInfo>(response, HttpStatus.OK);
	}

	@GetMapping("/co/{caseNum}")
	public ResponseEntity<String> generateCo(@PathVariable Integer caseNum) {
		boolean status = eligService.generateCo(caseNum);
		if (status) {
			return new ResponseEntity<>("Notice Generated", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Notice Not Generated", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
