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
package com.esofthead.mycollab.module.crm.view.cases;

import com.esofthead.mycollab.module.crm.CrmTooltipGenerator;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.SearchHandler;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.FieldSelection;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public class CaseSelectionWindow extends Window {
    private static final long serialVersionUID = 1L;
    private CaseTableDisplay tableItem;
    private FieldSelection fieldSelection;

    public CaseSelectionWindow(FieldSelection fieldSelection) {
        super("Case Name Lookup");
        this.setWidth("900px");
        this.fieldSelection = fieldSelection;
        this.setModal(true);
        this.setResizable(false);
    }

    public void show() {
        MVerticalLayout layout = new MVerticalLayout();
        createCaseList();
        CaseSimpleSearchPanel caseSimpleSearchPanel = new CaseSimpleSearchPanel();
        caseSimpleSearchPanel.addSearchHandler(new SearchHandler<CaseSearchCriteria>() {

            @Override
            public void onSearch(CaseSearchCriteria criteria) {
                tableItem.setSearchCriteria(criteria);
            }

        });
        layout.addComponent(caseSimpleSearchPanel);
        layout.addComponent(tableItem);
        this.setContent(layout);

        tableItem.setSearchCriteria(new CaseSearchCriteria());
        center();
    }

    @SuppressWarnings("serial")
    private void createCaseList() {
        tableItem = new CaseTableDisplay(Arrays.asList(
                CaseTableFieldDef.subject(), CaseTableFieldDef.account(),
                CaseTableFieldDef.priority(), CaseTableFieldDef.status(),
                CaseTableFieldDef.assignUser()));
        tableItem.setDisplayNumItems(10);

        tableItem.addGeneratedColumn("subject", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component generateCell(Table source, Object itemId, Object columnId) {
                final SimpleCase cases = tableItem.getBeanByIndex(itemId);

                ButtonLink b = new ButtonLink(cases.getSubject(), new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        fieldSelection.fireValueChange(cases);
                        CaseSelectionWindow.this.close();
                    }
                });
                b.setDescription(CrmTooltipGenerator.generateTooltipCases(
                        AppContext.getUserLocale(), cases, AppContext.getSiteUrl(), AppContext.getUserTimezone()));
                return b;
            }
        });
    }
}
