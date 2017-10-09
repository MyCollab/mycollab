package com.mycollab.module.crm.view.account;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.fielddef.LeadTableFieldDef;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.ui.components.RelatedItemSelectionWindow;
import com.mycollab.module.crm.view.lead.LeadSearchPanel;
import com.mycollab.module.crm.view.lead.LeadTableDisplay;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.WebThemes;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class AccountLeadSelectionWindow extends RelatedItemSelectionWindow<SimpleLead, LeadSearchCriteria> {

    AccountLeadSelectionWindow(AccountLeadListComp associateLeadList) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(LeadI18nEnum.LIST)),
                associateLeadList);
        this.setWidth("1000px");
    }

    @Override
    protected void initUI() {
        tableItem = new LeadTableDisplay(LeadTableFieldDef.selected,
                Arrays.asList(LeadTableFieldDef.name, LeadTableFieldDef.title,
                        LeadTableFieldDef.email, LeadTableFieldDef.phoneoffice));

        tableItem.addGeneratedColumn("leadName", (source, itemId, columnId) -> {
            final SimpleLead lead = tableItem.getBeanByIndex(itemId);

            ELabel b = new ELabel(lead.getLeadName()).withStyleName(WebThemes.BUTTON_LINK)
                    .withDescription(CrmTooltipGenerator.generateTooltipLead(UserUIContext.getUserLocale(), lead,
                            AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
            if ("Dead".equals(lead.getStatus()) || "Converted".equals(lead.getStatus())) {
                b.addStyleName(WebThemes.LINK_COMPLETED);
            }
            return b;
        });

        MButton selectBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_ACTION);

        LeadSearchPanel searchPanel = new LeadSearchPanel(false);
        searchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));

        bodyContent.with(searchPanel, selectBtn, tableItem);
    }

}
