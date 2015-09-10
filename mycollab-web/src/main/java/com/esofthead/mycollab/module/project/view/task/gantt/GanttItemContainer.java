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

import com.vaadin.data.util.BeanItemContainer;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class GanttItemContainer extends BeanItemContainer<GanttItemWrapper> {

    public GanttItemContainer() {
        super(GanttItemWrapper.class);
    }

    public GanttItemWrapper getItemByGanttIndex(int rowIndex) {
        List<GanttItemWrapper> items = getItemIds();
        for (GanttItemWrapper item : items) {
            if (rowIndex == item.getGanttIndex()) {
                return item;
            }
        }
        return null;
    }
}
