/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.ui.components;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.SearchCriteria;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.*;
import com.mycollab.vaadin.event.HasMassItemActionHandler;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.event.HasSelectableItemHandlers;
import com.mycollab.vaadin.event.HasSelectionOptionHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.mycollab.vaadin.web.ui.table.AbstractPagedGrid;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd.
 * @since 3.0
 */
// TODO
public abstract class AbstractListItemComp<S extends SearchCriteria, B> extends AbstractVerticalPageView implements IListView<S, B> {
    private static final long serialVersionUID = 1L;

    protected MVerticalLayout contentLayout;
    protected DefaultGenericSearchPanel<S> searchPanel;
    protected AbstractPagedGrid<S, B> grid;

    private Label selectedItemsNumberLabel = new Label();

    private SelectionOptionButton selectOptionButton;
    private DefaultMassItemActionHandlerContainer tableActionControls;
//    private ButtonGroup extraControlsLayout;

    public AbstractListItemComp() {
        this.withMargin(new MarginInfo(false, true, true, true));

        searchPanel = createSearchPanel();
        with(searchPanel);

        contentLayout = new MVerticalLayout().withSpacing(false).withMargin(false);
        with(contentLayout).expand(contentLayout);
        grid = createGrid();
        contentLayout.with(buildControlsLayout(), grid).expand(grid);
        grid.setHeightUndefined();
    }

    private ComponentContainer buildControlsLayout() {
        MHorizontalLayout viewControlsLayout = new MHorizontalLayout().withStyleName(WebThemes.TABLE_ACTION_CONTROLS).withFullWidth();

        selectOptionButton = new SelectionOptionButton(grid);
        selectOptionButton.setSizeUndefined();
        tableActionControls = createActionControls();

        MHorizontalLayout leftContainer = new MHorizontalLayout(selectOptionButton, tableActionControls, selectedItemsNumberLabel);
        leftContainer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

//        extraControlsLayout = new ButtonGroup();
        buildExtraControls();

//        viewControlsLayout.with(leftContainer, extraControlsLayout).withAlign(leftContainer, Alignment.MIDDLE_LEFT)
//                .withAlign(extraControlsLayout, Alignment.MIDDLE_RIGHT);
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
        selectedItemsNumberLabel.setValue(UserUIContext.getMessage(GenericI18Enum.TABLE_SELECTED_ITEM_TITLE, numOfSelectedItems));
    }

    public void addExtraButton(Button component) {
//        extraControlsLayout.addButton(component);
    }

    @Override
    public HasSelectionOptionHandlers getOptionSelectionHandlers() {
        return selectOptionButton;
    }

    @Override
    public AbstractPagedGrid<S, B> getPagedBeanGrid() {
        return grid;
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
        return grid;
    }

    abstract protected void buildExtraControls();

    abstract protected DefaultGenericSearchPanel<S> createSearchPanel();

    abstract protected AbstractPagedGrid<S, B> createGrid();

    abstract protected DefaultMassItemActionHandlerContainer createActionControls();
}
