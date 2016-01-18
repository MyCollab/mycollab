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

import com.esofthead.mycollab.common.ModuleNameConstants;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.MobileApplication;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.shell.ModuleHelper;
import com.esofthead.mycollab.mobile.module.project.view.AbstractProjectPresenter;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.ComponentContainer;

/**
 * @author MyCollab Inc.
 * @since 4.3.1
 */
public class ProjectModulePresenter extends AbstractProjectPresenter<ProjectModule> {
    private static final long serialVersionUID = 6940806138148601147L;

    public ProjectModulePresenter() {
        super(ProjectModule.class);
    }

    @Override
    protected void onGo(ComponentContainer navigator, ScreenData<?> data) {
        ModuleHelper.setCurrentModule(view);
        AppContext.getInstance().updateLastModuleVisit(ModuleNameConstants.PRJ);

        String[] params = (String[]) data.getParams();
        if (params == null || params.length == 0) {
            EventBusFactory.getInstance().post(new ProjectEvent.GotoProjectList(this, null));
            AppContext.addFragment("project", "Project");
        } else {
            MobileApplication.rootUrlResolver.getSubResolver("project").handle(params);
        }
    }
}
