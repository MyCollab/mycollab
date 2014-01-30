/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.shell;

import com.esofthead.mycollab.module.billing.BillingPlanChecker;
import com.esofthead.mycollab.module.user.domain.SimpleBillingAccount;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class BillingPlanCheckerContext {
	public static boolean isBugComponentEnable() {
		SimpleBillingAccount billingAccount = AppContext.getBillingAccount();
		return (billingAccount == null) ? false : billingAccount
				.getBillingPlan().getHasbugenable();
	}

	public static boolean isStandupComponentEnable() {
		SimpleBillingAccount billingAccount = AppContext.getBillingAccount();
		return (billingAccount == null) ? false : billingAccount
				.getBillingPlan().getHasstandupmeetingenable();
	}

	public static boolean canCreateNewProject() {
		return BillingPlanChecker
				.canCreateNewProject(AppContext.getAccountId());
	}

	public static boolean canCreateNewUser() {
		return BillingPlanChecker.canCreateNewUser(AppContext.getAccountId());
	}

	public static boolean canUploadMoreFiles(long extraBytes) {
		return BillingPlanChecker.canUploadMoreFiles(AppContext.getAccountId(),
				extraBytes);
	}
}
