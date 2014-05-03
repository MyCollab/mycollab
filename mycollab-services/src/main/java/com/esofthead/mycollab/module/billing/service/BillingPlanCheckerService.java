package com.esofthead.mycollab.module.billing.service;

import com.esofthead.mycollab.core.persistence.service.IService;
import com.esofthead.mycollab.module.billing.UsageExceedBillingPlanException;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public interface BillingPlanCheckerService extends IService {
	void validateAccountCanCreateMoreProject(Integer sAccountId)
			throws UsageExceedBillingPlanException;

	void validateAccountCanCreateNewUser(Integer sAccountId)
			throws UsageExceedBillingPlanException;

	void validateAccountCanUploadMoreFiles(Integer sAccountId, long extraBytes)
			throws UsageExceedBillingPlanException;
}
