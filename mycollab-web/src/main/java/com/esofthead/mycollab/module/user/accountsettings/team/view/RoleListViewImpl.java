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
package com.esofthead.mycollab.module.user.accountsettings.team.view;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.user.accountsettings.localization.RoleI18nEnum;
import com.esofthead.mycollab.module.user.domain.Role;
import com.esofthead.mycollab.module.user.domain.SimpleRole;
import com.esofthead.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.esofthead.mycollab.module.user.events.RoleEvent;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandler;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.esofthead.mycollab.vaadin.ui.SelectionOptionButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import java.util.Arrays;

/**
 *
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class RoleListViewImpl extends AbstractPageView implements RoleListView {
    private static final long serialVersionUID = 1L;

    private RoleSearchPanel searchPanel;
    private SelectionOptionButton selectOptionButton;
    private RoleTableDisplay tableItem;
    private VerticalLayout listLayout;
    private DefaultMassItemActionHandlerContainer tableActionControls;
    private Label selectedItemsNumberLabel = new Label();

    public RoleListViewImpl() {
        this.setMargin(new MarginInfo(false, true, false, true));

        this.searchPanel = new RoleSearchPanel();
        this.listLayout = new VerticalLayout();
        this.with(this.searchPanel, this.listLayout);

        this.generateDisplayTable();
    }

    private void generateDisplayTable() {
        this.tableItem = new RoleTableDisplay(new TableViewField(null,
                "selected", UIConstants.TABLE_CONTROL_WIDTH), Arrays.asList(
                new TableViewField(RoleI18nEnum.FORM_NAME, "rolename",
                        UIConstants.TABLE_EX_LABEL_WIDTH), new TableViewField(
                        GenericI18Enum.FORM_DESCRIPTION, "description",
                        UIConstants.TABLE_EX_LABEL_WIDTH)));

        this.tableItem.addTableListener(new TableClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void itemClick(final TableClickEvent event) {
                final Role role = (Role) event.getData();
                if ("rolename".equals(event.getFieldName())) {
                    EventBusFactory.getInstance().post(
                            new RoleEvent.GotoRead(RoleListViewImpl.this, role
                                    .getId()));
                }
            }
        });

        this.listLayout.addComponent(this.constructTableActionControls());
        this.listLayout.addComponent(this.tableItem);
    }

    @Override
    public HasSearchHandlers<RoleSearchCriteria> getSearchHandlers() {
        return this.searchPanel;
    }

    private ComponentContainer constructTableActionControls() {
        CssLayout layoutWrapper = new CssLayout();
        layoutWrapper.setWidth("100%");
        MHorizontalLayout layout = new MHorizontalLayout();
        layoutWrapper.addStyleName(UIConstants.TABLE_ACTION_CONTROLS);
        layoutWrapper.addComponent(layout);

        this.selectOptionButton = new SelectionOptionButton(this.tableItem);
        layout.addComponent(this.selectOptionButton);

        Button deleteBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_DELETE));
        deleteBtn.setEnabled(AppContext.canAccess(RolePermissionCollections.ACCOUNT_ROLE));

        this.tableActionControls = new DefaultMassItemActionHandlerContainer();
        if (AppContext.canAccess(RolePermissionCollections.ACCOUNT_ROLE)) {
            tableActionControls.addDeleteActionItem();
        }
        tableActionControls.addDownloadPdfActionItem();
        tableActionControls.addDownloadExcelActionItem();
        tableActionControls.addDownloadCsvActionItem();

        layout.with(tableActionControls, selectedItemsNumberLabel).withAlign(selectedItemsNumberLabel, Alignment.MIDDLE_LEFT);
        return layoutWrapper;
    }

    @Override
    public void enableActionControls(final int numOfSelectedItems) {
        this.tableActionControls.setVisible(true);
        this.selectedItemsNumberLabel.setValue(AppContext.getMessage(
                GenericI18Enum.TABLE_SELECTED_ITEM_TITLE, numOfSelectedItems));
    }

    @Override
    public void disableActionControls() {
        this.tableActionControls.setVisible(false);
        this.selectOptionButton.setSelectedCheckbox(false);
        this.selectedItemsNumberLabel.setValue("");
    }

    @Override
    public HasSelectionOptionHandlers getOptionSelectionHandlers() {
        return this.selectOptionButton;
    }

    @Override
    public HasMassItemActionHandler getPopupActionHandlers() {
        return this.tableActionControls;
    }

    @Override
    public HasSelectableItemHandlers<SimpleRole> getSelectableItemHandlers() {
        return this.tableItem;
    }

    @Override
    public AbstractPagedBeanTable<RoleSearchCriteria, SimpleRole> getPagedBeanTable() {
        return this.tableItem;
    }
}
