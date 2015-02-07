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

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.core.arguments.BitSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.module.crm.domain.SimpleCall;
import com.esofthead.mycollab.module.crm.domain.criteria.CallSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.ActivityI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.CallI18nEnum;
import com.esofthead.mycollab.module.crm.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.crm.service.CallService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.VerticalLayout;

import java.util.Arrays;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@SuppressWarnings("serial")
public class CallListDashlet extends Depot {

	private CallTableDisplay tableItem;

	public CallListDashlet() {
		super("My Calls", new VerticalLayout());

		tableItem = new CallTableDisplay(new TableViewField(null, "isClosed",
				UIConstants.TABLE_CONTROL_WIDTH), Arrays.asList(
				new TableViewField(CallI18nEnum.FORM_SUBJECT, "subject",
						UIConstants.TABLE_X_LABEL_WIDTH), new TableViewField(
						TaskI18nEnum.FORM_START_DATE, "startdate",
						UIConstants.TABLE_DATE_TIME_WIDTH), new TableViewField(
						ActivityI18nEnum.FORM_STATUS, "status",
						UIConstants.TABLE_S_LABEL_WIDTH)));

		tableItem
				.addTableListener(new TableClickListener() {

					@Override
					public void itemClick(final TableClickEvent event) {
						final SimpleCall call = (SimpleCall) event.getData();
						if ("isClosed".equals(event.getFieldName())) {
							call.setIsclosed(true);
							final CallService callService = ApplicationContextUtil
									.getSpringBean(CallService.class);
							callService.updateWithSession(call,
									AppContext.getUsername());
							display();
						}
					}
				});
		bodyContent.addComponent(tableItem);

		Button customizeViewBtn = new Button("", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {

			}
		});
		customizeViewBtn.setIcon(FontAwesome.ADJUST);
		customizeViewBtn.setDescription("Layout Options");
		customizeViewBtn.setStyleName(UIConstants.BUTTON_ICON_ONLY);

		this.addHeaderElement(customizeViewBtn);
	}

	public void display() {
		final CallSearchCriteria criteria = new CallSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
		criteria.setAssignUsers(new SetSearchField<>(
				new String[] { AppContext.getUsername() }));
		criteria.setIsClosed(BitSearchField.FALSE);
		tableItem.setSearchCriteria(criteria);
	}
}
