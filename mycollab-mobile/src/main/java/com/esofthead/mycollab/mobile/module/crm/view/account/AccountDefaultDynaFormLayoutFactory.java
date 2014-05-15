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
package com.esofthead.mycollab.mobile.module.crm.view.account;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.form.view.builder.DynaSectionBuilder;
import com.esofthead.mycollab.form.view.builder.EmailDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.IntDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.PhoneDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.PickListDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.esofthead.mycollab.mobile.module.crm.localization.AccountI18nEnum;
import com.esofthead.mycollab.mobile.module.crm.localization.CrmCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public class AccountDefaultDynaFormLayoutFactory {
	public static final DynaForm defaultForm;

	static {
		defaultForm = new DynaForm();

		// Build block account information
		DynaSection accountSection = new DynaSectionBuilder()
				.layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
				.header("Account Information").build();
		accountSection.addField(new TextDynaFieldBuilder()
				.fieldName("accountname")
				.displayName(
						AppContext
								.getMessage(AccountI18nEnum.FORM_ACCOUNT_NAME))
				.customField(false).fieldIndex(0).mandatory(true)
				.required(true).build());
		accountSection
				.addField(new TextDynaFieldBuilder()
						.fieldName("phoneoffice")
						.displayName(
								AppContext
										.getMessage(CrmCommonI18nEnum.FORM_PHONE_OFFICE_FIELD))
						.customField(false).fieldIndex(1).build());

		accountSection.addField(new TextDynaFieldBuilder()
				.fieldName("website")
				.fieldIndex(2)
				.displayName(
						AppContext.getMessage(AccountI18nEnum.FORM_WEBSITE))
				.customField(false).build());

		accountSection.addField(new PhoneDynaFieldBuilder().fieldName("fax")
				.fieldIndex(3)
				.displayName(AppContext.getMessage(AccountI18nEnum.FORM_FAX))
				.customField(false).build());

		accountSection.addField(new IntDynaFieldBuilder()
				.fieldName("numemployees")
				.fieldIndex(4)
				.displayName(
						AppContext.getMessage(AccountI18nEnum.FORM_EMPLOYEES))
				.customField(false).build());

		accountSection
				.addField(new PhoneDynaFieldBuilder()
						.fieldName("alternatephone")
						.fieldIndex(5)
						.displayName(
								AppContext
										.getMessage(AccountI18nEnum.FORM_OTHER_PHONE))
						.customField(false).build());

		accountSection.addField(new PickListDynaFieldBuilder<String>()
				.fieldName("industry")
				.fieldIndex(6)
				.displayName(
						AppContext.getMessage(AccountI18nEnum.FORM_INDUSTRY))
				.customField(false).build());

		accountSection.addField(new EmailDynaFieldBuilder().fieldName("email")
				.fieldIndex(7)
				.displayName(AppContext.getMessage(AccountI18nEnum.FORM_EMAIL))
				.customField(false).build());

		accountSection.addField(new PickListDynaFieldBuilder<String>()
				.fieldName("type").fieldIndex(8)
				.displayName(AppContext.getMessage(AccountI18nEnum.FORM_TYPE))
				.customField(false).build());

		accountSection.addField(new TextDynaFieldBuilder()
				.fieldName("ownership")
				.fieldIndex(9)
				.displayName(
						AppContext.getMessage(AccountI18nEnum.FORM_OWNERSHIP))
				.customField(false).build());

		accountSection
				.addField(new TextDynaFieldBuilder()
						.fieldName("assignuser")
						.fieldIndex(10)
						.displayName(
								AppContext
										.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD))
						.customField(false).build());

		accountSection
				.addField(new TextDynaFieldBuilder()
						.fieldName("annualrevenue")
						.fieldIndex(11)
						.displayName(
								AppContext
										.getMessage(AccountI18nEnum.FORM_ANNUAL_REVENUE))
						.customField(false).build());

		defaultForm.addSection(accountSection);

		// build block address
		DynaSection addressSection = new DynaSectionBuilder()
				.layoutType(LayoutType.TWO_COLUMN)
				.header("Address Information").orderIndex(1).build();
		addressSection
				.addField(new TextDynaFieldBuilder()
						.fieldIndex(0)
						.fieldName("billingaddress")
						.displayName(
								AppContext
										.getMessage(AccountI18nEnum.FORM_BILLING_ADDRESS))
						.customField(false).build());
		addressSection
				.addField(new TextDynaFieldBuilder()
						.fieldIndex(1)
						.fieldName("shippingaddress")
						.displayName(
								AppContext
										.getMessage(AccountI18nEnum.FORM_SHIPPING_ADDRESS))
						.customField(false).build());
		addressSection.addField(new TextDynaFieldBuilder()
				.fieldIndex(2)
				.fieldName("city")
				.displayName(
						AppContext
								.getMessage(AccountI18nEnum.FORM_BILLING_CITY))
				.customField(false).build());

		addressSection
				.addField(new TextDynaFieldBuilder()
						.fieldIndex(3)
						.fieldName("shippingcity")
						.displayName(
								AppContext
										.getMessage(AccountI18nEnum.FORM_SHIPPING_CITY))
						.customField(false).build());

		addressSection
				.addField(new TextDynaFieldBuilder()
						.fieldIndex(4)
						.fieldName("state")
						.displayName(
								AppContext
										.getMessage(AccountI18nEnum.FORM_BILLING_STATE))
						.customField(false).build());

		addressSection
				.addField(new TextDynaFieldBuilder()
						.fieldIndex(5)
						.fieldName("shippingstate")
						.displayName(
								AppContext
										.getMessage(AccountI18nEnum.FORM_SHIPPING_STATE))
						.customField(false).build());

		addressSection
				.addField(new TextDynaFieldBuilder()
						.fieldIndex(6)
						.fieldName("postalcode")
						.displayName(
								AppContext
										.getMessage(AccountI18nEnum.FORM_BILLING_POSTAL_CODE))
						.customField(false).build());

		addressSection
				.addField(new TextDynaFieldBuilder()
						.fieldIndex(7)
						.fieldName("shippingpostalcode")
						.displayName(
								AppContext
										.getMessage(AccountI18nEnum.FORM_SHIPPING_POSTAL_CODE))
						.customField(false).build());

		addressSection
				.addField(new TextDynaFieldBuilder()
						.fieldIndex(8)
						.fieldName("billingcountry")
						.displayName(
								AppContext
										.getMessage(AccountI18nEnum.FORM_BILLING_COUNTRY))
						.customField(false).build());

		addressSection
				.addField(new TextDynaFieldBuilder()
						.fieldIndex(9)
						.fieldName("shippingcountry")
						.displayName(
								AppContext
										.getMessage(AccountI18nEnum.FORM_SHIPPING_COUNTRY))
						.customField(false).build());

		defaultForm.addSection(addressSection);

		// build block description
		DynaSection descSection = new DynaSectionBuilder()
				.layoutType(LayoutType.ONE_COLUMN).header("Description")
				.orderIndex(2).build();

		descSection.addField(new TextDynaFieldBuilder()
				.fieldIndex(0)
				.fieldName("description")
				.customField(false)
				.displayName(
						AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION))
				.build());
		defaultForm.addSection(descSection);

	}

	public static DynaForm getForm() {
		return defaultForm;
	}
}
