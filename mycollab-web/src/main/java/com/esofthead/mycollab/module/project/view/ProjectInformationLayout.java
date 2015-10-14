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

package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.project.i18n.ProjectI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.grid.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.AbstractFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ProjectInformationLayout extends AbstractFormLayoutFactory {
	private static final long serialVersionUID = 1L;
	private GridFormLayoutHelper moreInfoLayout;

	@Override
	protected void onAttachField(final Object propertyId, final Field<?> field) {
		if (propertyId.equals("homepage")) {
			moreInfoLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_HOME_PAGE), 0, 0);
		} else if (propertyId.equals("projectstatus")) {
			moreInfoLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_STATUS), 1, 0);
		} else if (propertyId.equals("planstartdate")) {
			moreInfoLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_PLAN_START_DATE), 0, 1);
		} else if (propertyId.equals("currencyid")) {
			moreInfoLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_CURRENCY), 1, 1);
		} else if (propertyId.equals("planenddate")) {
			moreInfoLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_PLAN_END_DATE), 0, 2);
		} else if (propertyId.equals("defaultbillingrate")) {
			moreInfoLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_BILLING_RATE), 1, 2);
		} else if (propertyId.equals("actualstartdate")) {
			moreInfoLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_ACTUAL_START_DATE), 0, 3);
		} else if (propertyId.equals("targetbudget")) {
			moreInfoLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_TARGET_BUDGET), 1, 3);
		} else if (propertyId.equals("actualenddate")) {
			moreInfoLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_ACTUAL_END_DATE), 0, 4);
		} else if (propertyId.equals("actualbudget")) {
			moreInfoLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_ACTUAL_BUDGET), 1, 4);
		} else if (propertyId.equals("totalBillableHours")) {
			moreInfoLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_BILLABLE_HOURS), 0, 5);
		} else if (propertyId.equals("totalNonBillableHours")) {
			moreInfoLayout.addComponent(field, AppContext.getMessage(ProjectI18nEnum.FORM_NON_BILLABLE_HOURS), 1, 5);
		} else if (propertyId.equals("description")) {
			moreInfoLayout.addComponent(field, AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0, 6, 2, "100%");
		}
	}

	@Override
	public ComponentContainer getLayout() {
		moreInfoLayout =  GridFormLayoutHelper.defaultFormLayoutHelper(2, 7);
		return moreInfoLayout.getLayout();
	}
}
