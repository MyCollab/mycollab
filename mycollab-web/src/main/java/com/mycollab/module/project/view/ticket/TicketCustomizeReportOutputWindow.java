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

import com.mycollab.common.TableViewField;
import com.mycollab.db.query.VariableInjector;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.fielddef.TicketTableFieldDef;
import com.mycollab.module.project.i18n.OptionI18nEnum.Priority;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.reporting.CustomizeReportOutputWindow;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author MyCollab Ltd
 * @since 5.3.4
 */
public class TicketCustomizeReportOutputWindow extends CustomizeReportOutputWindow<ProjectTicketSearchCriteria, ProjectTicket> {
    public TicketCustomizeReportOutputWindow(VariableInjector<ProjectTicketSearchCriteria> variableInjector) {
        super(ProjectTypeConstants.TICKET, UserUIContext.getMessage(TicketI18nEnum.LIST), ProjectTicket.class,
                AppContextUtil.getSpringBean(ProjectTicketService.class), variableInjector);
    }

    @Override
    protected Object[] buildSampleData() {
        return new Object[]{"Task A", "Description 1", UserUIContext.formatDate(LocalDateTime.now().minusDays(2)),
                UserUIContext.formatDate(LocalDateTime.now().plusDays(1)), UserUIContext.formatDate(LocalDateTime.now().plusDays(1)),
                Priority.High.name(), "Will Smith", "Jonh Adam", "MVP", "3", "1"};
    }

    @Override
    protected Set<TableViewField> getDefaultColumns() {
        return new HashSet<>(Arrays.asList(TicketTableFieldDef.name, TicketTableFieldDef.startdate, TicketTableFieldDef.duedate,
                TicketTableFieldDef.priority, TicketTableFieldDef.assignee,
                TicketTableFieldDef.billableHours, TicketTableFieldDef.nonBillableHours));
    }

    @Override
    protected Set<TableViewField> getAvailableColumns() {
        return new HashSet<>(Arrays.asList(TicketTableFieldDef.name, TicketTableFieldDef.description, TicketTableFieldDef.startdate,
                TicketTableFieldDef.enddate, TicketTableFieldDef.duedate,
                TicketTableFieldDef.priority, TicketTableFieldDef.logUser,
                TicketTableFieldDef.assignee, TicketTableFieldDef.milestoneName,
                TicketTableFieldDef.billableHours, TicketTableFieldDef.nonBillableHours));
    }
}
