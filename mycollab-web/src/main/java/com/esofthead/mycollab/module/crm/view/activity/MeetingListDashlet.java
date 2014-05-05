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

import java.util.Arrays;

import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.module.crm.domain.criteria.MeetingSearchCriteria;
import com.esofthead.mycollab.module.crm.localization.CrmCommonI18nEnum;
import com.esofthead.mycollab.module.crm.localization.TaskI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.TableViewField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 */
@SuppressWarnings("serial")
public class MeetingListDashlet extends Depot {

	private MeetingTableDisplay tableItem;

	public MeetingListDashlet() {
		super("My Meetings", new VerticalLayout());

		tableItem = new MeetingTableDisplay(Arrays.asList(
				new TableViewField(AppContext
						.getMessage(TaskI18nEnum.TABLE_SUBJECT_HEADER),
						"subject", UIConstants.TABLE_X_LABEL_WIDTH),
						new TableViewField(AppContext
								.getMessage(TaskI18nEnum.TABLE_START_DATE_HEADER),
								"startdate", UIConstants.TABLE_DATE_TIME_WIDTH),
								new TableViewField(AppContext
										.getMessage(CrmCommonI18nEnum.TABLE_STATUS_HEADER),
										"status", UIConstants.TABLE_S_LABEL_WIDTH)));

		bodyContent.addComponent(tableItem);

		Button customizeViewBtn = new Button("", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		customizeViewBtn.setIcon(MyCollabResource
				.newResource("icons/16/customize_black.png"));
		customizeViewBtn.setDescription("Layout Options");
		customizeViewBtn.setStyleName(UIConstants.THEME_BLANK_LINK);

		this.addHeaderElement(customizeViewBtn);
	}

	public void display() {
		final MeetingSearchCriteria criteria = new MeetingSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setAssignUsers(new SetSearchField<String>(
				new String[] { AppContext.getUsername() }));
		tableItem.setSearchCriteria(criteria);
	}
}
