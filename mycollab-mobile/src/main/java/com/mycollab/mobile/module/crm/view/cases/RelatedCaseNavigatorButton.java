package com.mycollab.mobile.module.crm.view.cases;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
import com.mycollab.module.crm.service.CaseService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;

/**
 * @author Hai Nguyen
 * @since 5.4.9
 */
public class RelatedCaseNavigatorButton extends NavigationButton {
    private CaseSearchCriteria criteria;

    public RelatedCaseNavigatorButton() {
        super(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(CaseI18nEnum.SINGLE), 0));
        this.addClickListener(navigationButtonClickEvent -> {
            if (criteria != null) {
                getNavigationManager().navigateTo(new CaseListDisplayView(criteria));
            }
        });
    }

    void displayTotalCases(CaseSearchCriteria criteria) {
        this.criteria = criteria;
        CaseService caseService = AppContextUtil.getSpringBean(CaseService.class);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(CaseI18nEnum.SINGLE), caseService.getTotalCount(criteria)));
    }

    public void displayRelatedByAccount(Integer accountId) {
        CaseSearchCriteria searchCriteria = new CaseSearchCriteria();
        searchCriteria.setSaccountid(NumberSearchField.equal(AppUI.getAccountId()));
        searchCriteria.setAccountId(NumberSearchField.equal(accountId));
        displayTotalCases(searchCriteria);
    }
}
