package com.esofthead.mycollab.community.module.billing.service.ibatis;

import org.springframework.stereotype.Service;

import com.esofthead.mycollab.module.billing.UsageExceedBillingPlanException;
import com.esofthead.mycollab.module.billing.service.BillingPlanCheckerService;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
@Service
public class BillingPlanCheckerServiceImpl implements BillingPlanCheckerService {

	@Override
	public void validateAccountCanCreateMoreProject(Integer sAccountId)
			throws UsageExceedBillingPlanException {

	}

	@Override
	public void validateAccountCanCreateNewUser(Integer sAccountId)
			throws UsageExceedBillingPlanException {
	}

	@Override
	public void validateAccountCanUploadMoreFiles(Integer sAccountId,
			long extraBytes) throws UsageExceedBillingPlanException {

	}

}
