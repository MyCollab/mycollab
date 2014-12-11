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

import org.vaadin.maddon.layouts.MVerticalLayout;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */

@ViewComponent(scope = ViewScope.PROTOTYPE)
public class MilestoneListNoItemView extends AbstractPageView {
	private static final long serialVersionUID = 740826537581761743L;

	public MilestoneListNoItemView() {
		MVerticalLayout layout = new MVerticalLayout().withSpacing(true)
				.withMargin(true);
		layout.addStyleName("milestone-noitem");
		layout.setDefaultComponentAlignment(Alignment.TOP_CENTER);

		Image image = new Image(null,
				MyCollabResource
						.newResource(WebResourceIds._48_project_milestone));
		layout.addComponent(image);

		Label title = new Label(
				AppContext.getMessage(MilestoneI18nEnum.VIEW_NO_ITEM_TITLE));
		title.addStyleName("h2");
		title.setWidthUndefined();
		layout.addComponent(title);

		Label body = new Label(
				AppContext.getMessage(MilestoneI18nEnum.VIEW_NO_ITEM_HINT));
		body.setWidthUndefined();
		layout.addComponent(body);

		Button createMilestoneBtn = new Button(
				AppContext.getMessage(MilestoneI18nEnum.BUTTON_NEW_PHASE),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						EventBusFactory.getInstance().post(
								new MilestoneEvent.GotoAdd(this, null));
					}
				});
		createMilestoneBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.MILESTONES));

		HorizontalLayout links = new HorizontalLayout();

		links.addComponent(createMilestoneBtn);
		createMilestoneBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
		links.setSpacing(true);

		layout.addComponent(links);
		this.addComponent(layout);
		this.setComponentAlignment(layout, Alignment.TOP_CENTER);
	}

}
