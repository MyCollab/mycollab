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
package com.mycollab.module.project.view.settings.component;

import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.i18n.VersionI18nEnum;
import com.mycollab.module.project.view.settings.VersionDefaultFormLayoutFactory;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.service.VersionService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.IEditFormHandler;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import org.vaadin.viritin.layouts.MVerticalLayout;
import org.vaadin.viritin.layouts.MWindow;

import static com.mycollab.vaadin.web.ui.utils.FormControlsGenerator.generateEditFormControls;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
class VersionAddWindow extends MWindow implements IEditFormHandler<Version> {
    VersionAddWindow() {
        super(UserUIContext.getMessage(VersionI18nEnum.NEW));
        AdvancedEditBeanForm<Version> editForm = new AdvancedEditBeanForm<>();
        editForm.addFormHandler(this);
        editForm.setFormLayoutFactory(new DefaultDynaFormLayout(ProjectTypeConstants.BUG_VERSION,
                VersionDefaultFormLayoutFactory.getForm(), "id"));
        editForm.setBeanFormFieldFactory(new VersionEditFormFieldFactory(editForm));
        Version version = new Version();
        version.setProjectid(CurrentProjectVariables.getProjectId());
        version.setSaccountid(AppUI.getAccountId());
        version.setStatus(StatusI18nEnum.Open.name());
        editForm.setBean(version);
        ComponentContainer buttonControls = generateEditFormControls(editForm,
                CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.VERSIONS),
                false, true);
        withWidth("750px").withModal(true).withResizable(false).withContent(new MVerticalLayout(editForm,
                buttonControls).withAlign(buttonControls, Alignment.TOP_RIGHT)).withCenter();
    }

    @Override
    public void onSave(Version bean) {
        VersionService versionService = AppContextUtil.getSpringBean(VersionService.class);
        versionService.saveWithSession(bean, UserUIContext.getUsername());
        close();
    }

    @Override
    public void onSaveAndNew(Version bean) {

    }

    @Override
    public void onCancel() {
        close();
    }
}
