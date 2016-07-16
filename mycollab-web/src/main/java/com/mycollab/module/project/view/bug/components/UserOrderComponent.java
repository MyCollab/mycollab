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

import com.mycollab.configuration.StorageFactory;
import com.mycollab.core.utils.SortedArrayMap;
import com.mycollab.module.project.ui.components.IGroupComponent;
import com.mycollab.module.tracker.domain.SimpleBug;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.UIConstants;
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
public class UserOrderComponent extends BugGroupOrderComponent {
    private SortedArrayMap<String, GroupComponent> userAvailables = new SortedArrayMap<>();
    private GroupComponent unspecifiedBugs;

    @Override
    public void insertBugs(List<SimpleBug> bugs) {
        for (SimpleBug bug : bugs) {
            String assignUser = bug.getAssignuser();
            if (assignUser != null) {
                if (userAvailables.containsKey(assignUser)) {
                    GroupComponent groupComponent = userAvailables.get(assignUser);
                    groupComponent.insertBug(bug);
                } else {
                    GroupComponent groupComponent = new GroupComponent(bug);
                    userAvailables.put(assignUser, groupComponent);
                    int index = userAvailables.getKeyIndex(assignUser);
                    if (index > -1) {
                        addComponent(groupComponent, index);
                    } else {
                        addComponent(groupComponent);
                    }

                    groupComponent.insertBug(bug);
                }
            } else {
                if (unspecifiedBugs == null) {
                    unspecifiedBugs = new GroupComponent();
                    addComponent(unspecifiedBugs, 0);
                }
                unspecifiedBugs.insertBug(bug);
            }
        }
    }

    private static class GroupComponent extends VerticalLayout implements IGroupComponent {
        private CssLayout wrapBody;
        private Label headerLbl;

        GroupComponent(SimpleBug bug) {
            initComponent();
            Img img = new Img("", StorageFactory.getAvatarPath(bug.getAssignUserAvatarId(), 32));
            Div userDiv = new Div().appendChild(img, new Text(" " + bug.getAssignuserFullName()));
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

        void insertBug(SimpleBug bug) {
            wrapBody.addComponent(new BugRowComponent(bug));
        }
    }
}
