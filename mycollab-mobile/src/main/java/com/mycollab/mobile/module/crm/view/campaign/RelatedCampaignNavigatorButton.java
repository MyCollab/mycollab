package com.mycollab.mobile.module.crm.view.campaign;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.module.crm.service.CampaignService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;

/**
 * @author MyCollab Ltd
 * @since 5.4.9
 */
public class RelatedCampaignNavigatorButton extends NavigationButton {
    private CampaignSearchCriteria criteria;

    public RelatedCampaignNavigatorButton() {
        super(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(CampaignI18nEnum.SINGLE), 0));
        this.addClickListener(navigationButtonClickEvent -> {
            if (criteria != null) {
                getNavigationManager().navigateTo(new CampaignListDisplayView(criteria));
            }
        });
    }

    void displayTotalCampaigns(CampaignSearchCriteria criteria) {
        this.criteria = criteria;
        CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
        this.setCaption(UserUIContext.getMessage(GenericI18Enum.OPT_ITEM_VALUE,
                UserUIContext.getMessage(CampaignI18nEnum.SINGLE), campaignService.getTotalCount(criteria)));
    }

    public void displayRelatedByLead(Integer leadId) {
        CampaignSearchCriteria searchCriteria = new CampaignSearchCriteria();
        searchCriteria.setLeadId(NumberSearchField.equal(leadId));
        displayTotalCampaigns(searchCriteria);
    }
}
