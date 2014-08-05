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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class BugSimpleDisplayWidget extends
		BeanList<BugService, BugSearchCriteria, SimpleBug> {
	private static final long serialVersionUID = 1L;

	public BugSimpleDisplayWidget() {
		super(null, ApplicationContextUtil.getSpringBean(BugService.class),
				TaskRowDisplayHandler.class);
	}

	public static class TaskRowDisplayHandler extends
			BeanList.RowDisplayHandler<SimpleBug> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component generateRow(final SimpleBug bug, int rowIndex) {
			HorizontalLayout layout = new HorizontalLayout();
			layout.setSpacing(true);
			Button bugLink = new Button("Issue #" + bug.getBugkey() + ": ",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(ClickEvent event) {
							EventBusFactory.getInstance().post(
									new BugEvent.GotoRead(this, bug.getId()));
						}
					});
			bugLink.setStyleName("link");
			bugLink.setWidth(Sizeable.SIZE_UNDEFINED, Sizeable.Unit.PIXELS);
			if (bug.isCompleted()) {
				bugLink.addStyleName(UIConstants.LINK_COMPLETED);
			} else if (bug.isOverdue()) {
				bugLink.addStyleName(UIConstants.LINK_OVERDUE);
			}
			layout.addComponent(bugLink);

			Label bugSummary = new Label(bug.getSummary());
			layout.addComponent(bugSummary);
			layout.setComponentAlignment(bugSummary, Alignment.MIDDLE_LEFT);
			layout.setExpandRatio(bugSummary, 1.0f);
			return layout;
		}
	}
}
