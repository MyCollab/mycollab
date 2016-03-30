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
package com.esofthead.mycollab.module.project.view.task.components;

import com.esofthead.mycollab.configuration.StorageFactory;
import com.esofthead.mycollab.core.utils.SortedArrayMap;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.ui.components.IGroupComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.9
 */
@ViewComponent
public class UserOrderComponent extends TaskGroupOrderComponent {
    private SortedArrayMap<String, GroupComponent> userAvailables = new SortedArrayMap<>();
    private GroupComponent unspecifiedTasks;

    @Override
    public void insertTasks(List<SimpleTask> tasks) {
        for (SimpleTask task : tasks) {
            String assignUser = task.getAssignuser();
            if (assignUser != null) {
                if (userAvailables.containsKey(assignUser)) {
                    GroupComponent groupComponent = userAvailables.get(assignUser);
                    groupComponent.insertTask(task);
                } else {
                    GroupComponent groupComponent = new GroupComponent(task);
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
                    unspecifiedTasks = new GroupComponent();
                    addComponent(unspecifiedTasks, 0);
                }
                unspecifiedTasks.insertTask(task);
            }
        }
    }

    private static class GroupComponent extends VerticalLayout implements IGroupComponent {
        private CssLayout wrapBody;
        private Label headerLbl;

        GroupComponent(SimpleTask task) {
            initComponent();
            Img img = new Img("", StorageFactory.getInstance().getAvatarPath(task.getAssignUserAvatarId(), 32));
            Div userDiv = new Div().appendChild(img, new Text(" " + task.getAssignUserFullName()));
            headerLbl.setValue(userDiv.write());
        }

        GroupComponent() {
            initComponent();
            headerLbl.setValue("Unassigned");
        }

        private void initComponent() {
            this.setMargin(new MarginInfo(true, false, true, false));
            wrapBody = new CssLayout();
            wrapBody.setStyleName(UIConstants.BORDER_LIST);
            headerLbl = ELabel.h3("");
            this.addComponent(headerLbl);
            this.addComponent(wrapBody);
        }

        void insertTask(SimpleTask task) {
            wrapBody.addComponent(new TaskRowRenderer(task));
        }
    }
}
