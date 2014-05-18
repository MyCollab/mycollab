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
package com.esofthead.mycollab.module.crm.view.opportunity;

import java.util.Arrays;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.ApplicationEvent;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.crm.domain.SimpleOpportunity;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.events.AccountEvent;
import com.esofthead.mycollab.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.ui.components.AbstractListItemComp;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignImportWindow;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.MassItemActionHandler;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.DefaultMassItemActionHandlersContainer;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.TableClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class OpportunityListViewImpl extends
		AbstractListItemComp<OpportunitySearchCriteria, SimpleOpportunity>
		implements OpportunityListView {

	private static final long serialVersionUID = 1L;

	@Override
	protected void buildExtraControls() {
		Button customizeViewBtn = new Button("", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(
						new OpportunityListCustomizeWindow(
								OpportunityListView.VIEW_DEF_ID, tableItem));

			}
		});
		customizeViewBtn.setIcon(MyCollabResource
				.newResource("icons/16/customize.png"));
		customizeViewBtn.setDescription("Layout Options");
		customizeViewBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		this.addExtraComponent(customizeViewBtn);

		Button importBtn = new Button("", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(new CampaignImportWindow());
			}
		});
		importBtn.setDescription("Import");
		importBtn.setIcon(MyCollabResource.newResource("icons/16/import.png"));
		importBtn.addStyleName(UIConstants.THEME_BLUE_LINK);
		importBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_OPPORTUNITY));
		this.addExtraComponent(importBtn);

	}

	@Override
	protected DefaultGenericSearchPanel<OpportunitySearchCriteria> createSearchPanel() {
		return new OpportunitySearchPanel();
	}

	@Override
	protected AbstractPagedBeanTable<OpportunitySearchCriteria, SimpleOpportunity> createBeanTable() {
		OpportunityTableDisplay opportunityTableDisplay = new OpportunityTableDisplay(
				OpportunityListView.VIEW_DEF_ID,
				OpportunityTableFieldDef.selected, Arrays.asList(
						OpportunityTableFieldDef.opportunityName,
						OpportunityTableFieldDef.accountName,
						OpportunityTableFieldDef.saleStage,
						OpportunityTableFieldDef.amount,
						OpportunityTableFieldDef.expectedCloseDate,
						OpportunityTableFieldDef.assignUser));

		opportunityTableDisplay
				.addTableListener(new ApplicationEventListener<TableClickEvent>() {
					private static final long serialVersionUID = 1L;

					@Override
					public Class<? extends ApplicationEvent> getEventType() {
						return TableClickEvent.class;
					}

					@Override
					public void handle(final TableClickEvent event) {
						final SimpleOpportunity opportunity = (SimpleOpportunity) event
								.getData();
						if (event.getFieldName().equals("opportunityname")) {
							EventBus.getInstance().fireEvent(
									new OpportunityEvent.GotoRead(this,
											opportunity.getId()));
						} else if (event.getFieldName().equals("accountname")) {
							EventBus.getInstance().fireEvent(
									new AccountEvent.GotoRead(this, opportunity
											.getAccountid()));
						}
					}
				});

		return opportunityTableDisplay;
	}

	@Override
	protected DefaultMassItemActionHandlersContainer createActionControls() {
		DefaultMassItemActionHandlersContainer container = new DefaultMassItemActionHandlersContainer();

		if (AppContext.canAccess(RolePermissionCollections.CRM_OPPORTUNITY)) {
			container.addActionItem(MassItemActionHandler.DELETE_ACTION,
					MyCollabResource.newResource("icons/16/action/delete.png"),
					"delete",
					AppContext.getMessage(GenericI18Enum.BUTTON_DELETE_LABEL));
		}

		container.addActionItem(MassItemActionHandler.MAIL_ACTION,
				MyCollabResource.newResource("icons/16/action/mail.png"),
				"mail", AppContext.getMessage(GenericI18Enum.BUTTON_MAIL));

		container.addDownloadActionItem(
				MassItemActionHandler.EXPORT_PDF_ACTION,
				MyCollabResource.newResource("icons/16/action/pdf.png"),
				"export", "export.pdf",
				AppContext.getMessage(GenericI18Enum.BUTTON_EXPORT_PDF));

		container.addDownloadActionItem(
				MassItemActionHandler.EXPORT_EXCEL_ACTION,
				MyCollabResource.newResource("icons/16/action/excel.png"),
				"export", "export.xlsx",
				AppContext.getMessage(GenericI18Enum.BUTTON_EXPORT_EXCEL));

		container.addDownloadActionItem(
				MassItemActionHandler.EXPORT_CSV_ACTION,
				MyCollabResource.newResource("icons/16/action/csv.png"),
				"export", "export.csv",
				AppContext.getMessage(GenericI18Enum.BUTTON_EXPORT_CSV));

		if (AppContext.canWrite(RolePermissionCollections.CRM_OPPORTUNITY)) {
			container.addActionItem(MassItemActionHandler.MASS_UPDATE_ACTION,
					MyCollabResource
							.newResource("icons/16/action/massupdate.png"),
					"update", AppContext
							.getMessage(GenericI18Enum.BUTTON_MASSUPDATE));
		}

		return container;
	}
}
