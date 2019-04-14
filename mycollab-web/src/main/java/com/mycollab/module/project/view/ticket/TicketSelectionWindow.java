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
package com.mycollab.module.project.view.ticket;

import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTooltipGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.fielddef.TicketTableFieldDef;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.TicketTableDisplay;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.ui.FieldSelection;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
class TicketSelectionWindow extends MWindow {
    private static final long serialVersionUID = 1L;

    private FieldSelection<ProjectTicket> fieldSelection;

    TicketSelectionWindow(FieldSelection<ProjectTicket> fieldSelection) {
        super(UserUIContext.getMessage(GenericI18Enum.ACTION_SELECT_VALUE, UserUIContext.getMessage(TicketI18nEnum.SINGLE)));

        this.withWidth("900px").withModal(true).withResizable(false).withCenter();
        this.fieldSelection = fieldSelection;

        DefaultPagedBeanTable<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> tableItem = createTicketTable();
        ProjectTicketSearchCriteria baseCriteria = new ProjectTicketSearchCriteria();
        baseCriteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
        baseCriteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK));
        tableItem.setSearchCriteria(baseCriteria);

        TicketSearchPanel bugSearchPanel = new TicketSearchPanel();
        bugSearchPanel.addSearchHandler(criteria -> {
            criteria.setProjectIds(new SetSearchField<>(CurrentProjectVariables.getProjectId()));
            criteria.setTypes(new SetSearchField<>(ProjectTypeConstants.BUG, ProjectTypeConstants.TASK));
            tableItem.setSearchCriteria(criteria);
        });
        this.setContent(new MVerticalLayout(bugSearchPanel, tableItem));
    }

    private DefaultPagedBeanTable<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> createTicketTable() {
        DefaultPagedBeanTable<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> tableItem = new TicketTableDisplay(
                Arrays.asList(TicketTableFieldDef.name, TicketTableFieldDef.priority, TicketTableFieldDef.status, TicketTableFieldDef.id));
        tableItem.setWidth("100%");
        tableItem.setDisplayNumItems(10);
        tableItem.addGeneratedColumn("name", (source, itemId, columnId) -> {
            ProjectTicket ticket = tableItem.getBeanByIndex(itemId);
            A ticketLink = new A("#").appendText(ProjectAssetsManager.getAsset(ticket.getType()).getHtml() + " " + ticket.getName());

            ELabel ticketLbl = ELabel.html(ticketLink.write());
            if (ticket.isClosed()) {
                ticketLbl.addStyleName(WebThemes.LINK_COMPLETED);
            } else if (ticket.isOverdue()) {
                ticketLbl.addStyleName(WebThemes.LINK_OVERDUE);
            }

            ticketLbl.setDescription(ProjectTooltipGenerator.generateTooltipEntity(UserUIContext.getUserLocale(), AppUI.getDateFormat(),
                    ticket.getType(), ticket.getTypeId(), AppUI.getAccountId(), AppUI.getSiteUrl(), UserUIContext.getUserTimeZone(), false), ContentMode.HTML);
            ticketLbl.addStyleName(WebThemes.TEXT_ELLIPSIS);
            return ticketLbl;
        });
        tableItem.addGeneratedColumn("id", (source, itemId, columnId) -> {
            ProjectTicket ticket = tableItem.getBeanByIndex(itemId);
            MButton selectBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), (Button.ClickListener) clickEvent -> {
                fieldSelection.fireValueChange(ticket);
                close();
            }).withStyleName(WebThemes.BUTTON_ACTION);
            return selectBtn;
        });
        return tableItem;
    }
}
