package com.suyad.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import org.hibernate.mapping.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suyad.bindings.EligInfo;
import com.suyad.entity.AppEntity;
import com.suyad.entity.CoEntity;
import com.suyad.entity.EducationEntity;
import com.suyad.entity.EligDtlsEntity;
import com.suyad.entity.IncomeEntity;
import com.suyad.entity.KidEntity;
import com.suyad.entity.PlanEntity;
import com.suyad.entity.PlanSelEntity;
import com.suyad.repo.AppRepo;
import com.suyad.repo.CoRepo;
import com.suyad.repo.EducationRepo;
import com.suyad.repo.EligDtlsRepo;
import com.suyad.repo.IncomeRepo;
import com.suyad.repo.KidRepo;
import com.suyad.repo.PlanRepo;
import com.suyad.repo.PlanSelRepo;

@Service
public class EdServiceImpl implements EdService
{
	
	@Autowired
	private PlanSelRepo planSelRepo;

	@Autowired
	private PlanRepo planRepo;

	@Autowired
	private IncomeRepo incomeRepo;

	@Autowired
	private EducationRepo eduRepo;

	@Autowired
	private KidRepo kidRepo;

	@Autowired
	private EligDtlsRepo eligRepo;
	
	@Autowired
	private AppRepo appRepo;
	
	@Autowired
	private CoRepo coRepo;

	@Override
	public EligInfo determineEligibility(Integer caseNum) 
	{
		EligInfo response = new EligInfo();

		// get citizen plan name using caseNum

		PlanSelEntity planSel = planSelRepo.findByCaseNum(caseNum);

		Integer planId = planSel.getPlanId();

		PlanEntity planEntity = planRepo.findById(planId).get();

		String planName = planEntity.getPlanName();
		
		response.setPlanName(planName);
		
		// get citizen info using caseNum

		IncomeEntity incomeEntity = incomeRepo.findByCaseNum(caseNum);
		EducationEntity educationEntity = eduRepo.findByCaseNum(caseNum);
		java.util.List<KidEntity> kidsEntities = kidRepo.findByCaseNum(caseNum);
		AppEntity appEntity = appRepo.findById(caseNum).get();

		response.setPlanName(planName);
		response.setCaseNum(caseNum);

		if ("SNAP".equals(planName)) 
		{
			Double salaryIncome = incomeEntity.getSalaryIncome();
			if (salaryIncome > 300) {
				response.setPlanStatus("Denied");
				response.setDenialRsn("High Salary Income");
			} 
			else
			{
				response.setPlanStatus("Approved");
				response.setBenefitAmt(350.00);
				response.setPlanStartDate(LocalDate.now());
				response.setPlanEndDate(LocalDate.now().plusMonths(6));
			}

		} 
		else if ("CCAP".equals(planName))
		{
			Double income = incomeEntity.getSalaryIncome();
			java.util.List<KidEntity> kids = kidRepo.findByCaseNum(caseNum);
			boolean isValid = true;
			for(KidEntity kid:kids)
			{
				LocalDate kidDob = kid.getKidDob();
				int years = Period.between(kidDob, LocalDate.now()).getYears();
				if(years>16)
				{
					isValid = false;
					break;
				}
			}
			if(isValid && income<=300 && !kids.isEmpty())
			{
				response.setPlanStatus("Approved");
			}
			else
			{
				response.setPlanStatus("Denied");
			}
			if(!isValid)
			{
				response.setDenialRsn("Kid age Above 16");
			}
			if(income>300)
			{
				response.setDenialRsn("Hign income");
			}
			if(kids.isEmpty())
			{
				response.setDenialRsn("No kids available");
			}
			
		}
		else if ("Medicaid".equals(planName)) 
		{
			Double income = incomeEntity.getSalaryIncome();
			Double propertyIncome = incomeEntity.getPropertyIncome();
			Double rentIncome = incomeEntity.getRentIncome();
			if(income<=300 && ((propertyIncome+rentIncome)<0))
			{
				response.setPlanStatus("Approved");
			}
			else
			{
				response.setPlanStatus("Denied");
				response.setDenialRsn("High Income");
			}
		}
		else if ("Medicare".equals(planName)) 
		{
			
			LocalDate dob = appEntity.getDob();
			int years = Period.between(dob, LocalDate.now()).getYears();
			if(years>=65)
			{
				response.setPlanStatus("Approved");
			}
			else
			{
				response.setPlanStatus("Denied");
				response.setDenialRsn("Age Condition not Matched");
			}
			
		}
		else if ("RIW".equals(planName)) 
		{
			EducationEntity edu = eduRepo.findByCaseNum(caseNum);
			Integer graduationYear = edu.getGraduationYear();
			if(graduationYear!=null && incomeEntity!=null)
			{
				response.setPlanStatus("Approved");
			}
			else
			{
				response.setPlanStatus("Denied");
			}
			if(graduationYear==null)
			{
				response.setDenialRsn("Under Graduate");
			}
			if(incomeEntity!=null)
			{
				response.setDenialRsn("Already Employed");
			}
		}
		if(response.getPlanStatus().equals("Approved"))
		{
			response.setPlanStartDate(LocalDate.now());
			response.setPlanEndDate(LocalDate.now().plusMonths(6));
			response.setBenefitAmt(2000.00);
		}
		

		generateCorrespondence(appEntity);

		return response;
		
	}
	
	private void generateCorrespondence(AppEntity app)
	{
		CoEntity entity = new CoEntity();
		entity.setNoticeStatus("Pending");
		entity.setApp(app);
		coRepo.save(entity);
	}

	@Override
	public boolean generateCo(Integer caseNum) 
	{
		
		return false;
	}

}
