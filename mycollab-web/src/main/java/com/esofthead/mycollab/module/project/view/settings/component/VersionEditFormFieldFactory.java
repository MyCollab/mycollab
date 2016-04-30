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

import com.esofthead.mycollab.module.project.i18n.VersionI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupEditFieldFactory;
import com.esofthead.mycollab.vaadin.ui.DateFieldExt;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
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
        if (Version.Field.versionname.equalTo(propertyId)) {
            final TextField tf = new TextField();
            if (isValidateForm) {
                tf.setNullRepresentation("");
                tf.setRequired(true);
                tf.setRequiredError(AppContext.getMessage(VersionI18nEnum.FORM_VERSION_ERROR_MSG));
            }
            return tf;
        } else if (Version.Field.description.equalTo(propertyId)) {
            return new RichTextArea();
        } else if (Version.Field.duedate.equalTo(propertyId)) {
            final DateFieldExt dateField = new DateFieldExt();
            dateField.setResolution(Resolution.DAY);
            return dateField;
        }

        return null;
    }
}
