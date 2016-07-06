/**
 * This file is part of mycollab-services.
 *
 * mycollab-services is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-services is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-services.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.domain;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class TaskPredecessor extends Predecessor {
    public static final String SS = "SS";
    public static final String FS = "FS";
    public static final String FF = "FF";
    public static final String SF = "SF";

    private Integer ganttIndex;

    public Integer getGanttIndex() {
        return ganttIndex;
    }

    public void setGanttIndex(Integer ganttIndex) {
        this.ganttIndex = ganttIndex;
    }
}
