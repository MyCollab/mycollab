/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.ticket;

import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Img;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.SortedArrayMap;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.file.StorageUtils;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.UIConstants;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.2.9
 */
@ViewComponent
public class UserOrderComponent extends TicketGroupOrderComponent {
    private SortedArrayMap<String, DefaultTicketGroupComponent> userAvailables = new SortedArrayMap<>();
    private DefaultTicketGroupComponent unspecifiedTasks;

    @Override
    public void insertTickets(List<ProjectTicket> tickets) {
        for (ProjectTicket ticket : tickets) {
            String assignUser = ticket.getAssignUser();
            if (assignUser != null) {
                if (userAvailables.containsKey(assignUser)) {
                    DefaultTicketGroupComponent groupComponent = userAvailables.get(assignUser);
                    groupComponent.insertTicket(ticket);
                } else {
                    Img img = new Img("", StorageUtils.getAvatarPath(ticket.getAssignUserAvatarId(), 32))
                            .setCSSClass((UIConstants.CIRCLE_BOX));
                    Div userDiv = new DivLessFormatter().appendChild(img, new Text(" " + ticket.getAssignUserFullName()));

                    DefaultTicketGroupComponent groupComponent = new DefaultTicketGroupComponent(userDiv.write());
                    userAvailables.put(assignUser, groupComponent);
                    int index = userAvailables.getKeyIndex(assignUser);
                    if (index > -1) {
                        addComponent(groupComponent, index);
                    } else {
                        addComponent(groupComponent);
                    }

                    groupComponent.insertTicket(ticket);
                }
            } else {
                if (unspecifiedTasks == null) {
                    unspecifiedTasks = new DefaultTicketGroupComponent(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED));
                    addComponent(unspecifiedTasks, 0);
                }
                unspecifiedTasks.insertTicket(ticket);
            }
        }
    }
}
