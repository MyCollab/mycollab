/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.view.opportunity;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
import com.esofthead.mycollab.mobile.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.mobile.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.ui.Field;

public class OpportunityReadFormFieldFactory extends
		AbstractBeanFieldGroupViewFieldFactory<SimpleOpportunity> {
	private static final long serialVersionUID = 1L;

	public OpportunityReadFormFieldFactory(
			GenericBeanForm<SimpleOpportunity> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		Field<?> field = null;
		final SimpleOpportunity opportunity = attachForm.getBean();

		if (propertyId.equals("accountid")) {
			field = new FormViewField(opportunity.getAccountName());
		} else if (propertyId.equals("campaignid")) {
			field = new FormViewField(opportunity.getCampaignName());
		} else if (propertyId.equals("assignuser")) {
			field = new FormViewField(opportunity.getAssignUserFullName());
		} else if (propertyId.equals("expectedcloseddate")) {
			field = new FormViewField(AppContext.formatDate(opportunity
					.getExpectedcloseddate()));
		} else if (propertyId.equals("currencyid")) {
			if (opportunity.getCurrency() != null) {
				return new FormViewField(opportunity.getCurrency()
						.getShortname());
			} else {
				return new FormViewField("");
			}
		}
		return field;
	}

}
