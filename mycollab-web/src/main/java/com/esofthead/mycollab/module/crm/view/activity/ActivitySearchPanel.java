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
package com.esofthead.mycollab.module.crm.view.activity;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.events.ActivityEvent;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.Separator;
import com.esofthead.mycollab.vaadin.ui.SplitButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
public class ActivitySearchPanel extends
		GenericSearchPanel<ActivitySearchCriteria> {

	private static final long serialVersionUID = 1L;
	protected ActivitySearchCriteria searchCriteria;

	@Override
	public void attach() {
		super.attach();
		this.createBasicSearchLayout();
	}

	private void createBasicSearchLayout() {
		final EventBasicSearchLayout layout = new EventBasicSearchLayout();
		this.setCompositionRoot(layout);
	}

	private HorizontalLayout createSearchTopPanel() {
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeUndefined();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);

		final Image iconComp = new Image();
		iconComp.setSource(MyCollabResource
				.newResource("icons/22/crm/event.png"));
		layout.addComponent(iconComp);
		final Label searchtitle = new Label("Events");
		searchtitle.setStyleName(Reindeer.LABEL_H2);
		layout.addComponent(searchtitle);
		layout.setComponentAlignment(searchtitle, Alignment.MIDDLE_LEFT);
		layout.setExpandRatio(searchtitle, 1.0f);

		final SplitButton controlsBtn = new SplitButton();
		controlsBtn.setSizeUndefined();
		controlsBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_CALL)
				|| AppContext.canWrite(RolePermissionCollections.CRM_MEETING));
		controlsBtn.addStyleName(UIConstants.THEME_BLUE_LINK);
		controlsBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		controlsBtn.setCaption("Create Task");
		controlsBtn
				.addClickListener(new SplitButton.SplitButtonClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void splitButtonClick(
							final SplitButton.SplitButtonClickEvent event) {
						EventBus.getInstance().fireEvent(
								new ActivityEvent.TaskAdd(this, null));
					}
				});

		final VerticalLayout btnControlsLayout = new VerticalLayout();
		controlsBtn.setContent(btnControlsLayout);

		final Button createMeetingBtn = new Button("Create Event",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final Button.ClickEvent event) {
						controlsBtn.setPopupVisible(false);
						EventBus.getInstance().fireEvent(
								new ActivityEvent.MeetingAdd(this, null));
					}
				});
		createMeetingBtn.setStyleName("link");
		btnControlsLayout.addComponent(createMeetingBtn);
		createMeetingBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_MEETING));
		final Button createCallBtn = new Button("Create Call",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final Button.ClickEvent event) {
						controlsBtn.setPopupVisible(false);
						EventBus.getInstance().fireEvent(
								new ActivityEvent.CallAdd(this, null));
					}
				});
		createCallBtn.setStyleName("link");
		createCallBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_CALL));
		btnControlsLayout.addComponent(createCallBtn);
		UiUtils.addComponent(layout, controlsBtn, Alignment.MIDDLE_RIGHT);

		return layout;
	}

	@SuppressWarnings({ "serial", "rawtypes" })
	private class EventBasicSearchLayout extends BasicSearchLayout {

		private TextField nameField;
		private CheckBox myItemCheckbox;

		@SuppressWarnings("unchecked")
		public EventBasicSearchLayout() {
			super(ActivitySearchPanel.this);
		}

		@Override
		public ComponentContainer constructHeader() {
			return ActivitySearchPanel.this.createSearchTopPanel();
		}

		@Override
		public ComponentContainer constructBody() {
			final HorizontalLayout basicSearchBody = new HorizontalLayout();
			basicSearchBody.setMargin(true);

			this.nameField = new TextField();
			this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
			UiUtils.addComponent(basicSearchBody, this.nameField,
					Alignment.MIDDLE_CENTER);

			final Button searchBtn = new Button();
			searchBtn.setStyleName("search-icon-button");
			searchBtn.setIcon(MyCollabResource
					.newResource("icons/16/search_white.png"));

			searchBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final Button.ClickEvent event) {
					EventBasicSearchLayout.this.callSearchAction();
				}
			});
			UiUtils.addComponent(basicSearchBody, searchBtn,
					Alignment.MIDDLE_LEFT);

			this.myItemCheckbox = new CheckBox("My Items");
			this.myItemCheckbox.setWidth("75px");
			UiUtils.addComponent(basicSearchBody, this.myItemCheckbox,
					Alignment.MIDDLE_CENTER);

			final Separator separator1 = new Separator();
			UiUtils.addComponent(basicSearchBody, separator1,
					Alignment.MIDDLE_LEFT);

			final Button cancelBtn = new Button("Clear");
			cancelBtn.setStyleName(UIConstants.THEME_LINK);
			cancelBtn.addStyleName("cancel-button");
			cancelBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final Button.ClickEvent event) {
					EventBasicSearchLayout.this.nameField.setValue("");
				}
			});
			UiUtils.addComponent(basicSearchBody, cancelBtn,
					Alignment.MIDDLE_CENTER);
			return basicSearchBody;
		}

		@Override
		protected SearchCriteria fillupSearchCriteria() {
			ActivitySearchPanel.this.searchCriteria = new ActivitySearchCriteria();
			ActivitySearchPanel.this.searchCriteria
					.setSaccountid(new NumberSearchField(SearchField.AND,
							AppContext.getAccountId()));
			return ActivitySearchPanel.this.searchCriteria;
		}
	}
}
