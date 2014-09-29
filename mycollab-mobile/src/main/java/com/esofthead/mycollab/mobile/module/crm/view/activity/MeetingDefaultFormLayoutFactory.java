/**
 * This file is part of mycollab-mobile.
 *
 * mycollab-mobile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-mobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-mobile.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.mobile.module.crm.view.activity;

import com.esofthead.mycollab.form.view.builder.DateTimeDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.DynaSectionBuilder;
import com.esofthead.mycollab.form.view.builder.TextAreaDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;

/**
 * 
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class MeetingDefaultFormLayoutFactory {
	private static final DynaForm defaultForm;

	static {
		defaultForm = new DynaForm();

		DynaSection meetingSection = new DynaSectionBuilder()
				.layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
				.header("Meeting Information").build();

		meetingSection.addField(new TextDynaFieldBuilder().fieldName("subject")
				.displayName("Subject").mandatory(true).fieldIndex(0).build());

		meetingSection.addField(new TextDynaFieldBuilder().fieldName("status")
				.displayName("Status").fieldIndex(1).build());

		meetingSection.addField(new DateTimeDynaFieldBuilder()
				.fieldName("startdate").displayName("Start Date & Time")
				.fieldIndex(2).build());

		meetingSection.addField(new TextDynaFieldBuilder().fieldName("typeid")
				.displayName("Related To").fieldIndex(3).build());

		meetingSection.addField(new DateTimeDynaFieldBuilder()
				.fieldName("enddate").displayName("End Date & Time")
				.fieldIndex(4).build());

		meetingSection.addField(new TextDynaFieldBuilder()
				.fieldName("location").displayName("Location").fieldIndex(5)
				.build());
		// meetingSection.addField(new CheckBoxDynaFieldBuilder()
		// .fieldName("isrecurrence").displayName("Recurring Activity")
		// .fieldIndex(6).build());

		defaultForm.addSection(meetingSection);

		DynaSection descSection = new DynaSectionBuilder()
				.layoutType(LayoutType.ONE_COLUMN).orderIndex(1)
				.header("Description").build();
		descSection.addField(new TextAreaDynaFieldBuilder()
				.fieldName("description").displayName("Description")
				.fieldIndex(0).build());

		defaultForm.addSection(descSection);
	}

	public static DynaForm getForm() {
		return defaultForm;
	}
}
