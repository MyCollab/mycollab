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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.form.view.builder.DynaSectionBuilder;
import com.esofthead.mycollab.form.view.builder.TextDynaFieldBuilder;
import com.esofthead.mycollab.form.view.builder.type.DynaForm;
import com.esofthead.mycollab.form.view.builder.type.DynaSection;
import com.esofthead.mycollab.form.view.builder.type.DynaSection.LayoutType;
import com.esofthead.mycollab.module.project.i18n.ComponentI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.vaadin.AppContext;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.5.4
 *
 */
public class ComponentDefaultFormLayoutFactory {
	private static final DynaForm defaultForm;

	static {
		defaultForm = new DynaForm();
		DynaSection mainSection = new DynaSectionBuilder().layoutType(
				LayoutType.ONE_COLUMN).build();

		mainSection
				.addField(new TextDynaFieldBuilder()
						.fieldName(Component.Field.componentname)
						.displayName(
								AppContext
										.getMessage(ComponentI18nEnum.FORM_NAME))
						.required(true).mandatory(true).fieldIndex(0).build());

		mainSection.addField(new TextDynaFieldBuilder()
				.fieldName(Component.Field.description)
				.displayName(
						AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION))
				.fieldIndex(1).build());

		mainSection
				.addField(new TextDynaFieldBuilder()
						.fieldName(Component.Field.userlead)
						.displayName(
								AppContext
										.getMessage(ComponentI18nEnum.FORM_LEAD))
						.fieldIndex(2).build());

		defaultForm.addSection(mainSection);
	}

	public static DynaForm getForm() {
		return defaultForm;
	}
}
