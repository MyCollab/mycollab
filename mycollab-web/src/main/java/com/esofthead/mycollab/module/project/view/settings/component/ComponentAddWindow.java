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
package com.esofthead.mycollab.module.project.view.settings.component;

import com.esofthead.mycollab.common.i18n.OptionI18nEnum;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.ComponentI18nEnum;
import com.esofthead.mycollab.module.project.view.settings.ComponentDefaultFormLayoutFactory;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.service.ComponentService;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.IEditFormHandler;
import com.esofthead.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.esofthead.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Window;
import org.vaadin.viritin.layouts.MVerticalLayout;

import static com.esofthead.mycollab.vaadin.web.ui.utils.FormControlsGenerator.generateEditFormControls;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
public class ComponentAddWindow extends Window implements IEditFormHandler<Component> {
    public ComponentAddWindow() {
        super(AppContext.getMessage(ComponentI18nEnum.NEW));
        this.setWidth("750px");
        this.setResizable(false);
        this.setModal(true);
        AdvancedEditBeanForm<Component> editForm = new AdvancedEditBeanForm<>();
        editForm.addFormHandler(this);
        editForm.setFormLayoutFactory(new DefaultDynaFormLayout(ProjectTypeConstants.BUG_COMPONENT,
                ComponentDefaultFormLayoutFactory.getForm(), "id"));
        editForm.setBeanFormFieldFactory(new ComponentEditFormFieldFactory(editForm));
        Component component = new Component();
        component.setProjectid(CurrentProjectVariables.getProjectId());
        component.setSaccountid(AppContext.getAccountId());
        component.setStatus(OptionI18nEnum.StatusI18nEnum.Open.name());
        editForm.setBean(component);
        ComponentContainer buttonControls = generateEditFormControls(editForm, true, false, true);
        this.setContent(new MVerticalLayout(editForm, buttonControls).withAlign(buttonControls, Alignment.TOP_RIGHT));
        this.center();
    }

    @Override
    public void onSave(Component bean) {
        ComponentService componentService = AppContextUtil.getSpringBean(ComponentService.class);
        componentService.saveWithSession(bean, AppContext.getUsername());
        close();
    }

    @Override
    public void onSaveAndNew(Component bean) {

    }

    @Override
    public void onCancel() {
        close();
    }
}
