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
package com.mycollab.module.project.view;

import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Br;
import com.hp.gagawa.java.elements.Div;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.StringUtils;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectTypeConstants;
import com.mycollab.module.project.domain.Project;
import com.mycollab.module.project.domain.SimpleProject;
import com.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.mycollab.module.project.fielddef.ProjectTableFieldDef;
import com.mycollab.module.project.service.ProjectService;
import com.mycollab.module.project.ui.ProjectAssetsManager;
import com.mycollab.module.project.ui.ProjectAssetsUtil;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.TooltipHelper;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.event.HasMassItemActionHandler;
import com.mycollab.vaadin.event.HasSearchHandlers;
import com.mycollab.vaadin.event.HasSelectableItemHandlers;
import com.mycollab.vaadin.event.HasSelectionOptionHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.ui.DefaultMassItemActionHandlerContainer;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.CheckBoxDecor;
import com.mycollab.vaadin.web.ui.LabelLink;
import com.mycollab.vaadin.web.ui.SelectionOptionButton;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.table.DefaultPagedBeanTable;
import com.mycollab.vaadin.web.ui.table.IPagedTable;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.util.Arrays;

import static com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;

/**
 * @author MyCollab Ltd
 * @since 5.2.12
 */
@ViewComponent
public class ProjectListViewImpl extends AbstractVerticalPageView implements ProjectListView {
    private ProjectSearchPanel projectSearchPanel;
    private SelectionOptionButton selectOptionButton;
    private DefaultPagedBeanTable<ProjectService, ProjectSearchCriteria, SimpleProject> tableItem;
    private MVerticalLayout bodyLayout;
    private DefaultMassItemActionHandlerContainer tableActionControls;
    private Label selectedItemsNumberLabel = new Label();

    public ProjectListViewImpl() {
        withMargin(true);
    }

    @Override
    public void initContent() {
        removeAllComponents();
        projectSearchPanel = new ProjectSearchPanel();

        bodyLayout = new MVerticalLayout().withSpacing(false).withMargin(false);
        this.with(projectSearchPanel, bodyLayout).expand(bodyLayout);

        generateDisplayTable();
    }

    private void generateDisplayTable() {
        tableItem = new DefaultPagedBeanTable<>(AppContextUtil.getSpringBean(ProjectService.class),
                SimpleProject.class, ProjectTypeConstants.PROJECT, ProjectTableFieldDef.selected,
                Arrays.asList(ProjectTableFieldDef.projectName,
                        ProjectTableFieldDef.lead, ProjectTableFieldDef.client, ProjectTableFieldDef.startDate,
                        ProjectTableFieldDef.status));

        tableItem.addGeneratedColumn("selected", (source, itemId, columnId) -> {
            final SimpleProject item = tableItem.getBeanByIndex(itemId);
            final CheckBoxDecor cb = new CheckBoxDecor("", item.isSelected());
            cb.addValueChangeListener(valueChangeEvent -> tableItem.fireSelectItemEvent(item));
            item.setExtraData(cb);
            return cb;
        });

        tableItem.addGeneratedColumn(Project.Field.name.name(), (source, itemId, columnId) -> {
            SimpleProject project = tableItem.getBeanByIndex(itemId);
            A projectLink = new A(ProjectLinkGenerator.generateProjectLink(project.getId())).appendText(project.getName());
            projectLink.setAttribute("onmouseover", TooltipHelper.projectHoverJsFunction(ProjectTypeConstants.PROJECT,
                    project.getId() + ""));
            projectLink.setAttribute("onmouseleave", TooltipHelper.itemMouseLeaveJsFunction());
            A homepageUrl;
            if (StringUtils.isNotBlank(project.getHomepage())) {
                homepageUrl = new A(project.getHomepage(), "_blank").appendText(project.getHomepage()).setCSSClass(WebThemes.META_INFO);
            } else {
                homepageUrl = new A("").appendText(UserUIContext.getMessage(GenericI18Enum.OPT_UNDEFINED));
            }

            Div projectDiv = new Div().appendChild(projectLink, new Br(), homepageUrl);
            ELabel projectLbl = ELabel.html(projectDiv.write());
            return new MHorizontalLayout(ProjectAssetsUtil.projectLogoComp(project
                    .getShortname(), project.getId(), project.getAvatarid(), 32), projectLbl)
                    .expand(projectLbl).alignAll(Alignment.MIDDLE_LEFT).withMargin(false);
        });

        tableItem.addGeneratedColumn(Project.Field.memlead.name(), (source, itemId, columnId) -> {
            SimpleProject project = tableItem.getBeanByIndex(itemId);
            return ELabel.html(ProjectLinkBuilder.generateProjectMemberHtmlLink(project.getId(), project.getMemlead(),
                    project.getLeadFullName(), project.getLeadAvatarId(), true));
        });

        tableItem.addGeneratedColumn(Project.Field.clientid.name(), (source, itemId, columnId) -> {
            SimpleProject project = tableItem.getBeanByIndex(itemId);
            if (project.getClientid() != null) {
                LabelLink b = new LabelLink(project.getClientName(),
                        ProjectLinkGenerator.generateClientPreviewLink(project.getClientid()));
                b.setIconLink(ProjectAssetsManager.getAsset(ProjectTypeConstants.CLIENT));
                return b;
            } else {
                return new Label();
            }
        });

        tableItem.addGeneratedColumn(Project.Field.planstartdate.name(), (source, itemId, columnId) -> {
            SimpleProject project = tableItem.getBeanByIndex(itemId);
            return new Label(UserUIContext.formatDate(project.getPlanstartdate()));
        });

        tableItem.addGeneratedColumn(Project.Field.planenddate.name(), (source, itemId, columnId) -> {
            SimpleProject project = tableItem.getBeanByIndex(itemId);
            return new Label(UserUIContext.formatDate(project.getPlanenddate()));
        });

        tableItem.addGeneratedColumn(Project.Field.status.name(), (source, itemId, columnId) -> {
            SimpleProject project = tableItem.getBeanByIndex(itemId);
            return ELabel.i18n(project.getStatus(), StatusI18nEnum.class);
        });

        tableItem.addGeneratedColumn(Project.Field.createdtime.name(), (source, itemId, columnId) -> {
            SimpleProject project = tableItem.getBeanByIndex(itemId);
            return new Label(UserUIContext.formatDate(project.getCreatedtime()));
        });

        tableItem.setWidth("100%");

        bodyLayout.with(constructTableActionControls(), tableItem).expand(tableItem);
    }

    private ComponentContainer constructTableActionControls() {
        MHorizontalLayout layout = new MHorizontalLayout().withFullWidth().withStyleName(WebThemes.TABLE_ACTION_CONTROLS);

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

        MButton customizeViewBtn = new MButton("", clickEvent -> UI.getCurrent().addWindow(new ProjectListCustomizeWindow(tableItem)))
                .withStyleName(WebThemes.BUTTON_ACTION).withIcon(VaadinIcons.ADJUST);
        customizeViewBtn.setDescription(UserUIContext.getMessage(GenericI18Enum.OPT_LAYOUT_OPTIONS));
        layout.with(customizeViewBtn).withAlign(customizeViewBtn, Alignment.MIDDLE_RIGHT);

        return layout;
    }

    @Override
    public void showNoItemView() {

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
    public IPagedTable<ProjectSearchCriteria, SimpleProject> getPagedBeanGrid() {
        return tableItem;
    }
}
