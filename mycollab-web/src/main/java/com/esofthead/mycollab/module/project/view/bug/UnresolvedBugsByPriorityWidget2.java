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

import java.util.List;

import com.esofthead.mycollab.common.domain.GroupItem;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectDataTypeFactory;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.view.parameters.BugFilterParameter;
import com.esofthead.mycollab.module.project.view.parameters.BugScreenData;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.ProgressBarIndicator;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class UnresolvedBugsByPriorityWidget2 extends Depot {
	private static final long serialVersionUID = 1L;

	private BugSearchCriteria bugSearchCriteria;

	public UnresolvedBugsByPriorityWidget2() {
		super(AppContext
				.getMessage(BugI18nEnum.WIDGET_UNRESOLVED_BY_PRIORITY_TITLE),
				new VerticalLayout());
		this.setContentBorder(true);
		((VerticalLayout) this.bodyContent).setSpacing(true);
		((VerticalLayout) this.bodyContent).setMargin(true);
	}

	public void setSearchCriteria(final BugSearchCriteria searchCriteria) {
		this.bugSearchCriteria = searchCriteria;
		this.bodyContent.removeAllComponents();
		final BugService bugService = ApplicationContextUtil
				.getSpringBean(BugService.class);
		final int totalCount = bugService.getTotalCount(searchCriteria);
		final List<GroupItem> groupItems = bugService
				.getPrioritySummary(searchCriteria);
		final BugPriorityClickListener listener = new BugPriorityClickListener();

		if (!groupItems.isEmpty()) {
			for (final String priority : ProjectDataTypeFactory
					.getBugPriorityList()) {
				boolean isFound = false;
				for (final GroupItem item : groupItems) {
					if (priority.equals(item.getGroupid())) {
						isFound = true;
						final HorizontalLayout priorityLayout = new HorizontalLayout();
						priorityLayout.setSpacing(true);
						priorityLayout.setWidth("100%");
						final Button userLbl = new Button(priority, listener);
						final Resource iconPriority = new ExternalResource(
								ProjectResources
										.getIconResourceLink12ByBugPriority(priority));
						userLbl.setIcon(iconPriority);
						userLbl.setWidth("110px");
						userLbl.setStyleName("link");

						priorityLayout.addComponent(userLbl);
						final ProgressBarIndicator indicator = new ProgressBarIndicator(
								totalCount, totalCount - item.getValue(), false);
						indicator.setWidth("100%");
						priorityLayout.addComponent(indicator);
						priorityLayout.setExpandRatio(indicator, 1.0f);

						this.bodyContent.addComponent(priorityLayout);
						continue;
					}
				}

				if (!isFound) {
					final HorizontalLayout priorityLayout = new HorizontalLayout();
					priorityLayout.setSpacing(true);
					priorityLayout.setWidth("100%");
					final Button userLbl = new Button(priority, listener);
					final Resource iconPriority = new ExternalResource(
							ProjectResources
									.getIconResourceLink12ByBugPriority(priority));
					userLbl.setIcon(iconPriority);
					userLbl.setWidth("110px");
					userLbl.setStyleName("link");
					priorityLayout.addComponent(userLbl);
					final ProgressBarIndicator indicator = new ProgressBarIndicator(
							totalCount, totalCount, false);
					indicator.setWidth("100%");
					priorityLayout.addComponent(indicator);
					priorityLayout.setExpandRatio(indicator, 1.0f);

					this.bodyContent.addComponent(priorityLayout);
				}
			}

		}
	}

	private class BugPriorityClickListener implements Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(final ClickEvent event) {
			final String caption = event.getButton().getCaption();
			UnresolvedBugsByPriorityWidget2.this.bugSearchCriteria
					.setPriorities(new SetSearchField<String>(
							new String[] { caption }));
			final BugFilterParameter param = new BugFilterParameter(
					"Unresolved " + caption + " Bug List",
					UnresolvedBugsByPriorityWidget2.this.bugSearchCriteria);
			EventBusFactory.getInstance()
					.post(
							new BugEvent.GotoList(this,
									new BugScreenData.Search(param)));
		}
	}
}
