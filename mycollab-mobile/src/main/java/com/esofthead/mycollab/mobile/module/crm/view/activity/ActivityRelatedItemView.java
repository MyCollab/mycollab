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

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.mobile.module.crm.ui.AbstractRelatedListView;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.SimpleActivity;
import com.esofthead.mycollab.module.crm.domain.criteria.ActivitySearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CallI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.MeetingI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.TaskI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.Popover;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class ActivityRelatedItemView extends
		AbstractRelatedListView<SimpleActivity, ActivitySearchCriteria> {
	private static final long serialVersionUID = 955474758141391716L;
	private VerticalLayout addButtons;
	private final String type;
	private Integer beanId;

	public ActivityRelatedItemView(String type) {
		this.type = type;
		initUI();
	}

	public void displayActivity(Integer id) {
		this.beanId = id;
		loadActivities();
	}

	private void loadActivities() {
		ActivitySearchCriteria criteria = new ActivitySearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setType(new StringSearchField(SearchField.AND, this.type));
		criteria.setTypeid(new NumberSearchField(this.beanId));
		this.itemList.setSearchCriteria(criteria);
	}

	private void initUI() {
		this.setCaption(AppContext
				.getMessage(ActivityI18nEnum.M_TITLE_RELATED_ACTIVITIES));
		itemList = new ActivityListDisplay();
		this.setContent(itemList);
	}

	@Override
	public void refresh() {
		loadActivities();
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

		NavigationButton addTask = new NavigationButton(
				AppContext.getMessage(TaskI18nEnum.BUTTON_NEW_TASK));
		addTask.addClickListener(new NavigationButton.NavigationButtonClickListener() {
			private static final long serialVersionUID = 1920289198458066344L;

			@Override
			public void buttonClick(
					NavigationButton.NavigationButtonClickEvent event) {
				controlBtns.close();
				fireNewRelatedItem(CrmTypeConstants.TASK);
			}
		});
		addButtons.addComponent(addTask);

		NavigationButton addCall = new NavigationButton(
				AppContext.getMessage(CallI18nEnum.BUTTON_NEW_CALL));
		addCall.addClickListener(new NavigationButton.NavigationButtonClickListener() {
			private static final long serialVersionUID = -279151189261011902L;

			@Override
			public void buttonClick(
					NavigationButton.NavigationButtonClickEvent event) {
				controlBtns.close();
				fireNewRelatedItem(CrmTypeConstants.CALL);
			}
		});
		addButtons.addComponent(addCall);

		NavigationButton addMeeting = new NavigationButton(
				AppContext.getMessage(MeetingI18nEnum.BUTTON_NEW_MEETING));
		addMeeting
				.addClickListener(new NavigationButton.NavigationButtonClickListener() {
					private static final long serialVersionUID = 4770664404728700960L;

					@Override
					public void buttonClick(
							NavigationButton.NavigationButtonClickEvent event) {
						controlBtns.close();
						fireNewRelatedItem(CrmTypeConstants.MEETING);
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
