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

import com.mycollab.core.SecureAccessException;
import com.mycollab.db.persistence.service.ISearchableService;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.module.project.view.ProjectBreadcrumb;
import com.mycollab.module.project.view.ProjectGenericListPresenter;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewManager;
import com.mycollab.vaadin.mvp.ViewScope;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class TicketDashboardPresenter extends ProjectGenericListPresenter<TicketDashboardView, ProjectTicketSearchCriteria, ProjectTicket> {
    private static final long serialVersionUID = 1L;

    private ProjectTicketService projectTicketService;

    public TicketDashboardPresenter() {
        super(TicketDashboardView.class);
        projectTicketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
    }

    @Override
    public void doSearch(ProjectTicketSearchCriteria searchCriteria) {
        view.queryTickets(searchCriteria);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        if (CurrentProjectVariables.canReadTicket()) {
            TicketContainer ticketContainer = (TicketContainer) container;
            ticketContainer.setContent(view);
            String query = (data != null && data.getParams() instanceof String) ? (String) data.getParams() : "";
            view.displayView(query);

            ProjectBreadcrumb breadCrumb = ViewManager.getCacheComponent(ProjectBreadcrumb.class);
            breadCrumb.gotoTicketDashboard(query);
        } else {
            throw new SecureAccessException();
        }
    }

    @Override
    public ISearchableService<ProjectTicketSearchCriteria> getSearchService() {
        return projectTicketService;
    }

    @Override
    protected void deleteSelectedItems() {

    }
}
