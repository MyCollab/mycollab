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
package com.esofthead.mycollab.module.project.view.task.calendar;

import com.esofthead.mycollab.module.project.ProjectTooltipGenerator;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.components.calendar.event.BasicEvent;

import java.util.Date;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
public class GenericTaskEvent extends BasicEvent {
    private SimpleTask assignment;

    public GenericTaskEvent(SimpleTask assignment) {
        this.assignment = assignment;
        this.setCaption(assignment.getTaskname());
        this.setDescription(ProjectTooltipGenerator.generateToolTipTask(AppContext.getUserLocale(), assignment,
                AppContext.getSiteUrl(), AppContext.getUserTimezone()));
        this.setAllDay(true);

        if (AppContext.getUsername().equals(assignment.getAssignuser())) {
            this.setStyleName("owner");
        } else if (assignment.getAssignuser() == null) {
            this.setStyleName("nonowner");
        } else {
            this.setStyleName("otheruser");
        }

        // task has not start and end has both null
        if (assignment.getStartdate() == null) {
            assignment.setStartdate(assignment.getEnddate());
        }
        if (assignment.getEnddate() == null) {
            assignment.setEnddate(assignment.getStartdate());
        }

        this.setStart(assignment.getStartdate());
        this.setEnd(assignment.getEnddate());
    }

    public SimpleTask getAssignment() {
        return assignment;
    }

    @Override
    public void setStart(Date start) {
        super.setStart(start);
        assignment.setStartdate(start);
    }

    @Override
    public void setEnd(Date end) {
        super.setEnd(end);
        assignment.setEnddate(end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericTaskEvent)) return false;

        GenericTaskEvent taskEvent = (GenericTaskEvent) o;
        return (assignment.getId().intValue() == taskEvent.assignment.getId().intValue());

    }

    @Override
    public int hashCode() {
        return assignment.hashCode();
    }
}
