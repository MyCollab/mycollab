/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings.component;

import com.mycollab.module.tracker.domain.Component;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.vaadin.data.HasValue;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
// TODO
public class ComponentEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Component> {
    private static final long serialVersionUID = 1L;

    public ComponentEditFormFieldFactory(GenericBeanForm<Component> form) {
        super(form);
    }

    @Override
    protected HasValue<?> onCreateField(final Object propertyId) {
        if (Component.Field.name.equalTo(propertyId)) {
            final TextField tf = new TextField();
//            if (isValidateForm) {
//                tf.setNullRepresentation("");
//                tf.setRequired(true);
//                tf.setRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL,
//                        UserUIContext.getMessage(GenericI18Enum.FORM_NAME)));
//            }
            return tf;
        } else if (Component.Field.description.equalTo(propertyId)) {
            return new RichTextArea();
        } else if (Component.Field.userlead.equalTo(propertyId)) {
            return new ProjectMemberSelectionField();
        }

        return null;
    }
}
