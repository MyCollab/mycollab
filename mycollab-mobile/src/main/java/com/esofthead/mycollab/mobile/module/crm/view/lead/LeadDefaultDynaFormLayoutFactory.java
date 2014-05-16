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
package com.esofthead.mycollab.mobile.module.crm.view.lead;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.form.view.builder.DynaSectionBuilder;
import com.esofthead.mycollab.form.view.builder.EmailDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.IntDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.PhoneDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.TextAreaDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.UrlDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class LeadDefaultDynaFormLayoutFactory {
	public static final DynaForm defaultForm;

	static {
		defaultForm = new DynaForm();

		DynaSection infoSection = new DynaSectionBuilder()
				.layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
				.header("Contact Information").build();

		infoSection.addField(new TextDynaFieldBuilder().fieldName("firstname")
				.displayName("First Name").fieldIndex(0).build());

		infoSection.addField(new EmailDynaFieldBuilder().fieldName("email")
				.displayName("Email").fieldIndex(1).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("lastname")
				.displayName("Last Name").fieldIndex(2).build());

		infoSection.addField(new PhoneDynaFieldBuilder()
				.fieldName("officephone").displayName("Office Phone")
				.fieldIndex(3).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("title")
				.displayName("Title").fieldIndex(4).build());

		infoSection.addField(new PhoneDynaFieldBuilder().fieldName("mobile")
				.displayName("Mobile").fieldIndex(5).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("department")
				.displayName("Department").fieldIndex(6).build());

		infoSection.addField(new PhoneDynaFieldBuilder()
				.fieldName("otherphone").displayName("Other Phone")
				.fieldIndex(7).build());

		infoSection.addField(new TextDynaFieldBuilder()
				.fieldName("accountname").displayName("Account Name")
				.fieldIndex(8).build());

		infoSection.addField(new PhoneDynaFieldBuilder().fieldName("fax")
				.displayName("Fax").fieldIndex(9).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("source")
				.displayName("Lead Source").fieldIndex(10).build());

		infoSection.addField(new UrlDynaFieldBuilder().fieldName("website")
				.displayName("Website").fieldIndex(11).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("industry")
				.displayName("Industry").fieldIndex(12).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("status")
				.displayName("Status").fieldIndex(13).build());

		infoSection.addField(new IntDynaFieldBuilder().fieldName("noemployees")
				.displayName("No of Employees").fieldIndex(14).build());

		infoSection
				.addField(new TextDynaFieldBuilder()
						.fieldName("assignuser")
						.displayName(
								AppContext
										.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD))
						.fieldIndex(15).build());

		defaultForm.addSection(infoSection);

		DynaSection addressSection = new DynaSectionBuilder()
				.layoutType(LayoutType.TWO_COLUMN).orderIndex(1)
				.header("Address Information").build();

		addressSection.addField(new TextDynaFieldBuilder()
				.fieldName("primaddress").displayName("Address").fieldIndex(0)
				.build());

		addressSection.addField(new TextDynaFieldBuilder()
				.fieldName("otheraddress").displayName("Other Address")
				.fieldIndex(1).build());

		addressSection.addField(new TextDynaFieldBuilder()
				.fieldName("primcity").displayName("City").fieldIndex(2)
				.build());

		addressSection.addField(new TextDynaFieldBuilder()
				.fieldName("othercity").displayName("Other City").fieldIndex(3)
				.build());

		addressSection.addField(new TextDynaFieldBuilder()
				.fieldName("primstate").displayName("State").fieldIndex(4)
				.build());

		addressSection.addField(new TextDynaFieldBuilder()
				.fieldName("otherstate").displayName("Other State")
				.fieldIndex(5).build());

		addressSection.addField(new TextDynaFieldBuilder()
				.fieldName("primpostalcode").displayName("Postal Code")
				.fieldIndex(6).build());

		addressSection.addField(new TextDynaFieldBuilder()
				.fieldName("otherpostalcode").displayName("Other Postal Code")
				.fieldIndex(7).build());

		addressSection.addField(new TextDynaFieldBuilder()
				.fieldName("primcountry").displayName("Country").fieldIndex(8)
				.build());

		addressSection.addField(new TextDynaFieldBuilder()
				.fieldName("othercountry").displayName("Other Country")
				.fieldIndex(9).build());

		defaultForm.addSection(addressSection);

		DynaSection descSection = new DynaSectionBuilder()
				.layoutType(LayoutType.TWO_COLUMN).orderIndex(2)
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
