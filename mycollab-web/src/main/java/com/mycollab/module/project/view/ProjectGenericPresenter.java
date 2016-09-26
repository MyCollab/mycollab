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
package com.mycollab.module.project.view;

import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.UserNotBelongProjectException;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.module.project.view.user.ProjectDashboardPresenter;
import com.mycollab.vaadin.mvp.PageView;
import com.mycollab.vaadin.mvp.PresenterResolver;
import com.mycollab.vaadin.web.ui.AbstractPresenter;

import static com.mycollab.core.utils.ExceptionUtils.getExceptionType;

/**
 * @author MyCollab Ltd
 * @since 5.2.9
 */
public abstract class ProjectGenericPresenter<V extends PageView> extends AbstractPresenter<V> {
    public ProjectGenericPresenter(Class<V> viewClass) {
        super(viewClass);
    }

    @Override
    protected void onErrorStopChain(Throwable throwable) {
        super.onErrorStopChain(throwable);

        if (this instanceof ProjectViewPresenter) {
            if (getExceptionType(throwable, UserNotBelongProjectException.class) != null) {
                EventBusFactory.getInstance().post(new ProjectEvent.GotoUserDashboard(this, null));
            } else {
                EventBusFactory.getInstance().post(new ProjectEvent.GotoDashboard(this, null));
            }
        } else {
            EventBusFactory.getInstance().post(new ProjectEvent.GotoDashboard(this, null));
        }
    }

    @Override
    protected void onDefaultStopChain() {
        ProjectDashboardPresenter presenter = PresenterResolver.getPresenter(ProjectDashboardPresenter.class);
        presenter.go(view, null);
    }
}
