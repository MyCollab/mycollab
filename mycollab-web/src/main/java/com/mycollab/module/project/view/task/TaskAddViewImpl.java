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
package com.mycollab.module.project.view.task;

import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.SimpleTask;
import com.mycollab.module.project.domain.Task;
import com.mycollab.module.project.i18n.TaskI18nEnum;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasEditFormHandlers;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.AdvancedEditBeanForm;
import com.mycollab.vaadin.ui.IFormLayoutFactory;
import com.mycollab.vaadin.web.ui.DefaultDynaFormLayout;
import com.mycollab.vaadin.web.ui.field.AttachmentUploadField;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;

import java.util.List;

import static com.mycollab.vaadin.web.ui.utils.FormControlsGenerator.generateEditFormControls;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class TaskAddViewImpl extends AbstractEditItemComp<SimpleTask> implements TaskAddView {
    private static final long serialVersionUID = 1L;

    private TaskEditFormFieldFactory editFormFieldFactory;

    @Override
    public AttachmentUploadField getAttachUploadField() {
        return ((TaskEditFormFieldFactory) editForm.getFieldFactory()).getAttachmentUploadField();
    }

    @Override
    public HasEditFormHandlers<SimpleTask> getEditFormHandlers() {
        return this.editForm;
    }

    @Override
    protected String initFormHeader() {
        return (beanItem.getId() == null) ? UserUIContext.getMessage(TaskI18nEnum.NEW) : UserUIContext.getMessage(TaskI18nEnum.DETAIL);
    }

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? null : beanItem.getName();
    }

    @Override
    protected Resource initFormIconResource() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return generateEditFormControls(editForm);
    }

    @Override
    protected AdvancedEditBeanForm<SimpleTask> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        if (beanItem.getId() == null) {
            return new DefaultDynaFormLayout(ProjectTypeConstants.TASK, TaskDefaultFormLayoutFactory.getForm(),
                    Task.Field.parenttaskid.name());
        } else {
            return new DefaultDynaFormLayout(ProjectTypeConstants.TASK, TaskDefaultFormLayoutFactory.getForm(),
                    Task.Field.parenttaskid.name(), "selected");
        }
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<SimpleTask> initBeanFormFieldFactory() {
        editFormFieldFactory = new TaskEditFormFieldFactory(editForm, CurrentProjectVariables.getProjectId());
        return editFormFieldFactory;
    }

    @Override
    public List<String> getFollowers() {
        return editFormFieldFactory.getSubscribersComp().getFollowers();
    }
}
