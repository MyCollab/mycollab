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
package com.mycollab.module.project.view;

import com.mycollab.core.arguments.ValuedBean;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.vaadin.web.ui.IListView;
import com.mycollab.vaadin.web.ui.ListSelectionPresenter;

/**
 * @author MyCollab Ltd.
 * @since 4.1
 */
public abstract class ProjectGenericListPresenter<V extends IListView<S, B>, S extends SearchCriteria, B extends ValuedBean>
        extends ListSelectionPresenter<V, S, B> {
    private static final long serialVersionUID = 7270489652418186012L;

    public ProjectGenericListPresenter(Class<V> viewClass) {
        super(viewClass);
    }

    @Override
    protected void onErrorStopChain(Throwable throwable) {
        super.onErrorStopChain(throwable);
        EventBusFactory.getInstance().post(new ProjectEvent.GotoDashboard(this, null));
    }
}
