/**
 * This file is part of mycollab-services-community.
 *
 * mycollab-services-community is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services-community is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services-community.  If not, see <http://www.gnu.org/licenses/>.
 */
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
