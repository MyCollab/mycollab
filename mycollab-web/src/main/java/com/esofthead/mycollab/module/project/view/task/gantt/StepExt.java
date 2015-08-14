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

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.tltv.gantt.client.shared.AbstractStep;
import org.tltv.gantt.client.shared.Step;

/**
 * @author MyCollab Ltd.
 * @since 5.0.8
 */
public class StepExt extends Step {
    private GanttItemWrapper ganttItemWrapper;

    public GanttItemWrapper getGanttItemWrapper() {
        return ganttItemWrapper;
    }

    public void setGanttItemWrapper(GanttItemWrapper ganttItemWrapper) {
        this.ganttItemWrapper = ganttItemWrapper;
    }

    public int hashCode() {
        return new HashCodeBuilder(1, 31).append(getUid()).build();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else {
            if (obj instanceof AbstractStep) {
                AbstractStep other = (AbstractStep) obj;
                if (this.getUid() == null) {
                    if (other.getUid() != null) {
                        return false;
                    }
                } else if (!this.getUid().equals(other.getUid())) {
                    return false;
                }

                return true;
            } else {
                return false;
            }
        }
    }

}
