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

import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.vaadin.web.ui.SavedFilterComboBox;

import static com.mycollab.module.project.query.TicketQueryInfo.*;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class TicketSavedFilterComboBox extends SavedFilterComboBox {

    public TicketSavedFilterComboBox() {
        super(ProjectTypeConstants.TICKET);

        this.addSharedSearchQueryInfo(allTicketsQuery);
        this.addSharedSearchQueryInfo(allOpenTicketsQuery);
        this.addSharedSearchQueryInfo(overdueTicketsQuery);
        this.addSharedSearchQueryInfo(allClosedTicketsQuery);
        this.addSharedSearchQueryInfo(myTicketsQuery);
        this.addSharedSearchQueryInfo(ticketsCreatedByMeQuery);
        this.addSharedSearchQueryInfo(newTicketsThisWeekQuery);
        this.addSharedSearchQueryInfo(updateTicketsThisWeekQuery);
        this.addSharedSearchQueryInfo(newTicketsLastWeekQuery);
        this.addSharedSearchQueryInfo(updateTicketsLastWeekQuery);
    }

    public void setTotalCountNumber(Integer countNumber) {
        componentsText.setReadOnly(false);
        componentsText.setValue(String.format("%s (%d)", selectedQueryName, countNumber));
        componentsText.setReadOnly(true);
    }

    @Override
    protected void doSetValue(String s) {

    }

    @Override
    public String getValue() {
        return null;
    }
}
