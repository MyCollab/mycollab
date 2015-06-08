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
package com.esofthead.mycollab.mobile.module.project.view.settings;

import com.esofthead.mycollab.mobile.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */
public class ProjectMemberFormLayoutFactory implements IFormLayoutFactory {
	private static final long serialVersionUID = 8920529536882351151L;

	private GridFormLayoutHelper informationLayout;

	@Override
	public ComponentContainer getLayout() {
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(false);
		Label header = new Label(AppContext.getMessage(ProjectMemberI18nEnum.FORM_INFORMATION_SECTION));
		header.setStyleName("h2");
		layout.addComponent(header);

		this.informationLayout = new GridFormLayoutHelper(1, 3, "100%",
				"150px", Alignment.TOP_LEFT);
		this.informationLayout.getLayout().setWidth("100%");
		this.informationLayout.getLayout().addStyleName("colored-gridlayout");
		this.informationLayout.getLayout().setMargin(false);
		layout.addComponent(this.informationLayout.getLayout());
		layout.setComponentAlignment(this.informationLayout.getLayout(), Alignment.BOTTOM_CENTER);
		return layout;
	}

	@Override
	public void attachField(Object propertyId, Field<?> field) {
		if (propertyId.equals("memberFullName")) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(ProjectMemberI18nEnum.FORM_USER), 0,
					0);
		} else if (propertyId.equals("email")) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(ProjectMemberI18nEnum.M_FORM_EMAIL),
					0, 1);
		} else if (propertyId.equals("roleName")) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(ProjectMemberI18nEnum.FORM_ROLE), 0,
					2);
		}
	}

}
