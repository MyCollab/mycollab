package com.mycollab.mobile.module.crm.view.opportunity;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.crm.CrmLinkBuilder;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Opportunity;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunityLeadSource;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunitySalesStage;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunityType;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.field.DefaultViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Field;

class OpportunityReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleOpportunity> {
    private static final long serialVersionUID = 1L;

    OpportunityReadFormFieldFactory(GenericBeanForm<SimpleOpportunity> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        Field<?> field = null;
        final SimpleOpportunity opportunity = attachForm.getBean();

        if (propertyId.equals("accountid")) {
            if (opportunity.getAccountid() != null) {
                A accountLink = new A(CrmLinkBuilder.generateAccountPreviewLinkFull(opportunity.getAccountid()))
                        .appendText(opportunity.getAccountName());
                Div accountDiv = new Div().appendText(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT).getHtml()).appendChild(DivLessFormatter.EMPTY_SPACE, accountLink);
                field = new DefaultViewField(accountDiv.write(), ContentMode.HTML);
            }
        } else if (propertyId.equals("campaignid")) {
            if (opportunity.getCampaignid() != null) {
                A campaignLink = new A(CrmLinkBuilder.generateCampaignPreviewLinkFull(opportunity.getAccountid()))
                        .appendText(opportunity.getCampaignName());
                Div campaignDiv = new Div().appendText(CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN).getHtml()).appendChild(DivLessFormatter.EMPTY_SPACE, campaignLink);
                field = new DefaultViewField(campaignDiv.write(), ContentMode.HTML);
            }
        } else if (propertyId.equals("assignuser")) {
            field = new DefaultViewField(opportunity.getAssignUserFullName());
        } else if (propertyId.equals("expectedcloseddate")) {
            field = new DefaultViewField(UserUIContext.formatDate(opportunity.getExpectedcloseddate()));
        } else if (propertyId.equals("currencyid")) {
            if (opportunity.getCurrencyid() != null) {
                return new DefaultViewField(opportunity.getCurrencyid());
            } else {
                return new DefaultViewField("");
            }
        } else if (Opportunity.Field.salesstage.equalTo(propertyId)) {
            return new I18nFormViewField(opportunity.getSalesstage(), OpportunitySalesStage.class).withStyleName(UIConstants.FIELD_NOTE);
        } else if (Opportunity.Field.opportunitytype.equalTo(propertyId)) {
            return new I18nFormViewField(opportunity.getOpportunitytype(), OpportunityType.class);
        } else if (Opportunity.Field.source.equalTo(propertyId)) {
            return new I18nFormViewField(opportunity.getSource(), OpportunityLeadSource.class);
        }
        return field;
    }

}
