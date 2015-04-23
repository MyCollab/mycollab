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
package com.esofthead.mycollab.module.crm.ui.components;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.desktop.ui.ListView;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandler;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.esofthead.mycollab.vaadin.ui.SelectionOptionButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.peter.buttongroup.ButtonGroup;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractListItemComp<S extends SearchCriteria, B> extends
        AbstractPageView implements ListView<S, B> {
    private static final long serialVersionUID = 1L;

    protected MVerticalLayout contentLayout;
    protected DefaultGenericSearchPanel<S> searchPanel;
    protected AbstractPagedBeanTable<S, B> tableItem;

    protected Label selectedItemsNumberLabel = new Label();

    protected SelectionOptionButton selectOptionButton;
    protected DefaultMassItemActionHandlerContainer tableActionControls;
    protected ButtonGroup extraControlsLayout;

    public AbstractListItemComp() {
        super();
        this.setMargin(new MarginInfo(false, true, true, true));

        searchPanel = createSearchPanel();
        addComponent(searchPanel);

        contentLayout = new MVerticalLayout().withSpacing(false).withMargin(false);
        addComponent(contentLayout);

        this.tableItem = createBeanTable();
        buildLayout();
    }

    private void buildLayout() {
        CssLayout layoutWrapper = new CssLayout();
        layoutWrapper.setWidth("100%");

        MHorizontalLayout layout = new MHorizontalLayout().withWidth("100%");

        layoutWrapper.addStyleName(UIConstants.TABLE_ACTION_CONTROLS);
        layoutWrapper.addComponent(layout);

        selectOptionButton = new SelectionOptionButton(tableItem);
        selectOptionButton.setSizeUndefined();
        layout.addComponent(selectOptionButton);

        Button deleteBtn = new Button(
                AppContext.getMessage(GenericI18Enum.BUTTON_DELETE));
        deleteBtn.setEnabled(AppContext
                .canAccess(RolePermissionCollections.CRM_ACCOUNT));

        tableActionControls = createActionControls();

        layout.with(tableActionControls, selectedItemsNumberLabel).withAlign(selectedItemsNumberLabel,
                Alignment.MIDDLE_LEFT).expand(selectedItemsNumberLabel);

        contentLayout.with(layoutWrapper, tableItem);

        extraControlsLayout = new ButtonGroup();
        buildExtraControls();

        layout.with(extraControlsLayout).withAlign(extraControlsLayout, Alignment.MIDDLE_RIGHT);
    }

    @Override
    public void disableActionControls() {
        tableActionControls.setVisible(false);
        selectOptionButton.setSelectedCheckbox(false);
        selectedItemsNumberLabel.setValue("");
    }

    @Override
    public void enableActionControls(final int numOfSelectedItems) {
        tableActionControls.setVisible(true);
        selectedItemsNumberLabel.setValue(AppContext
                .getMessage(GenericI18Enum.TABLE_SELECTED_ITEM_TITLE,
                        numOfSelectedItems));
    }

    public void addExtraButton(Button component) {
        extraControlsLayout.addButton(component);
    }

    @Override
    public HasSelectionOptionHandlers getOptionSelectionHandlers() {
        return selectOptionButton;
    }

    @Override
    public AbstractPagedBeanTable<S, B> getPagedBeanTable() {
        return tableItem;
    }

    @Override
    public HasMassItemActionHandler getPopupActionHandlers() {
        return tableActionControls;
    }

    @Override
    public HasSearchHandlers<S> getSearchHandlers() {
        return searchPanel;
    }

    @Override
    public HasSelectableItemHandlers<B> getSelectableItemHandlers() {
        return tableItem;
    }

    abstract protected void buildExtraControls();

    abstract protected DefaultGenericSearchPanel<S> createSearchPanel();

    abstract protected AbstractPagedBeanTable<S, B> createBeanTable();

    abstract protected DefaultMassItemActionHandlerContainer createActionControls();
}
