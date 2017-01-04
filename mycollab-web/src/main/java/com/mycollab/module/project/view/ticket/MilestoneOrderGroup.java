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
package com.mycollab.module.project.view.ticket;

import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.Text;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.SortedArrayMap;
import com.mycollab.html.DivLessFormatter;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.vaadin.UserUIContext;

import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.4.6
 */
class MilestoneOrderGroup extends TicketGroupOrderComponent {
    private SortedArrayMap<Integer, MilestoneTicketGroupComponent> milestonesAvailable = new SortedArrayMap<>();
    private MilestoneTicketGroupComponent unspecifiedTickets;

    @Override
    public void insertTickets(List<ProjectTicket> tickets) {
        for (ProjectTicket ticket : tickets) {
            Integer milestoneId = ticket.getMilestoneId();
            if (milestoneId != null) {
                if (milestonesAvailable.containsKey(milestoneId)) {
                    MilestoneTicketGroupComponent groupComponent = milestonesAvailable.get(milestoneId);
                    groupComponent.insertTicket(ticket);
                } else {
                    Div milestoneDiv = new DivLessFormatter().appendChild(new Text(" " + ticket.getMilestoneName()));

                    MilestoneTicketGroupComponent groupComponent = new MilestoneTicketGroupComponent(milestoneDiv.write(), milestoneId);
                    milestonesAvailable.put(milestoneId, groupComponent);
                    int index = milestonesAvailable.getKeyIndex(milestoneId);
                    if (index > -1) {
                        addComponent(groupComponent, index);
                    } else {
                        if (unspecifiedTickets != null) {
                            addComponent(groupComponent, getComponentCount() - 1);
                        } else {
                            addComponent(groupComponent);
                        }
                    }

                    groupComponent.insertTicket(ticket);
                }
            } else {
                if (unspecifiedTickets == null) {
                    unspecifiedTickets = new MilestoneTicketGroupComponent(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED), null);
                    addComponent(unspecifiedTickets);
                }
                unspecifiedTickets.insertTicket(ticket);
            }
        }
    }
}
