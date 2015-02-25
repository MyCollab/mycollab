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
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ComponentI18nEnum;
import com.esofthead.mycollab.utils.FieldGroupFormatter;
import com.esofthead.mycollab.utils.FieldGroupFormatter.I18nHistoryFieldFormat;
import com.esofthead.mycollab.vaadin.ui.HistoryLogComponent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ComponentHistoryLogList extends HistoryLogComponent {
	private static final long serialVersionUID = 1L;

	public static final FieldGroupFormatter componentFormatter;

	static {
		componentFormatter = new FieldGroupFormatter();

		componentFormatter.generateFieldDisplayHandler("componentname",
				ComponentI18nEnum.FORM_NAME);
		componentFormatter.generateFieldDisplayHandler("description",
				GenericI18Enum.FORM_DESCRIPTION);
		componentFormatter.generateFieldDisplayHandler("userlead",
				ComponentI18nEnum.FORM_LEAD);
		componentFormatter.generateFieldDisplayHandler("status",
				ComponentI18nEnum.FORM_STATUS, new I18nHistoryFieldFormat(
						StatusI18nEnum.class));
	}

	public ComponentHistoryLogList(String module, String type) {
		super(module, type);
	}

	@Override
	protected FieldGroupFormatter buildFormatter() {
		return componentFormatter;
	}

}
