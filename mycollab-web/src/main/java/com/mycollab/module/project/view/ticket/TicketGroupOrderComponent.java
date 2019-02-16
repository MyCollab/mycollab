/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.ticket;

import com.mycollab.core.MyCollabException;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.ui.components.TicketRowRender;
import com.vaadin.ui.CssLayout;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * @author MyCollab Ltd
 * @since 5.1.1
 */
abstract public class TicketGroupOrderComponent extends CssLayout {
    private Class<? extends TicketRowRender> ticketRowRenderCls;

    public TicketGroupOrderComponent() {
        this(EditableTicketRowRenderer.class);
    }

    public TicketGroupOrderComponent(Class<? extends TicketRowRender> ticketRowRenderCls) {
        this.setWidth("100%");
        this.ticketRowRenderCls = ticketRowRenderCls;
    }

    abstract public void insertTickets(List<ProjectTicket> tickets);

    TicketRowRender buildRenderer(ProjectTicket ticket) {
        try {
            Constructor<? extends TicketRowRender> constructor = ticketRowRenderCls.getConstructor(ProjectTicket.class);
            return constructor.newInstance(ticket);
        } catch (Exception e) {
            throw new MyCollabException(e);
        }
    }
}
