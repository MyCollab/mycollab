package com.mycollab.mobile.module.crm.view.contact;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.service.ContactService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;

/**
 * @author MyCollab Ltd
 * @since 5.4.9
 */
public class RelatedContactNavigatorButton extends NavigationButton {
    private ContactSearchCriteria criteria;

    public RelatedContactNavigatorButton() {
        super(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(ContactI18nEnum.SINGLE), 0));
        this.addClickListener(navigationButtonClickEvent -> {
            if (criteria != null) {
                getNavigationManager().navigateTo(new ContactListDisplayView(criteria));
            }
        });
    }

    void displayTotalContacts(ContactSearchCriteria criteria) {
        this.criteria = criteria;
        ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(ContactI18nEnum.SINGLE), contactService.getTotalCount(criteria)));
    }

    public void displayRelatedByAccount(Integer accountId) {
        ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
        searchCriteria.setSaccountid(NumberSearchField.equal(AppUI.getAccountId()));
        searchCriteria.setAccountId(NumberSearchField.equal(accountId));
        displayTotalContacts(searchCriteria);
    }

    public void displayRelatedByCampaign(Integer campaignId) {
        ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
        searchCriteria.setCampaignId(NumberSearchField.equal(campaignId));
        displayTotalContacts(searchCriteria);
    }

    public void displayRelatedByOpportunity(Integer opportunityId) {
        ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
        searchCriteria.setOpportunityId(NumberSearchField.equal(opportunityId));
        displayTotalContacts(searchCriteria);
    }

    public void displayRelatedByCase(Integer caseId) {
        ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
        searchCriteria.setCaseId(NumberSearchField.equal(caseId));
        displayTotalContacts(searchCriteria);
    }
}
