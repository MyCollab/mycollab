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
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBus;
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
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UserAvatarControlFactory;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class UnresolvedBugsByAssigneeWidget2 extends Depot {
	private static final long serialVersionUID = 1L;

	private BugSearchCriteria bugSearchCriteria;

	public UnresolvedBugsByAssigneeWidget2() {
		super(AppContext
				.getMessage(BugI18nEnum.WIDGET_UNRESOLVED_BY_ASSIGNEE_TITLE),
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
				.getAssignedDefectsSummary(searchCriteria);
		if (!groupItems.isEmpty()) {
			for (final GroupItem item : groupItems) {
				final HorizontalLayout assigneeLayout = new HorizontalLayout();
				assigneeLayout.setSpacing(true);
				assigneeLayout.setWidth("100%");

				String assignUser = item.getGroupid();
				String assignUserFullName = item.getGroupid() == null ? AppContext
						.getMessage(BugI18nEnum.OPT_UNDEFINED_USER) : item
						.getGroupname();
				if (assignUserFullName == null
						|| "".equals(assignUserFullName.trim())) {
					assignUserFullName = StringUtils
							.extractNameFromEmail(assignUser);
				}
				final BugAssigneeLink userLbl = new BugAssigneeLink(assignUser,
						item.getExtraValue(), assignUserFullName);
				assigneeLayout.addComponent(userLbl);
				final ProgressBarIndicator indicator = new ProgressBarIndicator(
						totalCount, totalCount - item.getValue(), false);
				indicator.setWidth("100%");
				assigneeLayout.addComponent(indicator);
				assigneeLayout.setExpandRatio(indicator, 1.0f);
				this.bodyContent.addComponent(assigneeLayout);
			}

		}
	}

	class BugAssigneeLink extends Button {
		private static final long serialVersionUID = 1L;

		public BugAssigneeLink(final String assignee,
				final String assigneeAvatarId, final String assigneeFullName) {
			super(assigneeFullName, new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					UnresolvedBugsByAssigneeWidget2.this.bugSearchCriteria
							.setAssignuser(new StringSearchField(
									SearchField.AND, assignee));
					final BugFilterParameter param = new BugFilterParameter(
							"Unresolved Bugs of " + assigneeFullName,
							UnresolvedBugsByAssigneeWidget2.this.bugSearchCriteria);
					EventBus.getInstance().fireEvent(
							new BugEvent.GotoList(this,
									new BugScreenData.Search(param)));
				}
			});

			this.setStyleName("link");
			this.setWidth("110px");
			this.addStyleName(UIConstants.TEXT_ELLIPSIS);
			this.setIcon(UserAvatarControlFactory.createAvatarResource(
					assigneeAvatarId, 16));
			this.setDescription(assigneeFullName);
		}
	}
}
