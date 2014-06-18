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

import com.esofthead.mycollab.common.ui.components.ProjectTooltipGenerator;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.LabelLink;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.view.parameters.BugFilterParameter;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.LabelHTMLDisplayWidget;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class DueBugWidget extends BugDisplayWidget {
	private static final long serialVersionUID = 1L;

	public DueBugWidget() {
		super(AppContext.getMessage(BugI18nEnum.DUE_BUGS_WIDGET_TITLE),
				DueBugRowDisplayHandler.class);
	}

	@Override
	protected BugFilterParameter constructMoreDisplayFilter() {
		return new BugFilterParameter("Due Bugs", searchCriteria);
	}

	public static class DueBugRowDisplayHandler implements
			BeanList.RowDisplayHandler<SimpleBug> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component generateRow(final SimpleBug bug, final int rowIndex) {
			final HorizontalLayout layout = new HorizontalLayout();
			layout.setWidth("100%");
			layout.setSpacing(true);
			layout.setMargin(true);
			layout.addComponent(new Image(null, MyCollabResource
					.newResource("icons/16/project/bug.png")));

			VerticalLayout rowContent = new VerticalLayout();
			final LabelLink defectLink = new LabelLink("["
					+ CurrentProjectVariables.getProject().getShortname() + "-"
					+ bug.getBugkey() + "]: " + bug.getSummary(),
					ProjectLinkBuilder.generateBugPreviewFullLink(
							bug.getProjectid(), bug.getId()));
			defectLink.setWidth("100%");
			defectLink
					.setDescription(ProjectTooltipGenerator.generateToolTipBug(
							AppContext.getUserLocale(), bug,
							AppContext.getSiteUrl(), AppContext.getTimezoneId()));

			if (bug.isOverdue()) {
				defectLink.addStyleName(UIConstants.LINK_OVERDUE);
			}

			rowContent.addComponent(defectLink);

			final LabelHTMLDisplayWidget descInfo = new LabelHTMLDisplayWidget(
					bug.getDescription());
			descInfo.setWidth("100%");
			rowContent.addComponent(descInfo);

			final Label dateInfo = new Label("Due on "
					+ AppContext.formatDate(bug.getDuedate()) + ". Status: "
					+ bug.getStatus());
			dateInfo.setStyleName(UIConstants.WIDGET_ROW_METADATA);
			rowContent.addComponent(dateInfo);

			final HorizontalLayout hLayoutDateInfo = new HorizontalLayout();
			hLayoutDateInfo.setSpacing(true);
			final Label lbAssignee = new Label("Assignee: ");
			lbAssignee.setStyleName(UIConstants.WIDGET_ROW_METADATA);
			hLayoutDateInfo.addComponent(lbAssignee);
			hLayoutDateInfo.setComponentAlignment(lbAssignee,
					Alignment.MIDDLE_CENTER);

			final ProjectUserLink userLink = new ProjectUserLink(
					bug.getAssignuser(), bug.getAssignUserAvatarId(),
					bug.getAssignuserFullName(), false, true);
			hLayoutDateInfo.addComponent(userLink);
			hLayoutDateInfo.setComponentAlignment(userLink,
					Alignment.MIDDLE_CENTER);

			rowContent.addComponent(hLayoutDateInfo);
			layout.addComponent(rowContent);
			layout.setExpandRatio(rowContent, 1.0f);
			layout.setStyleName(UIConstants.WIDGET_ROW);
			if ((rowIndex + 1) % 2 != 0) {
				layout.addStyleName("odd");
			}
			layout.setWidth("100%");
			return layout;
		}
	}
}
