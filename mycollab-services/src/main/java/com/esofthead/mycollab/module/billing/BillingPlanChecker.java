/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.billing;

import com.esofthead.mycollab.module.billing.service.BillingService;
import com.esofthead.mycollab.module.ecm.domain.DriveInfo;
import com.esofthead.mycollab.module.ecm.service.DriveInfoService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.user.domain.BillingPlan;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class BillingPlanChecker {

	public static boolean canCreateNewProject(int sAccountId) {
		BillingService billingService = ApplicationContextUtil
				.getSpringBean(BillingService.class);
		BillingPlan billingPlan = billingService.findBillingPlan(sAccountId);

		ProjectService projectService = ApplicationContextUtil
				.getSpringBean(ProjectService.class);
		Integer numOfActiveProjects = projectService
				.getTotalActiveProjectsInAccount(sAccountId);

		return (numOfActiveProjects < billingPlan.getNumprojects());
	}

	public static boolean canCreateNewUser(int sAccountId) {
		BillingService billingService = ApplicationContextUtil
				.getSpringBean(BillingService.class);
		BillingPlan billingPlan = billingService.findBillingPlan(sAccountId);

		UserService userService = ApplicationContextUtil
				.getSpringBean(UserService.class);
		int numOfUsers = userService.getTotalActiveUsersInAccount(sAccountId);
		return (numOfUsers < billingPlan.getNumusers());
	}

	public static boolean canUploadMoreFiles(int sAccountId, long extraBytes) {
		BillingService billingService = ApplicationContextUtil
				.getSpringBean(BillingService.class);
		BillingPlan billingPlan = billingService.findBillingPlan(sAccountId);

		DriveInfoService driveInfoService = ApplicationContextUtil
				.getSpringBean(DriveInfoService.class);
		DriveInfo driveInfo = driveInfoService.getDriveInfo(sAccountId);
		return (driveInfo.getUsedvolume() + extraBytes <= billingPlan
				.getVolume());
	}
}
