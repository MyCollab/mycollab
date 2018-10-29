/**
 * Copyright © MyCollab
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.project.view.settings;

import com.mycollab.common.GridFieldMeta;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.domain.SimpleProjectRole;
import com.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.mycollab.module.project.service.ProjectRoleService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasMassItemActionHandler;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.event.HasSelectableItemHandlers;
import com.mycollab.vaadin.event.HasSelectionOptionHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.mycollab.vaadin.web.ui.SelectionOptionButton;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.mycollab.vaadin.web.ui.table.AbstractPagedGrid;
import com.mycollab.vaadin.web.ui.table.DefaultPagedGrid;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
// TODO
@ViewComponent
public class ProjectRoleListViewImpl extends AbstractVerticalPageView implements ProjectRoleListView {
    private static final long serialVersionUID = 1L;

    private ProjectRoleSearchPanel searchPanel;
    private SelectionOptionButton selectOptionButton;
    private DefaultPagedGrid<ProjectRoleService, ProjectRoleSearchCriteria, SimpleProjectRole> tableItem;
    private VerticalLayout listLayout;
    private DefaultMassItemActionHandlerContainer tableActionControls;
    private Label selectedItemsNumberLabel = new Label();

    public ProjectRoleListViewImpl() {
        this.setMargin(new MarginInfo(false, true, true, true));
        searchPanel = new ProjectRoleSearchPanel();
        listLayout = new VerticalLayout();
        with(searchPanel, listLayout);

        this.generateDisplayTable();
    }

    private void generateDisplayTable() {
        tableItem = new DefaultPagedGrid<>(AppContextUtil.getSpringBean(ProjectRoleService.class),
                SimpleProjectRole.class, new GridFieldMeta(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH),
                Arrays.asList(new GridFieldMeta(GenericI18Enum.FORM_NAME, "rolename", WebUIConstants.TABLE_EX_LABEL_WIDTH),
                        new GridFieldMeta(GenericI18Enum.FORM_DESCRIPTION, "description", WebUIConstants.TABLE_EX_LABEL_WIDTH)));

//        gridItem.addGeneratedColumn("selected", (source, itemId, columnId) -> {
//            final SimpleProjectRole role = gridItem.getBeanByIndex(itemId);
//            CheckBoxDecor cb = new CheckBoxDecor("", role.isSelected());
//            cb.setImmediate(true);
//            cb.addValueChangeListener(valueChangeEvent -> gridItem.fireSelectItemEvent(role));
//            role.setExtraData(cb);
//            return cb;
//        });
//
//        gridItem.addGeneratedColumn("rolename", (source, itemId, columnId) -> {
//            ProjectRole role = gridItem.getBeanByIndex(itemId);
//            return new LabelLink(role.getRolename(),
//                    ProjectLinkGenerator.generateRolePreviewLink(role.getProjectid(), role.getId()));
//        });

        listLayout.addComponent(this.constructTableActionControls());
        listLayout.addComponent(this.tableItem);
    }

    @Override
    public void showNoItemView() {

    }

    @Override
    public HasSearchHandlers<ProjectRoleSearchCriteria> getSearchHandlers() {
        return this.searchPanel;
    }

    private ComponentContainer constructTableActionControls() {
        CssLayout layoutWrapper = new CssLayout();
        layoutWrapper.setWidth("100%");
        MHorizontalLayout layout = new MHorizontalLayout();
        layoutWrapper.addStyleName(WebThemes.TABLE_ACTION_CONTROLS);
        layoutWrapper.addComponent(layout);

        selectOptionButton = new SelectionOptionButton(this.tableItem);
        layout.addComponent(this.selectOptionButton);

        tableActionControls = new DefaultMassItemActionHandlerContainer();
        if (CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.ROLES)) {
            tableActionControls.addDeleteActionItem();
        }

        tableActionControls.addDownloadPdfActionItem();
        tableActionControls.addDownloadExcelActionItem();
        tableActionControls.addDownloadCsvActionItem();

        layout.with(this.tableActionControls, this.selectedItemsNumberLabel).withAlign(selectedItemsNumberLabel,
                Alignment.MIDDLE_LEFT);
        return layoutWrapper;
    }

    @Override
    public void enableActionControls(int numOfSelectedItems) {
        tableActionControls.setVisible(true);
        selectedItemsNumberLabel.setValue(UserUIContext.getMessage(GenericI18Enum.TABLE_SELECTED_ITEM_TITLE, numOfSelectedItems));
    }

    @Override
    public void disableActionControls() {
        tableActionControls.setVisible(false);
        selectOptionButton.setSelectedCheckbox(false);
        selectedItemsNumberLabel.setValue("");
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
    public HasSelectableItemHandlers<SimpleProjectRole> getSelectableItemHandlers() {
        return this.tableItem;
    }

    @Override
    public AbstractPagedGrid<ProjectRoleSearchCriteria, SimpleProjectRole> getPagedBeanGrid() {
        return this.tableItem;
    }
}
