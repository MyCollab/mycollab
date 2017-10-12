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
package com.mycollab.community.module.project.view;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.vaadin.EventBusFactory;
import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.event.ProjectEvent;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.view.AbstractProjectAddWindow;
import com.mycollab.module.project.view.ProjectGeneralInfoStep;
import com.mycollab.module.project.view.parameters.ProjectScreenData;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.mvp.PageActionChain;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.3.5
 */
@ViewComponent
public class ProjectAddWindow extends AbstractProjectAddWindow {

    private ProjectGeneralInfoStep projectInfo;

    public ProjectAddWindow() {
        super(new Project());

        MVerticalLayout contentLayout = new MVerticalLayout().withSpacing(false).withMargin(new MarginInfo(false, false, true, false));
        setContent(contentLayout);
        projectInfo = new ProjectGeneralInfoStep(project);
        contentLayout.addComponent(projectInfo.getContent());

        MButton saveBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SAVE), clickEvent -> {
            boolean isValid = projectInfo.commit();
            if(isValid) {
                ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
                project.setSaccountid(AppUI.getAccountId());
                projectService.saveWithSession(project, UserUIContext.getUsername());

                EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this,
                        new PageActionChain(new ProjectScreenData.Goto(project.getId()))));
                close();
            }
        }).withIcon(FontAwesome.SAVE).withStyleName(WebThemes.BUTTON_ACTION);

        MButton cancelBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebThemes.BUTTON_OPTION);
        MHorizontalLayout buttonControls = new MHorizontalLayout(cancelBtn, saveBtn).withMargin(new MarginInfo(true,
                true, false, false));
        contentLayout.with(buttonControls).withAlign(buttonControls, Alignment.MIDDLE_RIGHT);
    }
}
