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
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.IFormLayoutFactory;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
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
	public ComponentContainer getLayout() {
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
	public void attachField(final Object propertyId, final Field<?> field) {
		if (BugWithBLOBs.Field.summary.equalTo(propertyId)) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(BugI18nEnum.FORM_SUMMARY), 0, 0, 2,
					"100%");
		} else if (BugWithBLOBs.Field.priority.equalTo(propertyId)) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(BugI18nEnum.FORM_PRIORITY), 0, 1);
		} else if (SimpleBug.Field.components.equalTo(propertyId)) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(BugI18nEnum.FORM_COMPONENTS), 1, 1);
		} else if (BugWithBLOBs.Field.severity.equalTo(propertyId)) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(BugI18nEnum.FORM_SEVERITY), 0, 2);
		} else if (SimpleBug.Field.affectedVersions.equalTo(propertyId)) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(BugI18nEnum.FORM_AFFECTED_VERSIONS),
					1, 2);
		} else if (BugWithBLOBs.Field.duedate.equalTo(propertyId)) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(BugI18nEnum.FORM_DUE_DATE), 0, 3);
		} else if (SimpleBug.Field.fixedVersions.equalTo(propertyId)) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(BugI18nEnum.FORM_FIXED_VERSIONS), 1,
					3);
		} else if (BugWithBLOBs.Field.assignuser.equalTo(propertyId)) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE), 0, 4);
		} else if (BugWithBLOBs.Field.milestoneid.equalTo(propertyId)) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(BugI18nEnum.FORM_PHASE), 1, 4);
		} else if (BugWithBLOBs.Field.estimatetime.equalTo(propertyId)) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(BugI18nEnum.FORM_ORIGINAL_ESTIMATE),
					0, 5);
		} else if (BugWithBLOBs.Field.estimateremaintime.equalTo(propertyId)) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(BugI18nEnum.FORM_REMAIN_ESTIMATE), 1,
					5);
		} else if (BugWithBLOBs.Field.environment.equalTo(propertyId)) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(BugI18nEnum.FORM_ENVIRONMENT), 0, 6,
					2, "100%");
		} else if (BugWithBLOBs.Field.description.equalTo(propertyId)) {
			this.informationLayout.addComponent(field,
					AppContext.getMessage(GenericI18Enum.FORM_DESCRIPTION), 0,
					7, 2, "100%");
		} else if (BugWithBLOBs.Field.id.equalTo(propertyId)) {// add attachment
																// box
			this.informationLayout.addComponent(field,
					AppContext.getMessage(BugI18nEnum.FORM_ATTACHMENT), 0, 8,
					2, "100%");
		}

	}
}
