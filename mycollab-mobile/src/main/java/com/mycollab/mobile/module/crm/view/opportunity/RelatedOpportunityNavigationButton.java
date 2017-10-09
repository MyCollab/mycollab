package com.mycollab.mobile.module.crm.view.opportunity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.mycollab.module.crm.service.OpportunityService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;

/**
 * @author MyCollab Ltd
 * @since 5.4.9
 */
public class RelatedOpportunityNavigationButton extends NavigationButton {
    private OpportunitySearchCriteria criteria;

    public RelatedOpportunityNavigationButton() {
        super(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(OpportunityI18nEnum.SINGLE), 0));
        this.addClickListener(navigationButtonClickEvent -> {
            if (criteria != null) {
                getNavigationManager().navigateTo(new OpportunityListDisplayView(criteria));
            }
        });
    }

    void displayTotalOpportunities(OpportunitySearchCriteria criteria) {
        this.criteria = criteria;
        OpportunityService opportunityService = AppContextUtil.getSpringBean(OpportunityService.class);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(OpportunityI18nEnum.SINGLE), opportunityService.getTotalCount(criteria)));
    }

    public void displayRelatedByAccount(Integer accountId) {
        OpportunitySearchCriteria searchCriteria = new OpportunitySearchCriteria();
        searchCriteria.setSaccountid(NumberSearchField.equal(AppUI.getAccountId()));
        searchCriteria.setAccountId(NumberSearchField.equal(accountId));
        displayTotalOpportunities(searchCriteria);
    }

    public void displayRelatedByContact(Integer contactId) {
        OpportunitySearchCriteria searchCriteria = new OpportunitySearchCriteria();
        searchCriteria.setContactId(NumberSearchField.equal(contactId));
        displayTotalOpportunities(searchCriteria);
    }
}
