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
package com.mycollab.module.project.view.settings;

import com.mycollab.common.TableViewField;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTooltipGenerator;
import com.mycollab.module.project.i18n.ComponentI18nEnum;
import com.mycollab.module.project.view.settings.component.ProjectUserLink;
import com.mycollab.module.tracker.domain.SimpleComponent;
import com.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.mycollab.module.tracker.service.ComponentService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasMassItemActionHandler;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.event.HasSelectableItemHandlers;
import com.mycollab.vaadin.event.HasSelectionOptionHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.*;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ComponentListViewImpl extends AbstractVerticalPageView implements ComponentListView {
    private static final long serialVersionUID = 1L;

    private ComponentSearchPanel componentSearchPanel;
    private SelectionOptionButton selectOptionButton;
    private DefaultPagedBeanTable<ComponentService, ComponentSearchCriteria, SimpleComponent> tableItem;
    private VerticalLayout componentListLayout;
    private DefaultMassItemActionHandlerContainer tableActionControls;
    private Label selectedItemsNumberLabel = new Label();

    public ComponentListViewImpl() {
        this.setMargin(new MarginInfo(false, true, true, true));

        this.componentSearchPanel = new ComponentSearchPanel();
        this.addComponent(this.componentSearchPanel);

        componentListLayout = new VerticalLayout();
        this.addComponent(componentListLayout);

        this.generateDisplayTable();
    }

    private void generateDisplayTable() {
        tableItem = new DefaultPagedBeanTable<>(AppContextUtil.getSpringBean(ComponentService.class),
                SimpleComponent.class, new TableViewField(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH),
                Arrays.asList(
                        new TableViewField(GenericI18Enum.FORM_NAME, "name", WebUIConstants.TABLE_EX_LABEL_WIDTH),
                        new TableViewField(ComponentI18nEnum.FORM_LEAD, "userLeadFullName", WebUIConstants.TABLE_X_LABEL_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_STATUS, "status", WebUIConstants.TABLE_M_LABEL_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_DESCRIPTION, "description", 500),
                        new TableViewField(GenericI18Enum.FORM_PROGRESS, "id", WebUIConstants.TABLE_M_LABEL_WIDTH)));

        tableItem.addGeneratedColumn("selected", (source, itemId, columnId) -> {
            final SimpleComponent component = tableItem.getBeanByIndex(itemId);
            CheckBoxDecor cb = new CheckBoxDecor("", component.isSelected());
            cb.setImmediate(true);
            cb.addValueChangeListener(valueChangeEvent -> tableItem.fireSelectItemEvent(component));
            component.setExtraData(cb);
            return cb;
        });

        tableItem.addGeneratedColumn("name", (source, itemId, columnId) -> {
            SimpleComponent bugComponent = tableItem.getBeanByIndex(itemId);
            LabelLink b = new LabelLink(bugComponent.getName(), ProjectLinkBuilder
                    .generateComponentPreviewFullLink(bugComponent.getProjectid(), bugComponent.getId()));
            if (bugComponent.getStatus() != null && bugComponent.getStatus().equals(StatusI18nEnum.Closed.name())) {
                b.addStyleName(WebThemes.LINK_COMPLETED);
            }
            b.setDescription(ProjectTooltipGenerator.generateToolTipComponent(UserUIContext.getUserLocale(),
                    bugComponent, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
            return b;
        });

        tableItem.addGeneratedColumn("userLeadFullName", (source, itemId, columnId) -> {
            SimpleComponent component = tableItem.getBeanByIndex(itemId);
            return new ProjectUserLink(component.getProjectid(), component.getUserlead(),
                    component.getUserLeadAvatarId(), component.getUserLeadFullName());
        });

        tableItem.addGeneratedColumn("id", (source, itemId, columnId) -> {
            SimpleComponent bugComponent = tableItem.getBeanByIndex(itemId);
            return new ProgressBarIndicator(bugComponent.getNumBugs(), bugComponent.getNumOpenBugs(), false);
        });

        tableItem.addGeneratedColumn("status", (source, itemId, columnId) -> {
            SimpleComponent bugComponent = tableItem.getBeanByIndex(itemId);
            return ELabel.i18n(bugComponent.getStatus(), StatusI18nEnum.class);
        });

        tableItem.addGeneratedColumn("description", (source, itemId, columnId) -> {
            SimpleComponent version = tableItem.getBeanByIndex(itemId);
            return ELabel.richText(version.getDescription());
        });

        tableItem.setWidth("100%");
        componentListLayout.addComponent(constructTableActionControls());
        componentListLayout.addComponent(tableItem);
    }

    @Override
    public HasSearchHandlers<ComponentSearchCriteria> getSearchHandlers() {
        return this.componentSearchPanel;
    }

    private ComponentContainer constructTableActionControls() {
        final CssLayout layoutWrapper = new CssLayout();
        layoutWrapper.setWidth("100%");

        MHorizontalLayout layout = new MHorizontalLayout();
        layoutWrapper.addStyleName(WebThemes.TABLE_ACTION_CONTROLS);
        layoutWrapper.addComponent(layout);

        this.selectOptionButton = new SelectionOptionButton(tableItem);
        layout.addComponent(this.selectOptionButton);

        tableActionControls = new DefaultMassItemActionHandlerContainer();
        if (CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.COMPONENTS)) {
            tableActionControls.addDeleteActionItem();
        }

        tableActionControls.addMailActionItem();
        tableActionControls.addDownloadPdfActionItem();
        tableActionControls.addDownloadExcelActionItem();
        tableActionControls.addDownloadCsvActionItem();

        layout.with(tableActionControls, selectedItemsNumberLabel)
                .withAlign(selectedItemsNumberLabel, Alignment.MIDDLE_CENTER);
        return layoutWrapper;
    }

    @Override
    public void showNoItemView() {
        removeAllComponents();
        this.addComponent(new ComponentListNoItemView());
    }

    @Override
    public void enableActionControls(final int numOfSelectedItems) {
        tableActionControls.setVisible(true);
        this.selectedItemsNumberLabel.setValue(UserUIContext.getMessage(
                GenericI18Enum.TABLE_SELECTED_ITEM_TITLE, numOfSelectedItems));
    }

    @Override
    public void disableActionControls() {
        tableActionControls.setVisible(false);
        this.selectOptionButton.setSelectedCheckbox(false);
        this.selectedItemsNumberLabel.setValue("");
    }

    @Override
    public HasSelectionOptionHandlers getOptionSelectionHandlers() {
        return this.selectOptionButton;
    }

    @Override
    public HasMassItemActionHandler getPopupActionHandlers() {
        return tableActionControls;
    }

    @Override
    public HasSelectableItemHandlers<SimpleComponent> getSelectableItemHandlers() {
        return tableItem;
    }

    @Override
    public AbstractPagedBeanTable<ComponentSearchCriteria, SimpleComponent> getPagedBeanTable() {
        return tableItem;
    }
}
