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
package com.esofthead.mycollab.module.project.view.task.gantt;

import org.tltv.gantt.client.shared.Step;

import java.util.Date;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 5.0.8
 */
public abstract class GanttItemWrapper {
    Date minDate, maxDate;
    GanttItemWrapper parent;
    Step ownStep;
    List<GanttItemWrapper> subItems;

    GanttItemWrapper(Date minDate, Date maxDate) {
        this.minDate = minDate;
        this.maxDate = maxDate;
    }

    abstract public String getName();

    abstract public List<GanttItemWrapper> subTasks();

    abstract Date getStartDate();

    abstract Date getEndDate();

    abstract String buildCaption();

    abstract String buildTooltip();

    public Step getStep() {
        return ownStep;
    }

    StepExt generateStep() {
        Date startDate = this.getStartDate();
        if (startDate.before(minDate)) {
            startDate = minDate;
        }
        Date endDate = this.getEndDate();
        if (endDate.after(maxDate)) {
            endDate = maxDate;
        }
        StepExt step = new StepExt();
        step.setCaption(buildCaption());
        step.setCaptionMode(Step.CaptionMode.HTML);
        step.setDescription(buildTooltip());
        step.setStartDate(startDate);
        step.setEndDate(endDate);
        step.setGanttItemWrapper(this);
        return step;
    }

    public GanttItemWrapper getParent() {
        return parent;
    }

    public void setParent(GanttItemWrapper parent) {
        this.parent = parent;
    }
}
