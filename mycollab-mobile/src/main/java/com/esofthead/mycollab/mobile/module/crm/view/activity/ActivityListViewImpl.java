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
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

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
	private VerticalLayout addButtons;

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
		final Popover controlBtns = new Popover();
		controlBtns.setClosable(true);
		controlBtns.setStyleName("controls-popover");

		addButtons = new VerticalLayout();
		addButtons.setSpacing(true);
		addButtons.setWidth("100%");
		addButtons.setMargin(true);
		addButtons.addStyleName("edit-btn-layout");

		Button addTask = new Button("Add Task", new Button.ClickListener() {
			private static final long serialVersionUID = 1920289198458066344L;

			@Override
			public void buttonClick(ClickEvent event) {
				controlBtns.close();
				EventBus.getInstance().fireEvent(
						new ActivityEvent.TaskAdd(this, null));
			}
		});
		addButtons.addComponent(addTask);

		Button addCall = new Button("Add Call", new Button.ClickListener() {
			private static final long serialVersionUID = -279151189261011902L;

			@Override
			public void buttonClick(ClickEvent event) {
				controlBtns.close();
				EventBus.getInstance().fireEvent(
						new ActivityEvent.CallAdd(this, null));
			}
		});
		addButtons.addComponent(addCall);

		Button addMeeting = new Button("Add Meeting",
				new Button.ClickListener() {
					private static final long serialVersionUID = 4770664404728700960L;

					@Override
					public void buttonClick(ClickEvent event) {
						controlBtns.close();
						EventBus.getInstance().fireEvent(
								new ActivityEvent.MeetingAdd(this, null));
					}
				});
		addButtons.addComponent(addMeeting);

		controlBtns.setContent(addButtons);

		final Button addActivity = new Button();
		addActivity.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1920289198458066344L;

			@Override
			public void buttonClick(ClickEvent evt) {
				if (!controlBtns.isAttached())
					controlBtns.showRelativeTo(addActivity);
				else
					controlBtns.close();
			}
		});
		addActivity.setStyleName("add-btn");
		return addActivity;
	}
}
