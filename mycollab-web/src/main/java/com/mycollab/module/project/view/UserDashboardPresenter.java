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

import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.view.parameters.ProjectScreenData;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.LoadPolicy;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.mvp.ViewScope;
import com.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@LoadPolicy(scope = ViewScope.PROTOTYPE)
public class UserDashboardPresenter extends AbstractPresenter<UserDashboardView> {
    private static final long serialVersionUID = 1L;

    public UserDashboardPresenter() {
        super(UserDashboardView.class);
    }

    @Override
    protected void onGo(HasComponents container, ScreenData<?> data) {
        ProjectModule prjContainer = (ProjectModule) container;
        prjContainer.setContent(view);
        if (data instanceof ProjectScreenData.GotoList) {
            view.showProjectList();
        } else {
            view.showDashboard();
        }
        AppUI.addFragment("project", UserUIContext.getMessage(ProjectI18nEnum.SINGLE));
    }
}
