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

package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.vaadin.ui.AddViewLayout;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Field;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public abstract class ProjectFormLayoutFactory implements IFormLayoutFactory {
	private static final long serialVersionUID = 1L;

	private final String title;

	private ProjectInformationLayout projectInformationLayout;

	public ProjectFormLayoutFactory(final String title) {
		this.title = title;
	}

	@Override
	public Layout getLayout() {
		final AddViewLayout projectAddLayout = new AddViewLayout(this.title,
				MyCollabResource.newResource("icons/24/project/project.png"));

		this.projectInformationLayout = new ProjectInformationLayout();

		final Layout topPanel = this.createTopPanel();
		if (topPanel != null) {
			projectAddLayout.addHeaderRight(topPanel);
		}

		projectAddLayout.addBody(this.projectInformationLayout.getLayout());

		final Layout bottomPanel = this.createTopPanel();
		if (bottomPanel != null) {
			projectAddLayout.addBottomControls(bottomPanel);
		}

		return projectAddLayout;
	}

	@Override
	public boolean attachField(final Object propertyId, final Field<?> field) {
		return this.projectInformationLayout.attachField(propertyId, field);
	}

	protected abstract Layout createTopPanel();

	protected abstract Layout createBottomPanel();

	public static class ProjectInformationLayout implements IFormLayoutFactory {
		private static final long serialVersionUID = 1L;
		private GridFormLayoutHelper informationLayout;
		private GridFormLayoutHelper financialLayout;
		private GridFormLayoutHelper descriptionLayout;

		@Override
		public Layout getLayout() {
			final VerticalLayout layout = new VerticalLayout();

			final Label organizationHeader = new Label("Project Information");
			organizationHeader.setStyleName("h2");
			layout.addComponent(organizationHeader);

			this.informationLayout = new GridFormLayoutHelper(2, 2, "100%",
					"167px", Alignment.TOP_LEFT);
			this.informationLayout.getLayout().setWidth("100%");
			this.informationLayout.getLayout().setMargin(false);
			this.informationLayout.getLayout().addStyleName(
					"colored-gridlayout");
			layout.addComponent(this.informationLayout.getLayout());
			layout.setComponentAlignment(this.informationLayout.getLayout(),
					Alignment.BOTTOM_CENTER);

			final Label financialHeader = new Label(
					"Schedule & Financial Information");
			financialHeader.setStyleName("h2");
			layout.addComponent(financialHeader);

			this.financialLayout = new GridFormLayoutHelper(2, 4, "100%",
					"167px", Alignment.TOP_LEFT);
			this.financialLayout.getLayout().setWidth("100%");
			this.financialLayout.getLayout().setMargin(false);
			this.financialLayout.getLayout().addStyleName("colored-gridlayout");
			layout.addComponent(this.financialLayout.getLayout());
			layout.setComponentAlignment(this.financialLayout.getLayout(),
					Alignment.BOTTOM_CENTER);

			final Label descHeader = new Label("Description");
			descHeader.setStyleName("h2");
			layout.addComponent(descHeader);

			this.descriptionLayout = new GridFormLayoutHelper(2, 1, "100%",
					"167px", Alignment.TOP_LEFT);
			this.descriptionLayout.getLayout().setWidth("100%");
			this.descriptionLayout.getLayout().setMargin(false);
			this.descriptionLayout.getLayout().addStyleName(
					"colored-gridlayout");
			layout.addComponent(this.descriptionLayout.getLayout());
			layout.setComponentAlignment(this.descriptionLayout.getLayout(),
					Alignment.BOTTOM_CENTER);
			return layout;
		}

		@Override
		public boolean attachField(final Object propertyId, final Field<?> field) {
			if (propertyId.equals("name")) {
				this.informationLayout
						.addComponent(field, "Project Name", 0, 0);
			} else if (propertyId.equals("homepage")) {
				this.informationLayout.addComponent(field, "Home Page", 1, 0);
			} else if (propertyId.equals("shortname")) {
				this.informationLayout.addComponent(field, "Short Name", 0, 1);
			} else if (propertyId.equals("projectstatus")) {
				this.informationLayout.addComponent(field, "Status", 1, 1);
			} else if (propertyId.equals("planstartdate")) {
				this.financialLayout.addComponent(field, "Plan Start Date", 0,
						0);
			} else if (propertyId.equals("currencyid")) {
				this.financialLayout.addComponent(field, "Currency", 1, 0);
			} else if (propertyId.equals("planenddate")) {
				this.financialLayout.addComponent(field, "Plan End Date", 0, 1);
			} else if (propertyId.equals("defaultbillingrate")) {
				this.financialLayout.addComponent(field, "Rate", 1, 1);
			} else if (propertyId.equals("actualstartdate")) {
				this.financialLayout.addComponent(field, "Actual Start Date",
						0, 2);
			} else if (propertyId.equals("targetbudget")) {
				this.financialLayout.addComponent(field, "Target Budget", 1, 2);
			} else if (propertyId.equals("actualenddate")) {
				this.financialLayout.addComponent(field, "Actual End Date", 0,
						3);
			} else if (propertyId.equals("actualbudget")) {
				this.financialLayout.addComponent(field, "Actual Budget", 1, 3);
			} else if (propertyId.equals("description")) {
				this.descriptionLayout.addComponent(field, "Description", 0, 0,
						2, UIConstants.DEFAULT_2XCONTROL_WIDTH);
			} else {
				return false;
			}

			return true;
		}
	}
}
