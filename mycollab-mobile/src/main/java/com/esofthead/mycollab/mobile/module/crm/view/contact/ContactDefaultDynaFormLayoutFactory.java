/**
 * This file is part of mycollab-mobile.
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
package com.esofthead.mycollab.mobile.module.crm.view.contact;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.form.view.builder.BooleanDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.DynaSectionBuilder;
import com.esofthead.mycollab.form.view.builder.EmailDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.PhoneDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.esofthead.mycollab.mobile.module.crm.localization.CrmCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ContactDefaultDynaFormLayoutFactory {
	public static final DynaForm defaultForm;

	static {
		defaultForm = new DynaForm();

		DynaSection contactSection = new DynaSectionBuilder()
				.layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
				.header("Contact Information").build();

		contactSection.addField(new TextDynaFieldBuilder()
				.fieldName("firstname").displayName("First Name").fieldIndex(0)
				.build());

		contactSection
				.addField(new PhoneDynaFieldBuilder()
						.fieldName("officephone")
						.displayName(
								AppContext
										.getMessage(CrmCommonI18nEnum.FORM_PHONE_OFFICE_FIELD))
						.fieldIndex(1).build());

		contactSection.addField(new TextDynaFieldBuilder()
				.fieldName("lastname").displayName("Last Name").fieldIndex(2)
				.mandatory(true).build());

		contactSection.addField(new PhoneDynaFieldBuilder().fieldName("mobile")
				.displayName("Mobile").fieldIndex(3).build());

		contactSection.addField(new TextDynaFieldBuilder()
				.fieldName("accountid").displayName("Account").fieldIndex(4)
				.build());

		contactSection.addField(new PhoneDynaFieldBuilder()
				.fieldName("homephone").displayName("Home Phone").fieldIndex(5)
				.build());

		contactSection.addField(new TextDynaFieldBuilder().fieldName("title")
				.displayName("Title").fieldIndex(6).build());

		contactSection.addField(new PhoneDynaFieldBuilder()
				.fieldName("otherphone").displayName("Other Phone")
				.fieldIndex(7).build());

		contactSection.addField(new TextDynaFieldBuilder()
				.fieldName("department").displayName("Department")
				.fieldIndex(8).build());

		contactSection.addField(new PhoneDynaFieldBuilder().fieldName("fax")
				.displayName("Fax").fieldIndex(9).build());

		contactSection.addField(new EmailDynaFieldBuilder().fieldName("email")
				.displayName("Email").fieldIndex(10).build());

		contactSection.addField(new TextDynaFieldBuilder()
				.fieldName("birthday").displayName("Birthday").fieldIndex(11)
				.build());

		contactSection.addField(new TextDynaFieldBuilder()
				.fieldName("assistant").displayName("Assistant").fieldIndex(12)
				.build());

		contactSection.addField(new BooleanDynaFieldBuilder()
				.fieldName("iscallable").displayName("Callable").fieldIndex(13)
				.build());

		contactSection.addField(new TextDynaFieldBuilder()
				.fieldName("assistantphone").displayName("Assistant Phone")
				.fieldIndex(14).build());

		contactSection
				.addField(new TextDynaFieldBuilder()
						.fieldName("assignuser")
						.displayName(
								AppContext
										.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD))
						.fieldIndex(15).build());

		contactSection.addField(new TextDynaFieldBuilder()
				.fieldName("leadsource").displayName("Lead Source")
				.fieldIndex(16).build());

		defaultForm.addSection(contactSection);

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
				.layoutType(LayoutType.ONE_COLUMN).orderIndex(2)
				.header("Description").build();

		descSection.addField(new TextDynaFieldBuilder()
				.fieldName("description").displayName("Description")
				.fieldIndex(0).build());

		defaultForm.addSection(descSection);
	}

	public static DynaForm getForm() {
		return defaultForm;
	}
}
