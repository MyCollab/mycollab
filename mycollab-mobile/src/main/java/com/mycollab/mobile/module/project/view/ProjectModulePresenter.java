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

import com.mycollab.common.ModuleNameConstants;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.mobile.MobileApplication;
import com.mycollab.mobile.module.project.events.ProjectEvent;
import com.mycollab.mobile.shell.ModuleHelper;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.ScreenData;
import com.vaadin.ui.HasComponents;

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
    protected void onGo(HasComponents navigator, ScreenData<?> data) {
        ModuleHelper.setCurrentModule(view);
        UserUIContext.updateLastModuleVisit(ModuleNameConstants.PRJ);

        String[] params = (String[]) data.getParams();
        if (params == null || params.length == 0) {
            EventBusFactory.getInstance().post(new ProjectEvent.GotoProjectList(this, null));
            MyCollabUI.addFragment("project", UserUIContext.getMessage(ProjectI18nEnum.SINGLE));
        } else {
            MobileApplication.rootUrlResolver.getSubResolver("project").handle(params);
        }
    }
}
