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
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
class ProjectInformationLayout implements IFormLayoutFactory {
	private static final long serialVersionUID = 1L;
	private GridFormLayoutHelper moreInfoLayout;

	@Override
	public void attachField(final Object propertyId, final Field<?> field) {
		if (propertyId.equals("homepage")) {
			this.moreInfoLayout.addComponent(field,
					AppContext.getMessage(ProjectI18nEnum.FORM_HOME_PAGE), 0,
					0, Alignment.TOP_LEFT);
		} else if (propertyId.equals("projectstatus")) {
			this.moreInfoLayout.addComponent(field,
					AppContext.getMessage(ProjectI18nEnum.FORM_STATUS), 1, 0,
					Alignment.TOP_LEFT);
		} else if (propertyId.equals("planstartdate")) {
			this.moreInfoLayout
					.addComponent(field, AppContext
							.getMessage(ProjectI18nEnum.FORM_PLAN_START_DATE),
							0, 1, Alignment.TOP_LEFT);
		} else if (propertyId.equals("currencyid")) {
			this.moreInfoLayout.addComponent(field,
					AppContext.getMessage(ProjectI18nEnum.FORM_CURRENCY), 1, 1,
					Alignment.TOP_LEFT);
		} else if (propertyId.equals("planenddate")) {
			this.moreInfoLayout.addComponent(field,
					AppContext.getMessage(ProjectI18nEnum.FORM_PLAN_END_DATE),
					0, 2, Alignment.TOP_LEFT);
		} else if (propertyId.equals("defaultbillingrate")) {
			this.moreInfoLayout.addComponent(field,
					AppContext.getMessage(ProjectI18nEnum.FORM_BILLING_RATE),
					1, 2, Alignment.TOP_LEFT);
		} else if (propertyId.equals("actualstartdate")) {
			this.moreInfoLayout.addComponent(field, AppContext
					.getMessage(ProjectI18nEnum.FORM_ACTUAL_START_DATE), 0, 3,
					Alignment.TOP_LEFT);
		} else if (propertyId.equals("targetbudget")) {
			this.moreInfoLayout.addComponent(field,
					AppContext.getMessage(ProjectI18nEnum.FORM_TARGET_BUDGET),
					1, 3, Alignment.TOP_LEFT);
		} else if (propertyId.equals("actualenddate")) {
			this.moreInfoLayout
					.addComponent(field, AppContext
							.getMessage(ProjectI18nEnum.FORM_ACTUAL_END_DATE),
							0, 4, Alignment.TOP_LEFT);
		} else if (propertyId.equals("actualbudget")) {
			this.moreInfoLayout.addComponent(field,
					AppContext.getMessage(ProjectI18nEnum.FORM_ACTUAL_BUDGET),
					1, 4, Alignment.TOP_LEFT);
		} else if (propertyId.equals("totalBillableHours")) {
			this.moreInfoLayout.addComponent(field,
					AppContext.getMessage(ProjectI18nEnum.FORM_BILLABLE_HOURS),
					0, 5, Alignment.TOP_LEFT);
		} else if (propertyId.equals("totalNonBillableHours")) {
			this.moreInfoLayout.addComponent(field, AppContext
					.getMessage(ProjectI18nEnum.FORM_NON_BILLABLE_HOURS), 1, 5,
					Alignment.TOP_LEFT);
		} else if (propertyId.equals("description")) {
			this.moreInfoLayout.addComponent(field,
					AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0,
					6, 2, "100%", Alignment.TOP_LEFT);
		}
	}

	@Override
	public ComponentContainer getLayout() {
		this.moreInfoLayout = new GridFormLayoutHelper(2, 7, "100%", "167px",
				Alignment.TOP_LEFT);
		this.moreInfoLayout.getLayout().setWidth("100%");
		this.moreInfoLayout.getLayout().setMargin(false);
		this.moreInfoLayout.getLayout().addStyleName("colored-gridlayout");

		return this.moreInfoLayout.getLayout();
	}
}
