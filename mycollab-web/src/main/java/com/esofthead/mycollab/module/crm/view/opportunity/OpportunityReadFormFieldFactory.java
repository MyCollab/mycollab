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

import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.data.CrmLinkBuilder;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.form.field.*;
import com.vaadin.ui.Field;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
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
			field = new LinkViewField(opportunity.getAccountName(),
					CrmLinkBuilder.generateAccountPreviewLinkFull(opportunity
							.getAccountid()),
					CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
		} else if (propertyId.equals("campaignid")) {
			field = new LinkViewField(opportunity.getCampaignName(),
					CrmLinkBuilder.generateCampaignPreviewLinkFull(opportunity
							.getCampaignid()),
                    CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN));
		} else if (propertyId.equals("assignuser")) {
			field = new UserLinkViewField(opportunity.getAssignuser(),
					opportunity.getAssignUserAvatarId(),
					opportunity.getAssignUserFullName());
		} else if (propertyId.equals("expectedcloseddate")) {
			return new DateViewField(opportunity.getExpectedcloseddate());
		} else if (propertyId.equals("currencyid")) {
			String currency = (opportunity.getCurrency() != null) ? opportunity
					.getCurrency().getShortname() : "";
			return new DefaultViewField(currency);
		} else if (propertyId.equals("description")) {
			return new RichTextViewField(opportunity.getDescription());
		}

		return field;
	}

}
