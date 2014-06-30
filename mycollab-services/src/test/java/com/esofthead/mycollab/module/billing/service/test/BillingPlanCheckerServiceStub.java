package com.esofthead.mycollab.module.billing.service.test;

import org.springframework.stereotype.Service;

import com.esofthead.mycollab.module.billing.UsageExceedBillingPlanException;
import com.esofthead.mycollab.module.billing.service.BillingPlanCheckerService;


public class BillingPlanCheckerServiceStub implements BillingPlanCheckerService {

	@Override
	public void validateAccountCanCreateMoreProject(Integer sAccountId)
			throws UsageExceedBillingPlanException {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateAccountCanCreateNewUser(Integer sAccountId)
			throws UsageExceedBillingPlanException {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateAccountCanUploadMoreFiles(Integer sAccountId,
			long extraBytes) throws UsageExceedBillingPlanException {
		// TODO Auto-generated method stub

	}

}
