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
package com.esofthead.mycollab.module.crm.view.lead;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.events.LeadEvent;
import com.esofthead.mycollab.module.crm.ui.components.AbstractListItemComp;
import com.esofthead.mycollab.module.crm.ui.components.ComponentUtils;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.esofthead.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.esofthead.mycollab.vaadin.web.ui.table.IPagedBeanTable.TableClickEvent;
import com.esofthead.mycollab.vaadin.web.ui.table.IPagedBeanTable.TableClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.vaadin.viritin.button.MButton;

import java.util.Arrays;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@ViewComponent
public class LeadListViewImpl extends AbstractListItemComp<LeadSearchCriteria, SimpleLead> implements LeadListView {
	private static final long serialVersionUID = 1L;

	@Override
	protected void buildExtraControls() {
		MButton customizeViewBtn = ComponentUtils.createCustomizeViewButton().withListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                UI.getCurrent().addWindow(
                        new LeadListCustomizeWindow(LeadListView.VIEW_DEF_ID, tableItem));
            }
        });
		this.addExtraButton(customizeViewBtn);

		MButton importBtn = ComponentUtils.createImportEntitiesButton().withListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                UI.getCurrent().addWindow(new LeadImportWindow());
            }
        });
		importBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_LEAD));

		this.addExtraButton(importBtn);

	}

	@Override
	protected DefaultGenericSearchPanel<LeadSearchCriteria> createSearchPanel() {
		return new LeadSearchPanel();
	}

	@Override
	protected AbstractPagedBeanTable<LeadSearchCriteria, SimpleLead> createBeanTable() {
		LeadTableDisplay leadTableDisplay = new LeadTableDisplay(
				LeadListView.VIEW_DEF_ID, LeadTableFieldDef.selected(),
				Arrays.asList(LeadTableFieldDef.name(), LeadTableFieldDef.status(),
						LeadTableFieldDef.accountName(),
						LeadTableFieldDef.phoneoffice(), LeadTableFieldDef.email(),
						LeadTableFieldDef.assignedUser()));

		leadTableDisplay.addTableListener(new TableClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(final TableClickEvent event) {
				final SimpleLead lead = (SimpleLead) event.getData();
				if ("leadName".equals(event.getFieldName())) {
					EventBusFactory.getInstance().post(
							new LeadEvent.GotoRead(LeadListViewImpl.this, lead
									.getId()));
				}
			}
		});

		return leadTableDisplay;
	}

	@Override
	protected DefaultMassItemActionHandlerContainer createActionControls() {
		DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();

		if (AppContext.canAccess(RolePermissionCollections.CRM_LEAD)) {
			container.addDeleteActionItem();
		}

		container.addMailActionItem();
		container.addDownloadPdfActionItem();
		container.addDownloadExcelActionItem();
		container.addDownloadCsvActionItem();

		if (AppContext.canWrite(RolePermissionCollections.CRM_LEAD)) {
			container.addMassUpdateActionItem();
		}

		return container;
	}
}
