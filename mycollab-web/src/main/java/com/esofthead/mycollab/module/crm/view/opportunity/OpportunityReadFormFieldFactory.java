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
package com.esofthead.mycollab.module.crm.view.opportunity;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormLinkViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.FormViewField;
import com.esofthead.mycollab.vaadin.ui.DefaultFormViewFieldFactory.UserLinkViewField;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
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
			field = new FormLinkViewField(opportunity.getAccountName(),
					CrmLinkBuilder.generateAccountPreviewLinkFull(opportunity
							.getAccountid()),
					MyCollabResource
							.newResourceLink("icons/16/crm/account.png"));
		} else if (propertyId.equals("campaignid")) {
			field = 
			new FormLinkViewField(opportunity.getCampaignName(),
					CrmLinkBuilder.generateCampaignPreviewLinkFull(opportunity
							.getCampaignid()),
					MyCollabResource
							.newResourceLink("icons/16/crm/campaign.png"));
		} else if (propertyId.equals("assignuser")) {
			field = new UserLinkViewField(opportunity.getAssignuser(),
					opportunity.getAssignUserAvatarId(),
					opportunity.getAssignUserFullName());
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
