/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.bug;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTooltipGenerator;
import com.mycollab.module.project.fielddef.BugTableFieldDef;
import com.mycollab.module.project.i18n.BugI18nEnum;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.mycollab.module.tracker.service.BugService;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.shared.ui.ContentMode;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class BugSelectionWindow extends MWindow {
    private static final long serialVersionUID = 1L;

    private FieldSelection<SimpleBug> fieldSelection;

    BugSelectionWindow(FieldSelection<SimpleBug> fieldSelection) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(BugI18nEnum.SINGLE)));

        this.withWidth("900px").withModal(true).withResizable(false).withCenter();
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
        this.setContent(new MVerticalLayout(bugSearchPanel, tableItem));
    }

    private DefaultPagedBeanTable<BugService, BugSearchCriteria, SimpleBug> createBugTable() {
        final DefaultPagedBeanTable<BugService, BugSearchCriteria, SimpleBug> tableItem = new BugTableDisplay(
                Arrays.asList(BugTableFieldDef.summary, BugTableFieldDef.severity, BugTableFieldDef.resolution));
        tableItem.setWidth("100%");
        tableItem.setDisplayNumItems(10);
        tableItem.addGeneratedColumn("name", (source, itemId, columnId) -> {
            final SimpleBug bug = tableItem.getBeanByIndex(itemId);

            MButton bugLink = new MButton(bug.getName(), clickEvent -> {
                fieldSelection.fireValueChange(bug);
                close();
            }).withStyleName(WebThemes.BUTTON_LINK).withFullWidth();

            if (bug.isCompleted()) {
                bugLink.addStyleName(WebThemes.LINK_COMPLETED);
            } else if (bug.isOverdue()) {
                bugLink.addStyleName(WebThemes.LINK_OVERDUE);
            }

            bugLink.setDescription(ProjectTooltipGenerator.generateToolTipBug(UserUIContext.getUserLocale(), AppUI.getDateFormat(),
                    bug, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone(), false), ContentMode.HTML);
            bugLink.addStyleName(WebThemes.TEXT_ELLIPSIS);
            return bugLink;
        });
        return tableItem;
    }
}
