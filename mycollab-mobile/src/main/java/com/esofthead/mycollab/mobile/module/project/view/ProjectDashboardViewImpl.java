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
package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.ui.AbstractMobileSwipeView;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.4.0
 */

@ViewComponent
public class ProjectDashboardViewImpl extends AbstractMobileSwipeView implements ProjectDashboardView {
    private final Label welcomeTextLbl;
    public ProjectDashboardViewImpl() {
        super();
        this.setCaption(AppContext.getMessage(ProjectCommonI18nEnum.VIEW_DASHBOARD));
        welcomeTextLbl = new Label();
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        this.setContent(mainLayout);
        mainLayout.addComponent(welcomeTextLbl);
    }

    @Override
    public void displayDashboard() {
        SimpleProject currentProject = CurrentProjectVariables.getProject();
        this.welcomeTextLbl.setValue(currentProject.getName());
    }
}
