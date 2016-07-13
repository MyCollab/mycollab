/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.cases;

import com.mycollab.module.crm.CrmTooltipGenerator;
import com.mycollab.module.crm.domain.SimpleCase;
import com.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.ButtonLink;
import com.vaadin.ui.Window;
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
        super("Case Name Lookup");
        this.withModal(true).withResizable(false).withWidth("1000px");
        this.fieldSelection = fieldSelection;
    }

    public void show() {
        createCaseList();
        CaseSearchPanel searchPanel = new CaseSearchPanel();
        searchPanel.addSearchHandler(criteria -> tableItem.setSearchCriteria(criteria));
        this.setContent(new MVerticalLayout(searchPanel, tableItem));
        tableItem.setSearchCriteria(new CaseSearchCriteria());
        center();
    }

    private void createCaseList() {
        tableItem = new CaseTableDisplay(Arrays.asList(
                CaseTableFieldDef.subject(), CaseTableFieldDef.account(),
                CaseTableFieldDef.priority(), CaseTableFieldDef.status(),
                CaseTableFieldDef.assignUser()));
        tableItem.setDisplayNumItems(10);

        tableItem.addGeneratedColumn("subject", (source, itemId, columnId) -> {
                final SimpleCase cases = tableItem.getBeanByIndex(itemId);

                ButtonLink b = new ButtonLink(cases.getSubject(), clickEvent -> {
                    fieldSelection.fireValueChange(cases);
                    close();
                });
                b.setDescription(CrmTooltipGenerator.generateTooltipCases(
                        AppContext.getUserLocale(), cases, AppContext.getSiteUrl(), AppContext.getUserTimeZone()));
                return b;
        });
    }
}
