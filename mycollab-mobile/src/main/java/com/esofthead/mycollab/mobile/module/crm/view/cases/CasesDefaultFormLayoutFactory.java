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
package com.esofthead.mycollab.mobile.module.crm.view.cases;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.form.view.builder.DynaSectionBuilder;
import com.esofthead.mycollab.form.view.builder.EmailDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.PhoneDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.TextAreaDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class CasesDefaultFormLayoutFactory {
	public static final DynaForm defaultForm;

	static {
		defaultForm = new DynaForm();

		DynaSection infoSection = new DynaSectionBuilder()
				.layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
				.header("Case Information").build();

		infoSection.addField(new TextDynaFieldBuilder().fieldName("priority")
				.displayName("Priority").fieldIndex(0).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("type")
				.displayName("Type").fieldIndex(1).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("status")
				.displayName("Status").fieldIndex(2).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("reason")
				.displayName("Reason").fieldIndex(3).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("accountid")
				.displayName("Account Name").fieldIndex(4).mandatory(true)
				.build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("subject")
				.displayName("Subject").fieldIndex(5).mandatory(true).build());

		infoSection.addField(new PhoneDynaFieldBuilder()
				.fieldName("phonenumber").displayName("Phone Number")
				.fieldIndex(6).build());

		infoSection.addField(new EmailDynaFieldBuilder().fieldName("email")
				.displayName("Email").fieldIndex(7).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("origin")
				.displayName("Origin").fieldIndex(8).build());

		infoSection
				.addField(new TextDynaFieldBuilder()
						.fieldName("assignuser")
						.displayName(
								AppContext
										.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD))
						.fieldIndex(9).build());

		defaultForm.addSection(infoSection);

		DynaSection descSection = new DynaSectionBuilder()
				.layoutType(LayoutType.ONE_COLUMN).orderIndex(1)
				.header("Description").build();

		descSection.addField(new TextAreaDynaFieldBuilder()
				.fieldName("description").displayName("Description")
				.fieldIndex(0).build());
		descSection.addField(new TextAreaDynaFieldBuilder()
				.fieldName("resolution").displayName("Resolution")
				.fieldIndex(1).build());

		defaultForm.addSection(descSection);
	}

	public static DynaForm getForm() {
		return defaultForm;
	}
}
