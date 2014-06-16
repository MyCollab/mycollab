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

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CrmCommonI18nEnum;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.desktop.ui.ListView;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandlers;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.ui.DefaultMassItemActionHandlersContainer;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.SelectionOptionButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 3.0
 * 
 */
public abstract class AbstractListItemComp<S extends SearchCriteria, B> extends
		AbstractPageView implements ListView<S, B> {
	private static final long serialVersionUID = 1L;

	protected VerticalLayout contentLayout;
	protected GenericSearchPanel<S> searchPanel;
	protected AbstractPagedBeanTable<S, B> tableItem;

	protected Label selectedItemsNumberLabel = new Label();

	protected SelectionOptionButton selectOptionButton;
	protected DefaultMassItemActionHandlersContainer tableActionControls;
	protected HorizontalLayout extraControlsLayout;

	public AbstractListItemComp() {
		super();
		this.setMargin(new MarginInfo(false, true, true, true));

		this.searchPanel = createSearchPanel();
		this.addComponent(this.searchPanel);

		this.contentLayout = new VerticalLayout();
		this.addComponent(this.contentLayout);

		this.tableItem = createBeanTable();
		buildLayout();
	}

	private void buildLayout() {
		final CssLayout layoutWrapper = new CssLayout();
		layoutWrapper.setWidth("100%");
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setSpacing(true);
		layout.setWidth("100%");
		layoutWrapper.addStyleName(UIConstants.TABLE_ACTION_CONTROLS);
		layoutWrapper.addComponent(layout);

		this.selectOptionButton = new SelectionOptionButton(this.tableItem);
		this.selectOptionButton.setSizeUndefined();
		layout.addComponent(this.selectOptionButton);

		final Button deleteBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_DELETE_LABEL));
		deleteBtn.setEnabled(AppContext
				.canAccess(RolePermissionCollections.CRM_ACCOUNT));

		this.tableActionControls = createActionControls();

		layout.addComponent(this.tableActionControls);
		layout.addComponent(this.selectedItemsNumberLabel);
		layout.setComponentAlignment(this.selectedItemsNumberLabel,
				Alignment.MIDDLE_LEFT);

		layout.setExpandRatio(this.selectedItemsNumberLabel, 1.0f);

		extraControlsLayout = new HorizontalLayout();
		extraControlsLayout.setSpacing(true);
		layout.addComponent(extraControlsLayout);
		extraControlsLayout.addStyleName(UIConstants.THEME_SMALL_PADDING);
		layout.setComponentAlignment(this.extraControlsLayout,
				Alignment.MIDDLE_RIGHT);

		contentLayout.addComponent(layoutWrapper);
		contentLayout.addComponent(this.tableItem);

		buildExtraControls();
		layout.addComponent(extraControlsLayout);
	}

	@Override
	public void disableActionControls() {
		this.tableActionControls.setVisible(false);
		this.selectOptionButton.setSelectedChecbox(false);
		this.selectedItemsNumberLabel.setValue("");
	}

	@Override
	public void enableActionControls(final int numOfSelectedItems) {
		this.tableActionControls.setVisible(true);
		this.selectedItemsNumberLabel.setValue(AppContext
				.getMessage(CrmCommonI18nEnum.TABLE_SELECTED_ITEM_TITLE,
						numOfSelectedItems));
	}

	public void addExtraComponent(Component component) {
		extraControlsLayout.addComponent(component);
	}

	@Override
	public HasSelectionOptionHandlers getOptionSelectionHandlers() {
		return this.selectOptionButton;
	}

	@Override
	public AbstractPagedBeanTable<S, B> getPagedBeanTable() {
		return this.tableItem;
	}

	@Override
	public HasMassItemActionHandlers getPopupActionHandlers() {
		return tableActionControls;
	}

	@Override
	public HasSearchHandlers<S> getSearchHandlers() {
		return this.searchPanel;
	}

	@Override
	public HasSelectableItemHandlers<B> getSelectableItemHandlers() {
		return this.tableItem;
	}

	abstract protected void buildExtraControls();

	abstract protected GenericSearchPanel<S> createSearchPanel();

	abstract protected AbstractPagedBeanTable<S, B> createBeanTable();

	abstract protected DefaultMassItemActionHandlersContainer createActionControls();
}
