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
package com.esofthead.mycollab.mobile.module.crm.view.activity;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.mobile.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.mobile.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.mobile.ui.TableClickEvent;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */

@ViewComponent
public class ActivityListViewImpl extends
		AbstractListViewComp<ActivitySearchCriteria, SimpleActivity> implements
		ActivityListView {
	private static final long serialVersionUID = -7632616933330982900L;

	public ActivityListViewImpl() {
		super();

		setCaption("Activities");
		setToggleButton(true);
	}

	@Override
	protected AbstractPagedBeanList<ActivitySearchCriteria, SimpleActivity> createBeanTable() {
		ActivityListDisplay activityListDisplay = new ActivityListDisplay(
				"subject");
		activityListDisplay
				.addTableListener(new ApplicationEventListener<TableClickEvent>() {
					private static final long serialVersionUID = 8965270809651619473L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return TableClickEvent.class;
					}

					@Override
					public void handle(TableClickEvent event) {
						final SimpleActivity activity = (SimpleActivity) event
								.getData();
						if ("subject".equals(event.getFieldName())) {
							if (activity.getEventType().equals(
									CrmTypeConstants.TASK)) {
								EventBus.getInstance().fireEvent(
										new ActivityEvent.TaskRead(
												ActivityListViewImpl.this,
												activity.getId()));
							} else if (activity.getEventType().equals(
									CrmTypeConstants.CALL)) {
								EventBus.getInstance().fireEvent(
										new ActivityEvent.CallRead(
												ActivityListViewImpl.this,
												activity.getId()));
							} else if (activity.getEventType().equals(
									CrmTypeConstants.MEETING)) {
								EventBus.getInstance().fireEvent(
										new ActivityEvent.MeetingRead(
												ActivityListViewImpl.this,
												activity.getId()));
							}
						}
					}
				});
		return activityListDisplay;
	}

	@Override
	protected Component createRightComponent() {
		Button addActivity = new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = -5246005226179299205L;

			@Override
			public void buttonClick(ClickEvent event) {
				// EventBus.getInstance().fireEvent(
				// new ActivityEvent.GotoAdd(this, null));
			}
		});
		addActivity.setStyleName("add-btn");
		return addActivity;
	}

}
