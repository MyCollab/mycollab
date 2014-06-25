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

package com.esofthead.mycollab.module.project.view.milestone;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Field;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class MilestoneFormLayoutFactory implements IFormLayoutFactory {
	private static final long serialVersionUID = 1L;

	private GridFormLayoutHelper informationLayout;

	@Override
	public void attachField(final Object propertyId, final Field<?> field) {
		if (propertyId.equals("name")) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(MilestoneI18nEnum.FORM_NAME_FIELD),
					0, 0, 2, "100%");
		} else if (propertyId.equals("startdate")) {
			this.informationLayout.addComponent(field, AppContext
					.getMessage(MilestoneI18nEnum.FORM_START_DATE_FIELD), 0, 1);
		} else if (propertyId.equals("enddate")) {
			this.informationLayout.addComponent(field, AppContext
					.getMessage(MilestoneI18nEnum.FORM_END_DATE_FIELD), 0, 2);
		} else if (propertyId.equals("owner")) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 1, 1);
		} else if (propertyId.equals("status")) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(MilestoneI18nEnum.FORM_STATUS_FIELD),
					1, 2);
		} else if (propertyId.equals("numOpenTasks")) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(MilestoneI18nEnum.FORM_TASK_FIELD),
					0, 3);
		} else if (propertyId.equals("numOpenBugs")) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(MilestoneI18nEnum.FORM_BUG_FIELD), 1,
					3);
		} else if (propertyId.equals("description")) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0,
					4, 2, "100%");
		}
	}

	@Override
	public Layout getLayout() {
		final VerticalLayout layout = new VerticalLayout();

		this.informationLayout = new GridFormLayoutHelper(2, 5, "100%",
				"145px", Alignment.TOP_LEFT);
		this.informationLayout.getLayout().setWidth("100%");
		this.informationLayout.getLayout().addStyleName("colored-gridlayout");
		this.informationLayout.getLayout().setMargin(false);
		layout.addComponent(this.informationLayout.getLayout());
		layout.setComponentAlignment(this.informationLayout.getLayout(),
				Alignment.BOTTOM_CENTER);
		return layout;
	}
}
