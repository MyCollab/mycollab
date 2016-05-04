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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.module.project.domain.FollowingTicket;
import com.esofthead.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.web.ui.InitializingView;
import com.esofthead.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public interface FollowingTicketView extends InitializingView {
    void displayTickets();

    HasSearchHandlers<FollowingTicketSearchCriteria> getSearchHandlers();

    AbstractPagedBeanTable<FollowingTicketSearchCriteria, FollowingTicket> getPagedBeanTable();
}
