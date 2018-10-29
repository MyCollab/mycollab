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
package com.mycollab.module.crm.view.activity;

import com.mycollab.module.crm.domain.MeetingWithBLOBs;
import com.mycollab.module.crm.domain.SimpleMeeting;
import com.mycollab.module.crm.i18n.OptionI18nEnum.CallStatus;
import com.mycollab.module.crm.ui.components.RelatedReadItemField;
import com.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.mycollab.vaadin.ui.GenericBeanForm;
import com.mycollab.vaadin.ui.UIConstants;
import com.mycollab.vaadin.ui.field.DateTimeViewField;
import com.mycollab.vaadin.ui.field.I18nFormViewField;
import com.mycollab.vaadin.ui.field.RichTextViewField;
import com.vaadin.data.HasValue;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
class MeetingReadFormFieldFactory extends AbstractBeanFieldGroupViewFieldFactory<SimpleMeeting> {
    private static final long serialVersionUID = 1L;

    MeetingReadFormFieldFactory(GenericBeanForm<SimpleMeeting> form) {
        super(form);
    }

    @Override
    protected HasValue<?> onCreateField(Object propertyId) {
        SimpleMeeting meeting = attachForm.getBean();

        if (propertyId.equals("type")) {
            return new RelatedReadItemField(meeting);
        } else if (propertyId.equals("startdate")) {
            return new DateTimeViewField(meeting.getStartdate());
        } else if (propertyId.equals("enddate")) {
            return new DateTimeViewField(meeting.getEnddate());
        } else if (propertyId.equals("isrecurrence")) {
            return null;
        } else if (propertyId.equals("description")) {
            return new RichTextViewField(meeting.getDescription());
        } else if (MeetingWithBLOBs.Field.status.equalTo(propertyId)) {
            return new I18nFormViewField(meeting.getStatus(), CallStatus.class).withStyleName(UIConstants.FIELD_NOTE);
        }
        return null;
    }
}
