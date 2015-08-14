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

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.domain.SimpleCase;
import com.esofthead.mycollab.module.crm.domain.criteria.CaseSearchCriteria;
import com.esofthead.mycollab.module.crm.events.AccountEvent;
import com.esofthead.mycollab.module.crm.events.CaseEvent;
import com.esofthead.mycollab.module.crm.ui.components.AbstractListItemComp;
import com.esofthead.mycollab.module.crm.ui.components.ComponentUtils;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
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
public class CaseListViewImpl extends AbstractListItemComp<CaseSearchCriteria, SimpleCase> implements CaseListView {
	private static final long serialVersionUID = 1L;

	@Override
	protected void buildExtraControls() {
		MButton customizeViewBtn = ComponentUtils.createCustomizeViewButton().withListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                UI.getCurrent().addWindow(
                        new CaseListCustomizeWindow(CaseListView.VIEW_DEF_ID,
                                tableItem));
            }
        });
		this.addExtraButton(customizeViewBtn);

		MButton importBtn = ComponentUtils.createImportEntitiesButton().withListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                UI.getCurrent().addWindow(new CaseImportWindow());
            }
        });
		importBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_CASE));

		this.addExtraButton(importBtn);

	}

	@Override
	protected DefaultGenericSearchPanel<CaseSearchCriteria> createSearchPanel() {
		return new CaseSearchPanel();
	}

	@Override
	protected AbstractPagedBeanTable<CaseSearchCriteria, SimpleCase> createBeanTable() {
		CaseTableDisplay caseTableDisplay = new CaseTableDisplay(
				CaseListView.VIEW_DEF_ID, CaseTableFieldDef.selected(),
				Arrays.asList(CaseTableFieldDef.subject(),
						CaseTableFieldDef.account(), CaseTableFieldDef.priority(),
						CaseTableFieldDef.status(), CaseTableFieldDef.assignUser(),
						CaseTableFieldDef.createdTime()));

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
	protected DefaultMassItemActionHandlerContainer createActionControls() {
		DefaultMassItemActionHandlerContainer container = new DefaultMassItemActionHandlerContainer();

		if (AppContext.canAccess(RolePermissionCollections.CRM_CASE)) {
			container.addDeleteActionItem();
		}

		container.addMailActionItem();
		container.addDownloadPdfActionItem();
		container.addDownloadExcelActionItem();
		container.addDownloadCsvActionItem();

		if (AppContext.canWrite(RolePermissionCollections.CRM_CASE)) {
			container.addMassUpdateActionItem();
		}

		return container;
	}
}
