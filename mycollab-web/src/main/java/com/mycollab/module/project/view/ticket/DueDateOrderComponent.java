/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.ticket;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.SortedArrayMap;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class DueDateOrderComponent extends TicketGroupOrderComponent {
    private SortedArrayMap<Long, DefaultTicketGroupComponent> dueDateAvailables = new SortedArrayMap<>();
    private DefaultTicketGroupComponent unspecifiedTasks;

    @Override
    public void insertTickets(List<ProjectTicket> tickets) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(AppUI.getLongDateFormat()).withLocale(UserUIContext.getUserLocale());
        for (ProjectTicket ticket : tickets) {
            if (ticket.getDueDate() != null) {
                Date dueDate = ticket.getDueDate();
                DateTime jodaTime = new DateTime(dueDate, DateTimeZone.UTC);
                DateTime monDay = jodaTime.dayOfWeek().withMinimumValue();
                String monDayStr = formatter.print(monDay);
                Long time = new LocalDate(monDay).toDate().getTime();
                if (dueDateAvailables.containsKey(time)) {
                    DefaultTicketGroupComponent groupComponent = dueDateAvailables.get(time);
                    groupComponent.insertTicket(ticket);
                } else {
                    DateTime maxValue = monDay.dayOfWeek().withMaximumValue();
                    String sundayStr = formatter.print(maxValue);
                    String titleValue = String.format("%s - %s", monDayStr, sundayStr);

                    DefaultTicketGroupComponent groupComponent = new DefaultTicketGroupComponent(titleValue);
                    dueDateAvailables.put(time, groupComponent);
                    addComponent(groupComponent);
                    groupComponent.insertTicket(ticket);
                }
            } else {
                if (unspecifiedTasks == null) {
                    unspecifiedTasks = new DefaultTicketGroupComponent(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED));
                    addComponent(unspecifiedTasks);
                }
                unspecifiedTasks.insertTicket(ticket);
            }
        }
    }
}
