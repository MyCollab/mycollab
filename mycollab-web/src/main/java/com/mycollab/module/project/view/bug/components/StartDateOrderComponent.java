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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.vaadin.AppContext;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class StartDateOrderComponent extends BugGroupOrderComponent {
    private Map<DateTime, DefaultBugGroupComponent> startDateAvailables = new TreeMap<>();
    private DefaultBugGroupComponent unspecifiedTasks;

    @Override
    public void insertBugs(List<SimpleBug> bugs) {
        for (SimpleBug bug : bugs) {
            if (bug.getCreatedtime() != null) {
                Date startDate = bug.getCreatedtime();
                DateTime jodaTime = new DateTime(startDate, DateTimeZone.UTC);
                DateTime monDay = jodaTime.dayOfWeek().withMinimumValue();
                if (startDateAvailables.containsKey(monDay)) {
                    DefaultBugGroupComponent groupComponent = startDateAvailables.get(monDay);
                    groupComponent.insertBug(bug);
                } else {
                    DateTime maxValue = monDay.dayOfWeek().withMaximumValue();
                    DateTimeFormatter formatter = DateTimeFormat.forPattern(AppContext.getLongDateFormat());
                    String monDayStr = formatter.print(monDay);
                    String sundayStr = formatter.print(maxValue);
                    String titleValue = String.format("%s - %s", monDayStr, sundayStr);

                    DefaultBugGroupComponent groupComponent = new DefaultBugGroupComponent(titleValue);
                    startDateAvailables.put(monDay, groupComponent);
                    addComponent(groupComponent);
                    groupComponent.insertBug(bug);
                }
            } else {
                if (unspecifiedTasks == null) {
                    unspecifiedTasks = new DefaultBugGroupComponent(AppContext.getMessage(GenericI18Enum.OPT_UNDEFINED));
                    addComponent(unspecifiedTasks, 0);
                }
                unspecifiedTasks.insertBug(bug);
            }
        }
    }
}