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
public abstract class AbstractListItemComp<S extends SearchCriteria, B> extends AbstractVerticalPageView implements IListView<S, B> {
    private static final long serialVersionUID = 1L;

    protected MVerticalLayout contentLayout;
    protected DefaultGenericSearchPanel<S> searchPanel;
    protected AbstractPagedBeanTable<S, B> tableItem;

    private Label selectedItemsNumberLabel = new Label();

    private SelectionOptionButton selectOptionButton;
    private DefaultMassItemActionHandlerContainer tableActionControls;
    private ButtonGroup extraControlsLayout;

    public AbstractListItemComp() {
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
        MHorizontalLayout viewControlsLayout = new MHorizontalLayout().withStyleName(WebThemes.TABLE_ACTION_CONTROLS).withFullWidth();

        selectOptionButton = new SelectionOptionButton(tableItem);
        selectOptionButton.setSizeUndefined();
        tableActionControls = createActionControls();

        MHorizontalLayout leftContainer = new MHorizontalLayout(selectOptionButton, tableActionControls, selectedItemsNumberLabel);
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
        selectedItemsNumberLabel.setValue(UserUIContext.getMessage(GenericI18Enum.TABLE_SELECTED_ITEM_TITLE, numOfSelectedItems));
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
