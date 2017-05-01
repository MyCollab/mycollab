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

import com.mycollab.common.GenericLinkUtils;
import com.mycollab.common.domain.criteria.ActivityStreamSearchCriteria;
import com.mycollab.mobile.module.project.ui.AbstractListPageView;
import com.mycollab.mobile.ui.AbstractPagedBeanList;
import com.mycollab.mobile.ui.SearchInputField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.ProjectActivityStream;
import com.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 * @since 4.5.2
 */
@ViewComponent
public class ProjectActivitiesViewImpl extends AbstractListPageView<ActivityStreamSearchCriteria, ProjectActivityStream> implements ProjectActivitiesView {
    private static final long serialVersionUID = 6930154745425180819L;

    public ProjectActivitiesViewImpl() {
        this.setCaption(UserUIContext.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_ACTIVITIES));
    }

    @Override
    protected AbstractPagedBeanList<ActivityStreamSearchCriteria, ProjectActivityStream> createBeanList() {
        return new ProjectActivitiesStreamListDisplay();
    }

    @Override
    protected SearchInputField<ActivityStreamSearchCriteria> createSearchField() {
        return null;
    }

    @Override
    protected Component buildRightComponent() {
        return null;
    }

    @Override
    public void onBecomingVisible() {
        super.onBecomingVisible();
        MyCollabUI.addFragment("project/activities/" + GenericLinkUtils.encodeParam(CurrentProjectVariables.getProjectId()),
                UserUIContext.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_ACTIVITIES));
    }
}
