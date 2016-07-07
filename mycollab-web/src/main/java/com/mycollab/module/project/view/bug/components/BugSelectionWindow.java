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
package com.mycollab.module.project.view.bug.components;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTooltipGenerator;
import com.mycollab.module.project.view.bug.BugSearchPanel;
import com.mycollab.module.project.view.bug.BugTableDisplay;
import com.mycollab.module.project.view.bug.BugTableFieldDef;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.events.SearchHandler;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.ui.UIUtils;
import com.mycollab.vaadin.web.ui.ButtonLink;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;
import org.vaadin.jouni.restrain.Restrain;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class BugSelectionWindow extends Window {
    private static final long serialVersionUID = 1L;

    private FieldSelection<SimpleBug> fieldSelection;

    public BugSelectionWindow(FieldSelection<SimpleBug> fieldSelection) {
        super("Bug Selection");

        this.setWidth("900px");
        this.setModal(true);
        this.setResizable(false);
        this.fieldSelection = fieldSelection;

        final DefaultPagedBeanTable<BugService, BugSearchCriteria, SimpleBug> tableItem = createBugTable();
        BugSearchCriteria baseCriteria = new BugSearchCriteria();
        baseCriteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
        tableItem.setSearchCriteria(baseCriteria);

        BugSearchPanel bugSearchPanel = new BugSearchPanel(false);
        bugSearchPanel.addSearchHandler(criteria -> {
            criteria.setProjectId(new NumberSearchField(CurrentProjectVariables.getProjectId()));
            tableItem.setSearchCriteria(criteria);
        });
        new Restrain(tableItem).setMaxHeight((UIUtils.getBrowserHeight() - 120) + "px");
        this.setContent(new MVerticalLayout(bugSearchPanel, tableItem));
    }

    private DefaultPagedBeanTable<BugService, BugSearchCriteria, SimpleBug> createBugTable() {
        final DefaultPagedBeanTable<BugService, BugSearchCriteria, SimpleBug> tableItem = new BugTableDisplay(
                Arrays.asList(BugTableFieldDef.summary(), BugTableFieldDef.severity(), BugTableFieldDef.resolution()));
        tableItem.setWidth("100%");
        tableItem.setDisplayNumItems(10);
        tableItem.addGeneratedColumn("summary", (source, itemId, columnId) -> {
            final SimpleBug bug = tableItem.getBeanByIndex(itemId);

            ButtonLink b = new ButtonLink(bug.getSummary(), clickEvent -> {
                fieldSelection.fireValueChange(bug);
                close();
            });

            if (bug.isCompleted()) {
                b.addStyleName(UIConstants.LINK_COMPLETED);
            } else if (bug.isOverdue()) {
                b.addStyleName(UIConstants.LINK_OVERDUE);
            }

            b.setDescription(ProjectTooltipGenerator.generateToolTipBug(AppContext.getUserLocale(), AppContext.getDateFormat(),
                    bug, AppContext.getSiteUrl(), AppContext.getUserTimeZone(), false));
            return b;
        });
        return tableItem;
    }
}
