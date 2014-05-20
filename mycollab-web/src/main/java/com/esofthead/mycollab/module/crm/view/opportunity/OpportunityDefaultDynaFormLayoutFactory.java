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
package com.esofthead.mycollab.module.crm.view.opportunity;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.form.view.builder.CurrencyDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.DateDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.DynaSectionBuilder;
import com.esofthead.mycollab.form.view.builder.NumberDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.PercentageDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.TextAreaDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.esofthead.mycollab.module.crm.localization.OpportunityI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class OpportunityDefaultDynaFormLayoutFactory {
	public static final DynaForm defaultForm;

	static {
		defaultForm = new DynaForm();

		DynaSection infoSection = new DynaSectionBuilder()
				.layoutType(LayoutType.TWO_COLUMN)
				.orderIndex(0)
				.header(AppContext
						.getMessage(OpportunityI18nEnum.SECTION_OPPORTUNITY_INFORMATION))
				.build();

		infoSection.addField(new TextDynaFieldBuilder()
				.fieldName("opportunityname")
				.displayName(
						AppContext.getMessage(OpportunityI18nEnum.FORM_NAME))
				.mandatory(true).fieldIndex(0).build());

		infoSection
				.addField(new TextDynaFieldBuilder()
						.fieldName("accountid")
						.displayName(
								AppContext
										.getMessage(OpportunityI18nEnum.FORM_ACCOUNT_NAME))
						.fieldIndex(1).build());

		infoSection.addField(new CurrencyDynaFieldBuilder()
				.fieldName("currencyid")
				.displayName(
						AppContext
								.getMessage(OpportunityI18nEnum.FORM_CURRENCY))
				.fieldIndex(2).build());

		infoSection
				.addField(new DateDynaFieldBuilder()
						.fieldName("expectedcloseddate")
						.displayName(
								AppContext
										.getMessage(OpportunityI18nEnum.FORM_EXPECTED_CLOSE_DATE))
						.fieldIndex(3).build());

		infoSection.addField(new NumberDynaFieldBuilder()
				.fieldName("amount")
				.displayName(
						AppContext.getMessage(OpportunityI18nEnum.FORM_AMOUNT))
				.fieldIndex(4).build());

		infoSection.addField(new TextDynaFieldBuilder()
				.fieldName("opportunitytype")
				.displayName(
						AppContext.getMessage(OpportunityI18nEnum.FORM_TYPE))
				.fieldIndex(5).build());

		infoSection
				.addField(new TextDynaFieldBuilder()
						.fieldName("salesstage")
						.displayName(
								AppContext
										.getMessage(OpportunityI18nEnum.FORM_SALE_STAGE))
						.fieldIndex(6).build());

		infoSection
				.addField(new TextDynaFieldBuilder()
						.fieldName("source")
						.displayName(
								AppContext
										.getMessage(OpportunityI18nEnum.FORM_LEAD_SOURCE))
						.fieldIndex(7).build());

		infoSection
				.addField(new PercentageDynaFieldBuilder()
						.fieldName("probability")
						.displayName(
								AppContext
										.getMessage(OpportunityI18nEnum.FORM_PROBABILITY))
						.fieldIndex(8).build());

		infoSection
				.addField(new TextDynaFieldBuilder()
						.fieldName("campaignid")
						.displayName(
								AppContext
										.getMessage(OpportunityI18nEnum.FORM_CAMPAIGN_NAME))
						.fieldIndex(9).build());

		infoSection
				.addField(new TextDynaFieldBuilder()
						.fieldName("nextstep")
						.displayName(
								AppContext
										.getMessage(OpportunityI18nEnum.FORM_NEXT_STEP))
						.fieldIndex(10).build());

		infoSection
				.addField(new TextDynaFieldBuilder()
						.fieldName("assignuser")
						.displayName(
								AppContext
										.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD))
						.fieldIndex(11).build());

		defaultForm.addSection(infoSection);

		DynaSection descSection = new DynaSectionBuilder()
				.layoutType(LayoutType.ONE_COLUMN)
				.orderIndex(1)
				.header(AppContext
						.getMessage(OpportunityI18nEnum.SECTION_DESCRIPTION))
				.build();

		descSection.addField(new TextAreaDynaFieldBuilder()
				.fieldName("description")
				.displayName(
						AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION))
				.fieldIndex(0).build());

		defaultForm.addSection(descSection);
	}

	public static DynaForm getForm() {
		return defaultForm;
	}
}
