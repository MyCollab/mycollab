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
package com.esofthead.mycollab.module.crm.view.cases;

import java.util.Arrays;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.events.AccountEvent;
import com.esofthead.mycollab.module.crm.events.CaseEvent;
import com.esofthead.mycollab.module.crm.ui.components.AbstractListItemComp;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.MassItemActionHandler;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.DefaultMassItemActionHandlersContainer;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

/**
 * 
 * @author MyCollab Ltd.
 * @since 2.0
 * 
 */
@ViewComponent
public class CaseListViewImpl extends
		AbstractListItemComp<CaseSearchCriteria, SimpleCase> implements
		CaseListView {

	private static final long serialVersionUID = 1L;

	@Override
	protected void buildExtraControls() {
		Button customizeViewBtn = new Button("", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().addWindow(
						new CaseListCustomizeWindow(CaseListView.VIEW_DEF_ID,
								tableItem));

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
				UI.getCurrent().addWindow(new CaseImportWindow());
			}
		});
		importBtn.setDescription("Import");
		importBtn.setIcon(MyCollabResource.newResource("icons/16/import.png"));
		importBtn.addStyleName(UIConstants.THEME_BLUE_LINK);
		importBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_CASE));

		this.addExtraComponent(importBtn);

	}

	@Override
	protected DefaultGenericSearchPanel<CaseSearchCriteria> createSearchPanel() {
		return new CaseSearchPanel();
	}

	@Override
	protected AbstractPagedBeanTable<CaseSearchCriteria, SimpleCase> createBeanTable() {
		CaseTableDisplay caseTableDisplay = new CaseTableDisplay(
				CaseListView.VIEW_DEF_ID, CaseTableFieldDef.selected,
				Arrays.asList(CaseTableFieldDef.subject,
						CaseTableFieldDef.account, CaseTableFieldDef.priority,
						CaseTableFieldDef.status, CaseTableFieldDef.assignUser,
						CaseTableFieldDef.createdTime));

		caseTableDisplay.addTableListener(new TableClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(final TableClickEvent event) {
				final SimpleCase cases = (SimpleCase) event.getData();
				if ("subject".equals(event.getFieldName())) {
					EventBusFactory.getInstance().post(
							new CaseEvent.GotoRead(this, cases.getId()));
				} else if ("accountName".equals(event.getFieldName())) {
					EventBusFactory.getInstance().post(
							new AccountEvent.GotoRead(this, cases
									.getAccountid()));
				}
			}
		});

		return caseTableDisplay;
	}

	@Override
	protected DefaultMassItemActionHandlersContainer createActionControls() {
		DefaultMassItemActionHandlersContainer container = new DefaultMassItemActionHandlersContainer();

		if (AppContext.canAccess(RolePermissionCollections.CRM_CASE)) {
			container.addActionItem(MassItemActionHandler.DELETE_ACTION,
					MyCollabResource
							.newResource(WebResourceIds._16_action_delete),
					"delete", AppContext
							.getMessage(GenericI18Enum.BUTTON_DELETE));
		}

		container.addActionItem(MassItemActionHandler.MAIL_ACTION,
				MyCollabResource.newResource(WebResourceIds._16_action_mail),
				"mail", AppContext.getMessage(GenericI18Enum.BUTTON_MAIL));

		container.addDownloadActionItem(
				MassItemActionHandler.EXPORT_PDF_ACTION,
				MyCollabResource.newResource(WebResourceIds._16_action_pdf),
				"export", "export.pdf",
				AppContext.getMessage(GenericI18Enum.BUTTON_EXPORT_PDF));

		container.addDownloadActionItem(
				MassItemActionHandler.EXPORT_EXCEL_ACTION,
				MyCollabResource.newResource(WebResourceIds._16_action_excel),
				"export", "export.xlsx",
				AppContext.getMessage(GenericI18Enum.BUTTON_EXPORT_EXCEL));

		container.addDownloadActionItem(
				MassItemActionHandler.EXPORT_CSV_ACTION,
				MyCollabResource.newResource(WebResourceIds._16_action_csv),
				"export", "export.csv",
				AppContext.getMessage(GenericI18Enum.BUTTON_EXPORT_CSV));

		if (AppContext.canWrite(RolePermissionCollections.CRM_CASE)) {
			container.addActionItem(MassItemActionHandler.MASS_UPDATE_ACTION,
					MyCollabResource
							.newResource(WebResourceIds._16_action_massupdate),
					"update", AppContext
							.getMessage(GenericI18Enum.TOOLTIP_MASS_UPDATE));
		}

		return container;
	}
}
