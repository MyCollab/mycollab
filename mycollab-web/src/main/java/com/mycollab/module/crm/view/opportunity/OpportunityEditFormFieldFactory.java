package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.domain.Opportunity;
import com.mycollab.module.crm.view.account.AccountSelectionField;
import com.mycollab.module.crm.view.campaign.CampaignSelectionField;
import com.mycollab.module.crm.view.lead.LeadSourceComboBox;
import com.mycollab.module.user.ui.components.ActiveUserComboBox;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.CurrencyComboBoxField;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.web.ui.DoubleField;
import com.mycollab.vaadin.web.ui.IntegerField;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import org.vaadin.viritin.fields.MTextField;

/**
 * @param <B>
 * @author MyCollab Ltd.
 * @since 1.0
 */
class OpportunityEditFormFieldFactory<B extends Opportunity> extends AbstractBeanFieldGroupEditFieldFactory<B> {
    private static final long serialVersionUID = 1L;

    OpportunityEditFormFieldFactory(GenericBeanForm<B> form) {
        super(form);
    }

    OpportunityEditFormFieldFactory(GenericBeanForm<B> form, boolean isValidateForm) {
        super(form, isValidateForm);
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
            MTextField tf = new MTextField();
            if (isValidateForm) {
                tf.withNullRepresentation("").withRequired(true)
                        .withRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
                                UserUIContext.getMessage(GenericI18Enum.FORM_NAME)));
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
            return new RichTextArea();
        } else if (propertyId.equals("assignuser")) {
            return new ActiveUserComboBox();
        } else if (Opportunity.Field.amount.equalTo(propertyId)) {
            return new DoubleField();
        } else if (Opportunity.Field.probability.equalTo(propertyId)) {
            return new IntegerField();
        }

        return null;
    }
}
