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
package com.esofthead.mycollab.module.project.view;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.project.ProjectLinkBuilder;
import com.esofthead.mycollab.module.project.ProjectTooltipGenerator;
import com.esofthead.mycollab.module.project.domain.Project;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsUtil;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandler;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.esofthead.mycollab.vaadin.web.ui.CheckBoxDecor;
import com.esofthead.mycollab.vaadin.web.ui.LabelLink;
import com.esofthead.mycollab.vaadin.web.ui.SelectionOptionButton;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.esofthead.mycollab.vaadin.web.ui.table.IPagedBeanTable;
import com.hp.gagawa.java.elements.A;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;

import java.util.Arrays;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
@ViewComponent
public class ProjectListViewImpl extends AbstractPageView implements ProjectListView {
    private ProjectSearchPanel projectSearchPanel;
    private SelectionOptionButton selectOptionButton;
    private DefaultPagedBeanTable<ProjectService, ProjectSearchCriteria, SimpleProject> tableItem;
    private VerticalLayout bodyLayout;
    private DefaultMassItemActionHandlerContainer tableActionControls;
    private Label selectedItemsNumberLabel = new Label();

    public ProjectListViewImpl() {
        withMargin(true);
    }

    @Override
    public void initContent() {
        removeAllComponents();
        projectSearchPanel = new ProjectSearchPanel();
        with(projectSearchPanel);

        bodyLayout = new VerticalLayout();
        this.addComponent(bodyLayout);

        generateDisplayTable();
    }

    private void generateDisplayTable() {
        tableItem = new DefaultPagedBeanTable<>(
                AppContextUtil.getSpringBean(ProjectService.class),
                SimpleProject.class, VIEW_DEF_ID,
                ProjectTableFieldDef.selected(), Arrays.asList(ProjectTableFieldDef.projectName(),
                ProjectTableFieldDef.lead(), ProjectTableFieldDef.client(), ProjectTableFieldDef.startDate(),
                ProjectTableFieldDef.homePage(), ProjectTableFieldDef.status()));

        tableItem.addGeneratedColumn("selected", new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                final SimpleProject item = tableItem.getBeanByIndex(itemId);
                final CheckBoxDecor cb = new CheckBoxDecor("", item.isSelected());
                cb.setImmediate(true);
                cb.addValueChangeListener(new Property.ValueChangeListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        tableItem.fireSelectItemEvent(item);
                    }
                });
                item.setExtraData(cb);
                return cb;
            }
        });

        tableItem.addGeneratedColumn(Project.Field.name.name(), new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table table, Object itemId, Object columnId) {
                SimpleProject project = tableItem.getBeanByIndex(itemId);
                LabelLink b = new LabelLink(project.getName(), ProjectLinkBuilder.generateProjectFullLink(project.getId()));
                b.setDescription(ProjectTooltipGenerator.generateToolTipProject(AppContext.getUserLocale(), project,
                        AppContext.getSiteUrl(), AppContext.getUserTimeZone()));
                MHorizontalLayout layout = new MHorizontalLayout(ProjectAssetsUtil.buildProjectLogo(project
                        .getShortname(), project.getId(), project.getAvatarid(), 32), b)
                        .expand(b).alignAll(Alignment.MIDDLE_LEFT).withMargin(false).withFullHeight();
                return layout;
            }
        });

        tableItem.addGeneratedColumn(Project.Field.lead.name(), new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                SimpleProject project = tableItem.getBeanByIndex(itemId);
                return new Label(ProjectLinkBuilder.generateProjectMemberHtmlLink(project.getId(), project.getLead(),
                        project.getLeadFullName(), project.getLeadAvatarId(), true), ContentMode.HTML);
            }
        });

        tableItem.addGeneratedColumn(Project.Field.accountid.name(), new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                SimpleProject project = tableItem.getBeanByIndex(itemId);
                if (project.getAccountid() != null) {
                    LabelLink b = new LabelLink(project.getClientName(), ProjectLinkBuilder.generateClientPreviewFullLink
                            (project.getAccountid()));
                    b.setIconLink(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
                    return b;
                } else {
                    return new Label();
                }
            }
        });

        tableItem.addGeneratedColumn(Project.Field.planstartdate.name(), new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                SimpleProject project = tableItem.getBeanByIndex(itemId);
                return new Label(AppContext.formatDate(project.getPlanstartdate()));
            }
        });

        tableItem.addGeneratedColumn(Project.Field.planenddate.name(), new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                SimpleProject project = tableItem.getBeanByIndex(itemId);
                return new Label(AppContext.formatDate(project.getPlanenddate()));
            }
        });

        tableItem.addGeneratedColumn(Project.Field.createdtime.name(), new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                SimpleProject project = tableItem.getBeanByIndex(itemId);
                return new Label(AppContext.formatDate(project.getCreatedtime()));
            }
        });

        tableItem.addGeneratedColumn(Project.Field.homepage.name(), new Table.ColumnGenerator() {
            private static final long serialVersionUID = 1L;

            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                SimpleProject project = tableItem.getBeanByIndex(itemId);
                if (StringUtils.isNotBlank(project.getHomepage())) {
                    return new Label(new A(project.getHomepage(), "_blank").appendText(project.getHomepage()).write(), ContentMode.HTML);
                } else {
                    return new Label();
                }
            }
        });

        tableItem.setWidth("100%");

        bodyLayout.addComponent(constructTableActionControls());
        bodyLayout.addComponent(tableItem);
    }

    private ComponentContainer constructTableActionControls() {
        MHorizontalLayout layout = new MHorizontalLayout().withFullWidth();
        layout.addStyleName(UIConstants.TABLE_ACTION_CONTROLS);

        selectOptionButton = new SelectionOptionButton(tableItem);
        selectOptionButton.setWidthUndefined();
        layout.addComponent(selectOptionButton);

        tableActionControls = new DefaultMassItemActionHandlerContainer();

        tableActionControls.addDownloadPdfActionItem();
        tableActionControls.addDownloadExcelActionItem();
        tableActionControls.addDownloadCsvActionItem();

        tableActionControls.setVisible(false);
        tableActionControls.setWidthUndefined();

        layout.addComponent(tableActionControls);
        selectedItemsNumberLabel.setWidth("100%");
        layout.with(selectedItemsNumberLabel).withAlign(selectedItemsNumberLabel, Alignment.MIDDLE_CENTER).expand(selectedItemsNumberLabel);

        Button customizeViewBtn = new Button("", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                UI.getCurrent().addWindow(new ProjectListCustomizeWindow(VIEW_DEF_ID, tableItem));

            }
        });
        customizeViewBtn.setIcon(FontAwesome.ADJUST);
        customizeViewBtn.setDescription("Layout Options");
        customizeViewBtn.addStyleName(UIConstants.BUTTON_ACTION);
        layout.with(customizeViewBtn).withAlign(customizeViewBtn, Alignment.MIDDLE_RIGHT);

        return layout;
    }

    @Override
    public void showNoItemView() {

    }

    @Override
    public void enableActionControls(int numOfSelectedItems) {
        tableActionControls.setVisible(true);
        selectedItemsNumberLabel.setValue(AppContext.getMessage(GenericI18Enum.TABLE_SELECTED_ITEM_TITLE, numOfSelectedItems));
    }

    @Override
    public void disableActionControls() {
        tableActionControls.setVisible(false);
        selectOptionButton.setSelectedCheckbox(false);
        selectedItemsNumberLabel.setValue("");
    }

    @Override
    public HasSearchHandlers<ProjectSearchCriteria> getSearchHandlers() {
        return projectSearchPanel;
    }

    @Override
    public HasSelectionOptionHandlers getOptionSelectionHandlers() {
        return selectOptionButton;
    }

    @Override
    public HasMassItemActionHandler getPopupActionHandlers() {
        return tableActionControls;
    }

    @Override
    public HasSelectableItemHandlers<SimpleProject> getSelectableItemHandlers() {
        return tableItem;
    }

    @Override
    public IPagedBeanTable<ProjectSearchCriteria, SimpleProject> getPagedBeanTable() {
        return tableItem;
    }
}
