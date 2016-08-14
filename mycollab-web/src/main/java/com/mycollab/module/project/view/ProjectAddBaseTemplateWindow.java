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

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.BasicSearchRequest;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.eventmanager.EventBusFactory;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.project.events.ProjectEvent;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.service.ProjectTemplateService;
import com.mycollab.module.project.view.parameters.ProjectScreenData;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.mvp.PageActionChain;
import com.mycollab.vaadin.ui.NotificationUtil;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.grid.GridFormLayoutHelper;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.List;

import static com.mycollab.common.i18n.GenericI18Enum.FORM_NAME;
import static com.mycollab.module.project.i18n.ProjectI18nEnum.*;

/**
 * @author MyCollab Ltd
 * @since 5.2.8
 */
public class ProjectAddBaseTemplateWindow extends Window {
    public ProjectAddBaseTemplateWindow() {
        super(AppContext.getMessage(OPT_CREATE_PROJECT_FROM_TEMPLATE));
        this.setModal(true);
        this.setClosable(true);
        this.setResizable(false);
        this.setWidth("550px");
        MVerticalLayout content = new MVerticalLayout();
        GridFormLayoutHelper gridFormLayoutHelper = GridFormLayoutHelper.defaultFormLayoutHelper(1, 3);
        final TemplateProjectComboBox templateProjectComboBox = new TemplateProjectComboBox();
        gridFormLayoutHelper.addComponent(templateProjectComboBox, AppContext.getMessage(FORM_TEMPLATE),
                AppContext.getMessage(OPT_MARK_TEMPLATE_HELP), 0, 0);
        final TextField prjNameField = new TextField();
        gridFormLayoutHelper.addComponent(prjNameField, AppContext.getMessage(FORM_NAME), 0, 1);
        final TextField prjKeyField = new TextField();
        gridFormLayoutHelper.addComponent(prjKeyField, AppContext.getMessage(FORM_SHORT_NAME), 0, 2);

        MButton okBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_OK), clickEvent -> {
            SimpleProject templatePrj = (SimpleProject) templateProjectComboBox.getValue();
            if (templatePrj == null) {
                NotificationUtil.showErrorNotification(AppContext.getMessage(ERROR_MUST_CHOOSE_TEMPLATE_PROJECT));
                return;
            }
            String newPrjName = prjNameField.getValue();
            if (newPrjName.length() == 0) {
                NotificationUtil.showErrorNotification("Project name must be not null");
                return;
            }
            String newPrjKey = prjKeyField.getValue();
            if (newPrjKey.length() > 3 || newPrjKey.length() == 0) {
                NotificationUtil.showErrorNotification("Project key must be not null and less than 3 characters");
                return;
            }
            ProjectTemplateService projectTemplateService = AppContextUtil.getSpringBean
                    (ProjectTemplateService.class);
            if (projectTemplateService != null) {
                Integer newProjectId = projectTemplateService.cloneProject(templatePrj.getId(), newPrjName, newPrjKey,
                        AppContext.getAccountId(), AppContext.getUsername());
                EventBusFactory.getInstance().post(new ProjectEvent.GotoMyProject(this,
                        new PageActionChain(new ProjectScreenData.Goto(newProjectId))));
                close();
            }
        }).withIcon(FontAwesome.SAVE).withStyleName(WebUIConstants.BUTTON_ACTION);
        MButton cancelBtn = new MButton(AppContext.getMessage(GenericI18Enum.BUTTON_CANCEL), clickEvent -> close())
                .withStyleName(WebUIConstants.BUTTON_OPTION);
        MHorizontalLayout buttonControls = new MHorizontalLayout(cancelBtn, okBtn);
        content.with(gridFormLayoutHelper.getLayout(), buttonControls).withAlign(buttonControls, Alignment.MIDDLE_RIGHT);
        this.setContent(content);
    }

    private static class TemplateProjectComboBox extends ComboBox {
        TemplateProjectComboBox() {
            ProjectService projectService = AppContextUtil.getSpringBean(ProjectService.class);
            ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
            searchCriteria.addExtraField(ProjectSearchCriteria.p_template.buildParamIsEqual(SearchField.AND, 1));
            List<SimpleProject> projectTemplates = projectService.findPageableListByCriteria(new BasicSearchRequest<>(searchCriteria));
            this.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
            for (SimpleProject prjTemplate : projectTemplates) {
                this.addItem(prjTemplate);
                this.setItemCaption(prjTemplate, StringUtils.trim(String.format("[%s] %s", prjTemplate.getShortname(),
                        prjTemplate.getName()), 50, true));
            }
        }
    }
}
