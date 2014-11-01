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
package com.esofthead.mycollab.mobile.module.project.view.bug;

import com.esofthead.mycollab.common.i18n.DayI18nEnum;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.mobile.module.project.events.BugEvent;
import com.esofthead.mycollab.mobile.ui.DefaultPagedBeanList;
import com.esofthead.mycollab.mobile.ui.IconConstants;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.hp.gagawa.java.elements.A;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.5.2
 */
public class BugListDisplay extends
		DefaultPagedBeanList<BugService, BugSearchCriteria, SimpleBug> {

	private static final long serialVersionUID = -8911176517887730007L;

	public BugListDisplay() {
		super(ApplicationContextUtil.getSpringBean(BugService.class),
				new BugRowDisplayHandler());
		this.addStyleName("bugs-list");
	}

	private static class BugRowDisplayHandler implements
			RowDisplayHandler<SimpleBug> {

		@Override
		public Component generateRow(final SimpleBug bug, int rowIndex) {
			HorizontalLayout bugRowLayout = new HorizontalLayout();
			bugRowLayout.setWidth("100%");
			bugRowLayout.setStyleName("list-item");
			bugRowLayout.setSpacing(true);

			Label bugIconLbl = new Label(
					"<span aria-hidden=\"true\" data-icon=\""
							+ IconConstants.PROJECT_BUG + "\"></span>");
			bugIconLbl.setContentMode(ContentMode.HTML);
			bugIconLbl.setWidthUndefined();
			bugIconLbl.setStyleName("bug-icon");
			bugRowLayout.addComponent(bugIconLbl);

			VerticalLayout bugInfoLayout = new VerticalLayout();
			bugInfoLayout.setWidth("100%");

			Button bugName = new Button("["
					+ CurrentProjectVariables.getProject().getShortname() + "-"
					+ bug.getBugkey() + "]: " + bug.getSummary(),
					new Button.ClickListener() {

						private static final long serialVersionUID = 2763986609736084480L;

						@Override
						public void buttonClick(Button.ClickEvent event) {
							EventBusFactory.getInstance().post(
									new BugEvent.GotoRead(this, bug.getId()));
						}
					});
			bugName.setWidth("100%");
			bugName.setStyleName("bug-name");
			bugInfoLayout.addComponent(bugName);

			Label lastUpdatedTimeLbl = new Label(AppContext.getMessage(
					DayI18nEnum.LAST_UPDATED_ON,
					AppContext.formatDateTime(bug.getLastupdatedtime())));
			lastUpdatedTimeLbl.setStyleName("bug-meta-info");
			bugInfoLayout.addComponent(lastUpdatedTimeLbl);

			A assigneeLink = new A();
			assigneeLink.setHref(ProjectLinkGenerator
					.generateProjectMemberFullLink(AppContext.getSiteUrl(),
							CurrentProjectVariables.getProjectId(),
							bug.getAssignuser()));
			assigneeLink.setCSSClass("bug-assignee");
			assigneeLink.appendText(bug.getAssignuserFullName());

			Label assigneeLbl = new Label(
					AppContext.getMessage(GenericI18Enum.FORM_ASSIGNEE)
							+ (bug.getAssignuserFullName() == null ? ":&nbsp;N/A&nbsp;"
									: ":&nbsp;" + assigneeLink.write()));
			assigneeLbl.setStyleName("bug-meta-info");
			assigneeLbl.setContentMode(ContentMode.HTML);
			bugInfoLayout.addComponent(assigneeLbl);

			Label statusLbl = new Label(
					AppContext.getMessage(BugI18nEnum.FORM_STATUS)
							+ ":&nbsp;<span class='bug-status'>"
							+ AppContext.getMessage(BugStatus.class,
									bug.getStatus()) + "</span>");
			statusLbl.setContentMode(ContentMode.HTML);
			statusLbl.setStyleName("bug-meta-info");
			bugInfoLayout.addComponent(statusLbl);

			bugRowLayout.addComponent(bugInfoLayout);
			bugRowLayout.setExpandRatio(bugInfoLayout, 1.0f);

			return bugRowLayout;
		}

	}
}
