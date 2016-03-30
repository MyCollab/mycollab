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

import com.esofthead.mycollab.mobile.module.crm.view.account.AccountSelectionField;
import com.esofthead.mycollab.mobile.module.crm.view.campaign.CampaignSelectionField;
import com.esofthead.mycollab.mobile.module.crm.view.lead.LeadSourceComboBox;
import com.esofthead.mycollab.mobile.module.user.ui.components.ActiveUserComboBox;
import com.esofthead.mycollab.mobile.ui.CurrencyComboBoxField;
import com.esofthead.mycollab.module.crm.domain.Opportunity;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.fields.MTextArea;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 4.1
 */
class OpportunityEditFormFieldFactory<B extends Opportunity> extends AbstractBeanFieldGroupEditFieldFactory<B> {
    private static final long serialVersionUID = 1L;

    OpportunityEditFormFieldFactory(GenericBeanForm<B> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {

        if (propertyId.equals("campaignid")) {
            return new CampaignSelectionField();
        } else if (propertyId.equals("accountid")) {
            AccountSelectionField accountField = new AccountSelectionField();
            accountField.setRequired(true);
            return accountField;
        } else if (propertyId.equals("opportunityname")) {
            TextField tf = new TextField();
            if (isValidateForm) {
                tf.setNullRepresentation("");
                tf.setRequired(true);
                tf.setRequiredError("Name must not be null");
            }
            return tf;
        } else if (propertyId.equals("currencyid")) {
            return new CurrencyComboBoxField();
        } else if (propertyId.equals("salesstage")) {
            return new OpportunitySalesStageComboBox();
        } else if (propertyId.equals("opportunitytype")) {
            return new OpportunityTypeComboBox();
        } else if (propertyId.equals("source")) {
            return new LeadSourceComboBox();
        } else if (propertyId.equals("description")) {
            return new MTextArea();
        } else if (propertyId.equals("assignuser")) {
            return new ActiveUserComboBox();
        }

        return null;
    }
}
