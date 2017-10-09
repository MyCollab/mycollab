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
                    MilestoneTicketGroupComponent groupComponent = new MilestoneTicketGroupComponent(milestoneId);
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
                    unspecifiedTickets = new MilestoneTicketGroupComponent(null);
                    addComponent(unspecifiedTickets);
                }
                unspecifiedTickets.insertTicket(ticket);
            }
        }
    }
}
