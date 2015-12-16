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

package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.i18n.ComponentI18nEnum;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.module.project.ui.components.AbstractEditItemComp;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberSelectionField;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasEditFormHandlers;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.server.Resource;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ComponentAddViewImpl extends AbstractEditItemComp<Component> implements ComponentAddView {
    private static final long serialVersionUID = 1L;

    @Override
    protected String initFormHeader() {
        return (beanItem.getId() == null) ? AppContext
                .getMessage(ComponentI18nEnum.FORM_NEW_TITLE) : AppContext
                .getMessage(ComponentI18nEnum.FORM_EDIT_TITLE);
    }

    @Override
    protected String initFormTitle() {
        return (beanItem.getId() == null) ? null : beanItem.getComponentname();
    }

    @Override
    protected Resource initFormIconResource() {
        return ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG_COMPONENT);
    }

    @Override
    protected ComponentContainer createButtonControls() {
        return (new EditFormControlsGenerator<>(editForm)).createButtonControls();
    }

    @Override
    protected AdvancedEditBeanForm<Component> initPreviewForm() {
        return new AdvancedEditBeanForm<>();
    }

    @Override
    protected IFormLayoutFactory initFormLayoutFactory() {
        return new DynaFormLayout(ProjectTypeConstants.BUG_COMPONENT, ComponentDefaultFormLayoutFactory.getForm(), "id");
    }

    @Override
    protected AbstractBeanFieldGroupEditFieldFactory<Component> initBeanFormFieldFactory() {
        return new EditFormFieldFactory(editForm);
    }

    @Override
    public HasEditFormHandlers<Component> getEditFormHandlers() {
        return this.editForm;
    }

    private static class EditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Component> {
        private static final long serialVersionUID = 1L;

        public EditFormFieldFactory(GenericBeanForm<Component> form) {
            super(form);
        }

        @Override
        protected Field<?> onCreateField(final Object propertyId) {
            if (Component.Field.componentname.equalTo(propertyId)) {
                final TextField tf = new TextField();
                if (isValidateForm) {
                    tf.setNullRepresentation("");
                    tf.setRequired(true);
                    tf.setRequiredError(AppContext.getMessage(ComponentI18nEnum.FORM_COMPONENT_ERROR));
                }
                return tf;
            } else if (Component.Field.description.equalTo(propertyId)) {
                return new RichTextArea();
            } else if (Component.Field.userlead.equalTo(propertyId)) {
                return new ProjectMemberSelectionField();
            }

            return null;
        }
    }

}
