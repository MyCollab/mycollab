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
package com.mycollab.module.project.view.task.components;

import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.SortedArrayMap;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.UIConstants;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.9
 */
@ViewComponent
public class UserOrderComponent extends TaskGroupOrderComponent {
    private SortedArrayMap<String, DefaultTaskGroupComponent> userAvailables = new SortedArrayMap<>();
    private DefaultTaskGroupComponent unspecifiedTasks;

    @Override
    public void insertTasks(List<SimpleTask> tasks) {
        for (SimpleTask task : tasks) {
            String assignUser = task.getAssignuser();
            if (assignUser != null) {
                if (userAvailables.containsKey(assignUser)) {
                    DefaultTaskGroupComponent groupComponent = userAvailables.get(assignUser);
                    groupComponent.insertTask(task);
                } else {
                    Img img = new Img("", StorageFactory.getAvatarPath(task.getAssignUserAvatarId(), 32))
                            .setCSSClass((UIConstants.CIRCLE_BOX));
                    Div userDiv = new DivLessFormatter().appendChild(img, new Text(" " + task.getAssignUserFullName()));

                    DefaultTaskGroupComponent groupComponent = new DefaultTaskGroupComponent(userDiv.write());
                    userAvailables.put(assignUser, groupComponent);
                    int index = userAvailables.getKeyIndex(assignUser);
                    if (index > -1) {
                        addComponent(groupComponent, index);
                    } else {
                        addComponent(groupComponent);
                    }

                    groupComponent.insertTask(task);
                }
            } else {
                if (unspecifiedTasks == null) {
                    unspecifiedTasks = new DefaultTaskGroupComponent(AppContext.getMessage(GenericI18Enum.OPT_UNDEFINED));
                    addComponent(unspecifiedTasks, 0);
                }
                unspecifiedTasks.insertTask(task);
            }
        }
    }
}
