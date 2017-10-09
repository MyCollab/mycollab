package com.mycollab.module.crm.view.cases;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.module.crm.fielddef.CaseTableFieldDef;
import com.mycollab.module.crm.i18n.CaseI18nEnum;
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
public class CaseSelectionWindow extends MWindow {
    private static final long serialVersionUID = 1L;
    private CaseTableDisplay tableItem;
    private FieldSelection fieldSelection;

    public CaseSelectionWindow(FieldSelection fieldSelection) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(CaseI18nEnum.SINGLE)));
        this.withModal(true).withResizable(false).withWidth("1000px").withCenter();
        this.fieldSelection = fieldSelection;
    }

    public void show() {
        createCaseList();
        CaseSearchPanel searchPanel = new CaseSearchPanel(false);
        searchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));
        this.setContent(new MVerticalLayout(searchPanel, tableItem));
        tableItem.setSearchCriteria(new CaseSearchCriteria());
    }

    private void createCaseList() {
        tableItem = new CaseTableDisplay(Arrays.asList(CaseTableFieldDef.subject, CaseTableFieldDef.account,
                CaseTableFieldDef.priority, CaseTableFieldDef.status, CaseTableFieldDef.assignUser));
        tableItem.setDisplayNumItems(10);

        tableItem.addGeneratedColumn("subject", (source, itemId, columnId) -> {
            final SimpleCase cases = tableItem.getBeanByIndex(itemId);

            return new MButton(cases.getSubject(), clickEvent -> {
                fieldSelection.fireValueChange(cases);
                close();
            }).withStyleName(WebThemes.BUTTON_LINK).withDescription(CrmTooltipGenerator.generateTooltipCases(
                    UserUIContext.getUserLocale(), cases, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
        });
    }
}
