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

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.module.project.localization.TaskI18nEnum;
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
 * 
 */
public class BugAddFormLayoutFactory implements IFormLayoutFactory {

	private static final long serialVersionUID = 1L;
	private GridFormLayoutHelper informationLayout;

	@Override
	public Layout getLayout() {
		final VerticalLayout layout = new VerticalLayout();

		this.informationLayout = new GridFormLayoutHelper(2, 9, "100%",
				"167px", Alignment.TOP_LEFT);
		this.informationLayout.getLayout().setWidth("100%");
		this.informationLayout.getLayout().setMargin(false);
		this.informationLayout.getLayout().addStyleName("colored-gridlayout");
		layout.addComponent(this.informationLayout.getLayout());
		layout.setComponentAlignment(this.informationLayout.getLayout(),
				Alignment.BOTTOM_CENTER);

		return layout;
	}

	@Override
	public boolean attachField(final Object propertyId, final Field<?> field) {
		if (propertyId.equals("summary")) {
			this.informationLayout.addComponent(field, "Summary", 0, 0, 2,
					"100%");
		} else if (propertyId.equals("priority")) {
			this.informationLayout.addComponent(field, "Priority", 0, 1);
		} else if (propertyId.equals("severity")) {
			this.informationLayout.addComponent(field, "Severity", 0, 2);
		} else if (propertyId.equals("duedate")) {
			this.informationLayout.addComponent(field, "Due Date", 0, 3);
		} else if (propertyId.equals("assignuser")) {
			this.informationLayout.addComponent(field, AppContext
					.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD), 0, 4);
		} else if (propertyId.equals("components")) {
			this.informationLayout.addComponent(field, "Components", 1, 1);
		} else if (propertyId.equals("affectedVersions")) {
			this.informationLayout.addComponent(field, "Affected Versions", 1,
					2);
		} else if (propertyId.equals("fixedVersions")) {
			this.informationLayout.addComponent(field, "Fixed Versions", 1, 3);
		} else if (propertyId.equals("estimatetime")) {
			this.informationLayout.addComponent(field, "Original Estimate", 0,
					5);
		} else if (propertyId.equals("estimateremaintime")) {
			this.informationLayout.addComponent(field, "Remain Estimate", 1, 5);
		} else if (propertyId.equals("environment")) {
			this.informationLayout.addComponent(field, "Environment", 0, 6, 2,
					"100%");
		} else if (propertyId.equals("description")) {
			this.informationLayout.addComponent(field, "Description", 0, 7, 2,
					"100%");
		} else if (propertyId.equals("id")) {// add attachment box
			this.informationLayout.addComponent(field, "Attachment", 0, 8, 2,
					"100%");
		} else if (propertyId.equals("milestoneid")) {
			this.informationLayout.addComponent(field, AppContext
					.getMessage(TaskI18nEnum.FORM_PHASE_FIELD), 1, 4);
		} else {
			return false;
		}

		return true;

	}
}
