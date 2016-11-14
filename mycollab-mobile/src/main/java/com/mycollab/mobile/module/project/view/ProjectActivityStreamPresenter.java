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
package com.mycollab.mobile.module.project.view;

import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.ProjectActivityStream;
import com.mycollab.vaadin.mvp.ScreenData;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.vaadin.ui.HasComponents;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
public class ProjectActivityStreamPresenter extends ProjectListPresenter<ProjectActivityView, ActivityStreamSearchCriteria, ProjectActivityStream> {
    private static final long serialVersionUID = -2089284900326846089L;

    public ProjectActivityStreamPresenter() {
        super(ProjectActivityView.class);
    }

    @Override
    protected void onGo(HasComponents navigator, ScreenData<?> data) {
        if (CurrentProjectVariables.canRead(ProjectRolePermissionCollections.PROJECT)) {
            super.onGo(navigator, data);
        } else {
            NotificationUtil.showMessagePermissionAlert();
        }
    }

}
