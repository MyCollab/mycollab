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
package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.core.utils.SortedArrayMap;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
public class StartDateOrderComponent extends TaskGroupOrderComponent {
    private SortedArrayMap<DateTime, GroupComponent> startDateAvailables = new SortedArrayMap<>();
    private GroupComponent unspecifiedTasks;

    @Override
    void insertTasks(List<SimpleTask> tasks) {
        for (SimpleTask task : tasks) {
            if (task.getStartdate() != null) {
                Date startDate = task.getStartdate();
                DateTime jodaTime = new DateTime(startDate, DateTimeZone.UTC);
                DateTime monDay = jodaTime.dayOfWeek().withMinimumValue();
                if (startDateAvailables.containsKey(monDay)) {
                    GroupComponent groupComponent = startDateAvailables.get(monDay);
                    groupComponent.insertTask(task);
                } else {
                    GroupComponent groupComponent = new GroupComponent(monDay);
                    startDateAvailables.put(monDay, groupComponent);
                    int index = startDateAvailables.getKeyIndex(monDay);
                    if (index > -1) {
                        addComponent(groupComponent, index);
                    } else {
                        addComponent(groupComponent);
                    }

                    groupComponent.insertTask(task);
                }
            } else {
                if (unspecifiedTasks == null) {
                    unspecifiedTasks = new GroupComponent();
                    addComponent(unspecifiedTasks, 0);
                }
                unspecifiedTasks.insertTask(task);
            }
        }
    }

    private static class GroupComponent extends VerticalLayout {
        private CssLayout wrapBody;
        private Label headerLbl;

        GroupComponent(DateTime startDate) {
            initComponent();
            DateTime maxValue = startDate.dayOfWeek().withMaximumValue();
            DateTimeFormatter fomatter = DateTimeFormat.forPattern("E, dd MMM yyyy");
            String monDayStr = fomatter.print(startDate);
            String sundayStr = fomatter.print(maxValue);
            headerLbl.setValue(String.format("%s - %s", monDayStr, sundayStr));
        }

        GroupComponent() {
            initComponent();
            headerLbl.setValue("Unscheduled");
        }

        private void initComponent() {
            this.setMargin(new MarginInfo(true, false, true, false));
            wrapBody = new CssLayout();
            wrapBody.setStyleName("tasklist");
            headerLbl = new Label();
            headerLbl.addStyleName("h2");
            this.addComponent(headerLbl);
            this.addComponent(wrapBody);
        }

        void insertTask(SimpleTask task) {
            wrapBody.addComponent(new TaskRowRenderer(task));
        }
    }
}
