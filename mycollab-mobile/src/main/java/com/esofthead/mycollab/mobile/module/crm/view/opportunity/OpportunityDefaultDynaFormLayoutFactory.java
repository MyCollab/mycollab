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
package com.esofthead.mycollab.mobile.module.crm.view.opportunity;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
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
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
public class OpportunityDefaultDynaFormLayoutFactory {
	public static final DynaForm defaultForm;

	static {
		defaultForm = new DynaForm();

		DynaSection infoSection = new DynaSectionBuilder()
				.layoutType(LayoutType.TWO_COLUMN).orderIndex(0)
				.header("Opportunity Information").build();

		infoSection.addField(new TextDynaFieldBuilder()
				.fieldName("opportunityname").displayName("Name")
				.mandatory(true).fieldIndex(0).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("accountid")
				.displayName("Account Name").fieldIndex(1).build());

		infoSection.addField(new CurrencyDynaFieldBuilder()
				.fieldName("currencyid").displayName("Currency").fieldIndex(2)
				.build());

		infoSection.addField(new DateDynaFieldBuilder()
				.fieldName("expectedcloseddate")
				.displayName("Expected Close Date").fieldIndex(3).build());

		infoSection.addField(new NumberDynaFieldBuilder().fieldName("amount")
				.displayName("Amount").fieldIndex(4).build());

		infoSection.addField(new TextDynaFieldBuilder()
				.fieldName("opportunitytype").displayName("Type").fieldIndex(5)
				.build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("salesstage")
				.displayName("Sales Stage").fieldIndex(6).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("source")
				.displayName("Lead Source").fieldIndex(7).build());

		infoSection.addField(new PercentageDynaFieldBuilder()
				.fieldName("probability").displayName("Probability (%)")
				.fieldIndex(8).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("campaignid")
				.displayName("Campaign").fieldIndex(9).build());

		infoSection.addField(new TextDynaFieldBuilder().fieldName("nextstep")
				.displayName("Next Step").fieldIndex(10).build());

		infoSection
				.addField(new TextDynaFieldBuilder()
						.fieldName("assignuser")
						.displayName(
								AppContext
										.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD))
						.fieldIndex(11).build());

		defaultForm.addSection(infoSection);

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
