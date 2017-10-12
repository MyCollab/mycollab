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
package com.mycollab.mobile.module.project.view.ticket;

import com.mycollab.mobile.module.project.view.ProjectListPresenter;
import com.mycollab.mobile.module.project.view.parameters.TicketScreenData;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
public class TicketListPresenter extends ProjectListPresenter<TicketListView, ProjectTicketSearchCriteria, ProjectTicket> {
    private static final long serialVersionUID = -2899902106379842031L;

    public TicketListPresenter() {
        super(TicketListView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (data instanceof TicketScreenData.GotoDashboard) {
            ProjectTicketSearchCriteria searchCriteria = (ProjectTicketSearchCriteria) ((TicketScreenData.GotoDashboard) data).getParams();
            searchCriteria.setTypes(CurrentProjectVariables.getRestrictedTicketTypes());
        }
        super.onGo(container, data);
    }
}
