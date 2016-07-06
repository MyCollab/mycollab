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
package com.mycollab.module.crm.ui.components;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.web.ui.IListView;
import com.mycollab.vaadin.events.HasMassItemActionHandler;
import com.mycollab.vaadin.events.HasSearchHandlers;
import com.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.mycollab.vaadin.mvp.AbstractPageView;
import com.mycollab.vaadin.web.ui.DefaultGenericSearchPanel;
import com.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.mycollab.vaadin.web.ui.SelectionOptionButton;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import org.vaadin.peter.buttongroup.ButtonGroup;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
public abstract class AbstractListItemComp<S extends SearchCriteria, B> extends AbstractPageView implements IListView<S, B> {
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
        this.withMargin(new MarginInfo(false, true, true, true));

        searchPanel = createSearchPanel();
        with(searchPanel);

        contentLayout = new MVerticalLayout().withSpacing(false).withMargin(false);
        with(contentLayout).expand(contentLayout);
        tableItem = createBeanTable();
        contentLayout.with(buildControlsLayout(), tableItem).expand(tableItem);
        tableItem.setHeightUndefined();
    }

    private ComponentContainer buildControlsLayout() {
        MHorizontalLayout viewControlsLayout = new MHorizontalLayout().withFullWidth();
        viewControlsLayout.addStyleName(UIConstants.TABLE_ACTION_CONTROLS);

        selectOptionButton = new SelectionOptionButton(tableItem);
        selectOptionButton.setSizeUndefined();
        tableActionControls = createActionControls();

        MHorizontalLayout leftContainer = new MHorizontalLayout().with(selectOptionButton, tableActionControls,
                selectedItemsNumberLabel);
        leftContainer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

        extraControlsLayout = new ButtonGroup();
        buildExtraControls();

        viewControlsLayout.with(leftContainer, extraControlsLayout).withAlign(leftContainer, Alignment.MIDDLE_LEFT)
                .withAlign(extraControlsLayout, Alignment.MIDDLE_RIGHT);
        return viewControlsLayout;
    }

    @Override
    public void disableActionControls() {
        tableActionControls.setVisible(false);
        selectOptionButton.setSelectedCheckbox(false);
        selectedItemsNumberLabel.setValue("");
    }

    @Override
    public void enableActionControls(int numOfSelectedItems) {
        tableActionControls.setVisible(true);
        selectedItemsNumberLabel.setValue(AppContext.getMessage(GenericI18Enum.TABLE_SELECTED_ITEM_TITLE, numOfSelectedItems));
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
