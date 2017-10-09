package com.mycollab.module.crm.view.opportunity;

import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.CrmLinkBuilder;
import com.mycollab.module.crm.domain.Opportunity;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunityLeadSource;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunitySalesStage;
import com.mycollab.module.crm.i18n.OptionI18nEnum.OpportunityType;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.field.CurrencyViewField;
import com.mycollab.vaadin.ui.field.DateViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.mycollab.vaadin.web.ui.field.LinkViewField;
import com.mycollab.vaadin.web.ui.field.UserLinkViewField;
import com.vaadin.ui.Field;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class OpportunityReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleOpportunity> {
    private static final long serialVersionUID = 1L;

    public OpportunityReadFormFieldFactory(GenericBeanForm<SimpleOpportunity> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(Object propertyId) {
        Field<?> field = null;
        final SimpleOpportunity opportunity = attachForm.getBean();

        if (propertyId.equals("accountid")) {
            field = new LinkViewField(opportunity.getAccountName(),
                    CrmLinkBuilder.generateAccountPreviewLinkFull(opportunity.getAccountid()),
                    CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
        } else if (propertyId.equals("campaignid")) {
            field = new LinkViewField(opportunity.getCampaignName(),
                    CrmLinkBuilder.generateCampaignPreviewLinkFull(opportunity.getCampaignid()),
                    CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN));
        } else if (propertyId.equals("assignuser")) {
            field = new UserLinkViewField(opportunity.getAssignuser(), opportunity.getAssignUserAvatarId(),
                    opportunity.getAssignUserFullName());
        } else if (propertyId.equals("expectedcloseddate")) {
            return new DateViewField(opportunity.getExpectedcloseddate());
        } else if (propertyId.equals("currencyid")) {
            return new CurrencyViewField(opportunity.getCurrencyid());
        } else if (propertyId.equals("description")) {
            return new RichTextViewField(opportunity.getDescription());
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
