package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.Opportunity;
import com.mycollab.module.crm.domain.SimpleOpportunity;
import com.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.mycollab.module.crm.fielddef.OpportunityTableFieldDef;
import com.mycollab.module.crm.i18n.OpportunityI18nEnum;
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
public class OpportunitySelectionWindow extends MWindow {
    private static final long serialVersionUID = 1L;

    private OpportunityTableDisplay tableItem;
    private FieldSelection fieldSelection;

    public OpportunitySelectionWindow(FieldSelection fieldSelection) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(OpportunityI18nEnum.SINGLE)));
        this.withModal(true).withResizable(false).withWidth("1000px").withCenter();
        this.fieldSelection = fieldSelection;
    }

    public void show() {
        createOpportunityList();
        OpportunitySearchPanel searchPanel = new OpportunitySearchPanel(false);
        searchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));
        this.setContent(new MVerticalLayout(searchPanel, tableItem));

        tableItem.setSearchCriteria(new OpportunitySearchCriteria());
    }

    private void createOpportunityList() {
        tableItem = new OpportunityTableDisplay(Arrays.asList(OpportunityTableFieldDef.opportunityName,
                OpportunityTableFieldDef.saleStage, OpportunityTableFieldDef.accountName,
                OpportunityTableFieldDef.assignUser));
        tableItem.setDisplayNumItems(10);
        tableItem.setWidth("100%");

        tableItem.addGeneratedColumn(Opportunity.Field.opportunityname.name(), (source, itemId, columnId) -> {
            final SimpleOpportunity opportunity = tableItem.getBeanByIndex(itemId);

            return new MButton(opportunity.getOpportunityname(), clickEvent -> {
                fieldSelection.fireValueChange(opportunity);
                close();
            }).withStyleName(WebThemes.BUTTON_LINK).withDescription(CrmTooltipGenerator.generateTooltipOpportunity(UserUIContext.getUserLocale(), AppUI.getDateFormat(),
                    opportunity, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
        });
    }
}
