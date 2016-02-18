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
package com.esofthead.mycollab.module.project.view.assignments.gantt;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.module.project.domain.TaskPredecessor;
import com.esofthead.mycollab.module.project.service.GanttAssignmentService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.data.Container;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.3
 */
public class GanttItemContainer extends BeanItemContainer<GanttItemWrapper> implements Container.Hierarchical {
    private List<GanttItemWrapper> rootItems = new ArrayList<>();

    public GanttItemContainer() {
        super(GanttItemWrapper.class);
    }

    public GanttItemWrapper getItemByGanttIndex(int rowIndex) {
        List items = getAllItemIds();
        for (Object item : items) {
            GanttItemWrapper itemWrapper = (GanttItemWrapper) item;
            if (rowIndex == itemWrapper.getGanttIndex()) {
                return itemWrapper;
            }
        }
        return null;
    }

    @Override
    public BeanItem<GanttItemWrapper> addItem(Object itemId) {
        GanttItemWrapper ganttItemWrapper = (GanttItemWrapper) itemId;
        if (ganttItemWrapper.getParent() == null) {
            rootItems.add(ganttItemWrapper);
        }
        BeanItem<GanttItemWrapper> item = super.addItem(ganttItemWrapper);
        return item;
    }

    @Override
    public boolean removeItem(Object itemId) {
        if (itemId instanceof GanttItemWrapper) {
            GanttItemWrapper removedTask = (GanttItemWrapper) itemId;
            removeAssociatesPredecessorsAndDependents(removedTask);
            if (removedTask.getParent() == null) {
                rootItems.remove(removedTask);
            }
            return super.removeItem(itemId);
        } else {
            throw new MyCollabException("Do not support removing type " + itemId);
        }
    }

    private void removeAssociatesPredecessorsAndDependents(GanttItemWrapper removedTask) {
        List items = getAllItemIds();
        for (Object itemId : items) {
            GanttItemWrapper item = (GanttItemWrapper) itemId;
            List<TaskPredecessor> removedPredecessors = new ArrayList<>();

            List<TaskPredecessor> predecessors = item.getPredecessors();
            if (CollectionUtils.isNotEmpty(predecessors)) {
                Iterator<TaskPredecessor> iterator = predecessors.iterator();
                while (iterator.hasNext()) {
                    TaskPredecessor predecessor = iterator.next();
                    if (predecessor.getDescid().intValue() == removedTask.getId().intValue()) {
                        iterator.remove();
                        removedPredecessors.add(predecessor);
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(removedPredecessors)) {
                GanttAssignmentService ganttAssignmentService = ApplicationContextUtil.getSpringBean
                        (GanttAssignmentService.class);
                ganttAssignmentService.massDeletePredecessors(removedPredecessors, AppContext.getAccountId());
            }

            List<TaskPredecessor> dependents = item.getDependents();
            if (CollectionUtils.isNotEmpty(dependents)) {
                Iterator<TaskPredecessor> iterator = predecessors.iterator();
                while (iterator.hasNext()) {
                    TaskPredecessor dependent = iterator.next();
                    if (dependent.getSourceid().intValue() == removedTask.getId().intValue()) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    public boolean hasCircularRelationship(GanttItemWrapper item1, GanttItemWrapper item2) {
        if (item1.isAncestor(item2) || item2.isAncestor(item1)) {
            return true;
        }
        return false;
    }

    @Override
    public Collection<?> getChildren(Object o) {
        GanttItemWrapper ganttItemWrapper = (GanttItemWrapper) o;
        return ganttItemWrapper.subTasks();
    }

    @Override
    public Object getParent(Object o) {
        GanttItemWrapper ganttItemWrapper = (GanttItemWrapper) o;
        return ganttItemWrapper.getParent();
    }

    @Override
    public Collection<?> rootItemIds() {
        return rootItems;
    }

    @Override
    public boolean setParent(Object child, Object parent) throws UnsupportedOperationException {
        return true;
    }

    @Override
    public boolean areChildrenAllowed(Object o) {
        GanttItemWrapper ganttItemWrapper = (GanttItemWrapper) o;
        return ganttItemWrapper.hasSubTasks();
    }

    @Override
    public boolean setChildrenAllowed(Object o, boolean b) throws UnsupportedOperationException {
        return b;
    }

    @Override
    public boolean isRoot(Object o) {
        GanttItemWrapper ganttItemWrapper = (GanttItemWrapper) o;
        return ganttItemWrapper.getParent() == null;
    }

    @Override
    public boolean hasChildren(Object o) {
        GanttItemWrapper ganttItemWrapper = (GanttItemWrapper) o;
        return ganttItemWrapper.hasSubTasks();
    }
}
