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
package com.esofthead.mycollab.module.project.view.settings;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.ProjectRole;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectRoleService;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.*;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.*;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.DefaultPagedBeanTable;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.FontAwesome;
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
public class ProjectRoleListViewImpl extends AbstractPageView implements ProjectRoleListView {
    private static final long serialVersionUID = 1L;

    private ProjectRoleSearchPanel searchPanel;
    private SelectionOptionButton selectOptionButton;
    private DefaultPagedBeanTable<ProjectRoleService, ProjectRoleSearchCriteria, SimpleProjectRole> tableItem;
    private VerticalLayout listLayout;
    private DefaultMassItemActionHandlerContainer tableActionControls;
    private Label selectedItemsNumberLabel = new Label();

    public ProjectRoleListViewImpl() {
        this.setMargin(new MarginInfo(false, true, false, true));
        searchPanel = new ProjectRoleSearchPanel();
        listLayout = new VerticalLayout();
        with(searchPanel, listLayout);

        this.generateDisplayTable();
    }

    private void generateDisplayTable() {
        this.tableItem = new DefaultPagedBeanTable<>(
                ApplicationContextUtil.getSpringBean(ProjectRoleService.class),
                SimpleProjectRole.class,
                new TableViewField(null, "selected", UIConstants.TABLE_CONTROL_WIDTH),
                Arrays.asList(
                        new TableViewField(ProjectRoleI18nEnum.FORM_NAME,
                                "rolename", UIConstants.TABLE_EX_LABEL_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_DESCRIPTION,
                                "description", UIConstants.TABLE_EX_LABEL_WIDTH)));

        this.tableItem.addGeneratedColumn("selected", new Table.ColumnGenerator() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public Object generateCell(Table source, Object itemId, Object columnId) {
                        final SimpleProjectRole role = tableItem.getBeanByIndex(itemId);
                        CheckBoxDecor cb = new CheckBoxDecor("", role.isSelected());
                        cb.setImmediate(true);
                        cb.addValueChangeListener(new ValueChangeListener() {
                            private static final long serialVersionUID = 1L;

                            @Override
                            public void valueChange(Property.ValueChangeEvent event) {
                                tableItem.fireSelectItemEvent(role);
                            }
                        });

                        role.setExtraData(cb);
                        return cb;
                    }
                });

        this.tableItem.addGeneratedColumn("rolename", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public com.vaadin.ui.Component generateCell(Table source, Object itemId, Object columnId) {
                ProjectRole role = tableItem.getBeanByIndex(itemId);
                return new LabelLink(role.getRolename(),
                        ProjectLinkBuilder.generateRolePreviewFullLink(
                                role.getProjectid(), role.getId()));

            }
        });

        this.listLayout.addComponent(this.constructTableActionControls());
        this.listLayout.addComponent(this.tableItem);
    }

    @Override
    public HasSearchHandlers<ProjectRoleSearchCriteria> getSearchHandlers() {
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
        deleteBtn.setEnabled(CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.ROLES));

        this.tableActionControls = new DefaultMassItemActionHandlerContainer();
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
    public HasSelectableItemHandlers<SimpleProjectRole> getSelectableItemHandlers() {
        return this.tableItem;
    }

    @Override
    public AbstractPagedBeanTable<ProjectRoleSearchCriteria, SimpleProjectRole> getPagedBeanTable() {
        return this.tableItem;
    }
}
