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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.module.crm.domain.SimpleCall;
import com.esofthead.mycollab.module.crm.ui.components.RelatedReadItemField;
import com.esofthead.mycollab.vaadin.ui.AbstractBeanFieldGroupViewFieldFactory;
import com.esofthead.mycollab.vaadin.ui.GenericBeanForm;
import com.esofthead.mycollab.vaadin.ui.form.field.DateTimeViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.DefaultViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.RichTextViewField;
import com.esofthead.mycollab.vaadin.ui.form.field.UserLinkViewField;
import com.vaadin.ui.Field;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
class CallReadFormFieldFactory extends
		AbstractBeanFieldGroupViewFieldFactory<SimpleCall> {
	private static final long serialVersionUID = 1L;

	public CallReadFormFieldFactory(GenericBeanForm<SimpleCall> form) {
		super(form);
	}

	@Override
	protected Field<?> onCreateField(Object propertyId) {
		SimpleCall call = attachForm.getBean();

		if (propertyId.equals("assignuser")) {
			return new UserLinkViewField(call.getAssignuser(),
					call.getAssignUserAvatarId(), call.getAssignUserFullName());
		} else if (propertyId.equals("type")) {
			return new RelatedReadItemField(call);
		} else if (propertyId.equals("status")) {
			final String value = call.getStatus() + " " + call.getCalltype();
			final DefaultViewField field = new DefaultViewField(value);
			return field;
		} else if (propertyId.equals("durationinseconds")) {
			final Integer duration = call.getDurationinseconds();
			if (duration != null) {
				final int hours = duration / 3600;
				final int minutes = (duration % 3600) / 60;
				final StringBuffer value = new StringBuffer();
				if (hours == 1) {
					value.append("1 hour ");
				} else if (hours >= 2) {
					value.append(hours + " hours ");
				}

				if (minutes > 0) {
					value.append(minutes + " minutes");
				}

				return new DefaultViewField(value.toString());
			} else {
				return new DefaultViewField("");
			}
		} else if (propertyId.equals("startdate")) {
			return new DateTimeViewField(call.getStartdate());
		} else if (propertyId.equals("description")) {
			return new RichTextViewField(call.getDescription());
		}

		return null;
	}

}
