package com.mycollab.module.crm.view.lead;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.Lead;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.fielddef.LeadTableFieldDef;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.WebThemes;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LeadSelectionWindow extends MWindow {
    private static final long serialVersionUID = 1L;
    private LeadTableDisplay tableItem;
    private FieldSelection<Lead> fieldSelection;

    public LeadSelectionWindow(FieldSelection<Lead> fieldSelection) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(LeadI18nEnum.SINGLE)));
        this.withModal(true).withResizable(false).withWidth("1000px").withCenter();
        this.fieldSelection = fieldSelection;
    }

    public void show() {
        createLeadList();

        LeadSearchPanel searchPanel = new LeadSearchPanel(false);
        searchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));

        this.setContent(new MVerticalLayout(searchPanel, tableItem));
    }

    private void createLeadList() {
        tableItem = new LeadTableDisplay(Arrays.asList(LeadTableFieldDef.name, LeadTableFieldDef.status,
                LeadTableFieldDef.assignedUser, LeadTableFieldDef.accountName));
        tableItem.setDisplayNumItems(10);
        tableItem.setWidth("100%");

        tableItem.addGeneratedColumn("leadName", (source, itemId, columnId) -> {
            final SimpleLead lead = tableItem.getBeanByIndex(itemId);
            return new MButton(lead.getLeadName(), clickEvent -> {
                fieldSelection.fireValueChange(lead);
                close();
            }).withStyleName(WebThemes.BUTTON_LINK, WebThemes.BUTTON_SMALL_PADDING)
                    .withDescription(CrmTooltipGenerator.generateTooltipLead(UserUIContext.getUserLocale(), lead,
                            AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
        });
        tableItem.setSearchCriteria(new LeadSearchCriteria());
    }
}
