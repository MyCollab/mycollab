/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.mobile.module.project.view.ticket;

import com.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.vaadin.mvp.ViewComponent;

/**
 * @author MyCollab Ltd
 * @since 5.4.3
 */
@ViewComponent
public class TicketListViewImpl extends AbstractListPageView<ProjectTicketSearchCriteria, ProjectTicket> implements TicketListView {
    @Override
    protected AbstractPagedBeanList<ProjectTicketSearchCriteria, ProjectTicket> createBeanList() {
        return null;
    }

    @Override
    protected SearchInputField<ProjectTicketSearchCriteria> createSearchField() {
        return null;
    }
}
