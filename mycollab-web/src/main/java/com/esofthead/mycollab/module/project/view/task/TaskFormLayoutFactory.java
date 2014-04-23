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

package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.project.localization.TaskI18nEnum;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TaskFormLayoutFactory implements IFormLayoutFactory {
	private static final long serialVersionUID = 1L;

	private GridFormLayoutHelper informationLayout;

	@Override
	public Layout getLayout() {
		this.informationLayout = new GridFormLayoutHelper(2, 8, "100%",
				"180px", Alignment.TOP_LEFT);
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		this.informationLayout.getLayout().setMargin(false);
		this.informationLayout.getLayout().setWidth("100%");
		this.informationLayout.getLayout().addStyleName("colored-gridlayout");
		layout.addComponent(this.informationLayout.getLayout());
		layout.setComponentAlignment(this.informationLayout.getLayout(),
				Alignment.BOTTOM_CENTER);
		return layout;
	}

	@Override
	public boolean attachField(final Object propertyId, final Field<?> field) {
		if (propertyId.equals("taskname")) {
			this.informationLayout.addComponent(field, "Task Name", 0, 0, 2,
					"100%");
		} else if (propertyId.equals("startdate")) {
			this.informationLayout.addComponent(field, "Start Date", 0, 1);
		} else if (propertyId.equals("enddate")) {
			this.informationLayout.addComponent(field, "End Date", 0, 2);
		} else if (propertyId.equals("actualstartdate")) {
			this.informationLayout.addComponent(field, "Actual Start Date", 1,
					1);
		} else if (propertyId.equals("actualenddate")) {
			this.informationLayout.addComponent(field, "Actual End Date", 1, 2);
		} else if (propertyId.equals("deadline")) {
			this.informationLayout.addComponent(field, "Deadline", 0, 3);
		} else if (propertyId.equals("priority")) {
			this.informationLayout.addComponent(field, "Priority", 1, 3);
		} else if (propertyId.equals("assignuser")) {
			this.informationLayout.addComponent(field, LocalizationHelper
					.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD), 0, 4);
		} else if (propertyId.equals("tasklistid")) {
			this.informationLayout.addComponent(field, LocalizationHelper
					.getMessage(TaskI18nEnum.FORM_TASKGROUP_FIELD), 1, 4);
		} else if (propertyId.equals("percentagecomplete")) {
			this.informationLayout.addComponent(field, "Complete(%)", 0, 5, 2,
					"100%", Alignment.MIDDLE_LEFT);
		} else if (propertyId.equals("notes")) {
			field.setSizeUndefined();
			this.informationLayout
					.addComponent(field, "Notes", 0, 6, 2, "100%");
		} else if (propertyId.equals("id")) {
			this.informationLayout.addComponent(field, "Attachments", 0, 7, 2,
					"100%");
		} else {
			return false;
		}

		return true;
	}

	public void setPercent(String labelPercent) {
		((HorizontalLayout) this.informationLayout.getComponent(0, 5))
				.removeAllComponents();
		((HorizontalLayout) this.informationLayout.getComponent(0, 5))
				.addComponent(new Label(labelPercent));
	}
}
