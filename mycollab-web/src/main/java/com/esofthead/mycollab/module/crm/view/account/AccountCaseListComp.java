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

package com.esofthead.mycollab.module.crm.view.account;

import java.util.Arrays;

import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.events.CaseEvent;
import com.esofthead.mycollab.module.crm.ui.components.RelatedListComp;
import com.esofthead.mycollab.module.crm.view.cases.CaseTableDisplay;
import com.esofthead.mycollab.module.crm.view.cases.CaseTableFieldDef;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.TableClickEvent;
import com.vaadin.ui.Button;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class AccountCaseListComp extends
		RelatedListComp<SimpleCase, CaseSearchCriteria> {

	private static final long serialVersionUID = 1L;

	public AccountCaseListComp() {
		initUI();
	}

	private void initUI() {
		final Button newBtn = new Button("New Case",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final Button.ClickEvent event) {
						fireNewRelatedItem("");
					}
				});
		newBtn.setIcon(MyCollabResource.newResource("icons/16/addRecord.png"));
		newBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		newBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_CASE));

		this.addComponent(newBtn);

		tableItem = new CaseTableDisplay(Arrays.asList(
				CaseTableFieldDef.subject, CaseTableFieldDef.priority,
				CaseTableFieldDef.status, CaseTableFieldDef.assignUser,
				CaseTableFieldDef.createdTime));

		tableItem
				.addTableListener(new ApplicationEventListener<TableClickEvent>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return TableClickEvent.class;
					}

					@Override
					public void handle(final TableClickEvent event) {
						final SimpleCase cases = (SimpleCase) event.getData();
						if ("subject".equals(event.getFieldName())) {
							EventBus.getInstance()
									.fireEvent(
											new CaseEvent.GotoRead(this, cases
													.getId()));
						}
					}
				});

		this.addComponent(tableItem);
	}

	@Override
	public void refresh() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
