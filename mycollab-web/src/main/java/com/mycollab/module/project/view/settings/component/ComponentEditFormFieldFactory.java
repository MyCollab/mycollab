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
package com.mycollab.module.project.view.settings.component;

import com.mycollab.module.project.i18n.ComponentI18nEnum;
import com.mycollab.module.tracker.domain.Component;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
public class ComponentEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Component> {
    private static final long serialVersionUID = 1L;

    public ComponentEditFormFieldFactory(GenericBeanForm<Component> form) {
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
