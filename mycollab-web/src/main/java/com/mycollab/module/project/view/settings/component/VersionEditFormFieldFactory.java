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

import com.mycollab.common.i18n.ErrorI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.PopupDateFieldExt;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Field;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;

/**
 * @author MyCollab Ltd
 * @since 5.3.0
 */
public class VersionEditFormFieldFactory extends AbstractBeanFieldGroupEditFieldFactory<Version> {
    private static final long serialVersionUID = 1L;

    public VersionEditFormFieldFactory(GenericBeanForm<Version> form) {
        super(form);
    }

    @Override
    protected Field<?> onCreateField(final Object propertyId) {
        if (Version.Field.name.equalTo(propertyId)) {
            final TextField tf = new TextField();
            if (isValidateForm) {
                tf.setNullRepresentation("");
                tf.setRequired(true);
                tf.setRequiredError(UserUIContext.getMessage(ErrorI18nEnum.FIELD_MUST_NOT_NULL, UserUIContext
                        .getMessage(GenericI18Enum.FORM_NAME)));
            }
            return tf;
        } else if (Version.Field.description.equalTo(propertyId)) {
            return new RichTextArea();
        } else if (Version.Field.duedate.equalTo(propertyId)) {
            final PopupDateFieldExt dateField = new PopupDateFieldExt();
            dateField.setResolution(Resolution.DAY);
            return dateField;
        }

        return null;
    }
}
