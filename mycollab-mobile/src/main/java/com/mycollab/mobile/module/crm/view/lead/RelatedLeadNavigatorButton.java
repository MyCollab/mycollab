package com.mycollab.mobile.module.crm.view.lead;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.service.LeadService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;

/**
 * @author MyCollab Ltd
 * @since 5.4.9
 */
public class RelatedLeadNavigatorButton extends NavigationButton {
    private LeadSearchCriteria criteria;

    public RelatedLeadNavigatorButton() {
        super(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(LeadI18nEnum.SINGLE), 0));
        this.addClickListener(navigationButtonClickEvent -> {
            if (criteria != null) {
                getNavigationManager().navigateTo(new LeadListDisplayView(criteria));
            }
        });
    }

    void displayTotalLeads(LeadSearchCriteria criteria) {
        this.criteria = criteria;
        LeadService leadService = AppContextUtil.getSpringBean(LeadService.class);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(LeadI18nEnum.SINGLE), leadService.getTotalCount(criteria)));
    }

    public void displayRelatedByAccount(Integer accountId) {
        LeadSearchCriteria searchCriteria = new LeadSearchCriteria();
        searchCriteria.setSaccountid(NumberSearchField.equal(AppUI.getAccountId()));
        searchCriteria.setAccountId(NumberSearchField.equal(accountId));
        displayTotalLeads(searchCriteria);
    }

    public void displayRelatedByCampaign(Integer campaignId) {
        LeadSearchCriteria searchCriteria = new LeadSearchCriteria();
        searchCriteria.setCampaignId(NumberSearchField.equal(campaignId));
        displayTotalLeads(searchCriteria);
    }

    public void displayRelatedByOpportunity(Integer opportunityId) {
        LeadSearchCriteria searchCriteria = new LeadSearchCriteria();
        searchCriteria.setOpportunityId(NumberSearchField.equal(opportunityId));
        displayTotalLeads(searchCriteria);
    }
}
