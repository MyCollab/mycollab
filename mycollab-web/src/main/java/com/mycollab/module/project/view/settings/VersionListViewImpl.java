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

import com.mycollab.common.TableViewField;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkGenerator;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTooltipGenerator;
import com.mycollab.module.tracker.domain.SimpleVersion;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.mycollab.module.tracker.service.VersionService;
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
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Label;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class VersionListViewImpl extends AbstractVerticalPageView implements VersionListView {
    private static final long serialVersionUID = 1L;

    private final VersionSearchPanel versionSearchPanel;
    private SelectionOptionButton selectOptionButton;
    private DefaultPagedBeanTable<VersionService, VersionSearchCriteria, SimpleVersion> tableItem;
    private MVerticalLayout versionListLayout;
    private DefaultMassItemActionHandlerContainer tableActionControls;
    private Label selectedItemsNumberLabel = new Label();

    public VersionListViewImpl() {
        this.setMargin(new MarginInfo(false, true, false, true));
        this.versionSearchPanel = new VersionSearchPanel();
        this.versionListLayout = new MVerticalLayout().withSpacing(false).withMargin(false);
        this.with(versionSearchPanel, versionListLayout);
        this.generateDisplayTable();
    }

    private void generateDisplayTable() {
        tableItem = new DefaultPagedBeanTable<>(AppContextUtil.getSpringBean(VersionService.class), SimpleVersion.class,
                new TableViewField(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH),
                new HashSet<>(Arrays.asList(new TableViewField(GenericI18Enum.FORM_NAME, "name", WebUIConstants.TABLE_EX_LABEL_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_STATUS, "status", WebUIConstants.TABLE_M_LABEL_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_DESCRIPTION, "description", 2 * WebUIConstants.TABLE_EX_LABEL_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_DUE_DATE, "duedate", WebUIConstants.TABLE_DATE_TIME_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_PROGRESS, "id", WebUIConstants.TABLE_M_LABEL_WIDTH))));

        tableItem.addGeneratedColumn("selected", (source, itemId, columnId) -> {
            SimpleVersion version = tableItem.getBeanByIndex(itemId);
            CheckBoxDecor cb = new CheckBoxDecor("", version.isSelected());
            cb.addValueChangeListener(valueChangeEvent -> tableItem.fireSelectItemEvent(version));
            version.setExtraData(cb);
            return cb;
        });

        tableItem.addGeneratedColumn("name", (source, itemId, columnId) -> {
            final Version version = tableItem.getBeanByIndex(itemId);
            final LabelLink b = new LabelLink(version.getName(), ProjectLinkGenerator
                    .generateBugVersionPreviewLink(version.getProjectid(), version.getId()));
            if (version.getStatus() != null && version.getStatus().equals(StatusI18nEnum.Closed.name())) {
                b.addStyleName(WebThemes.LINK_COMPLETED);
            } else if (version.getDuedate() != null && (version.getDuedate().isBefore(LocalDate.now()))) {
                b.addStyleName(WebThemes.LINK_OVERDUE);
            }
            b.setDescription(ProjectTooltipGenerator.generateToolTipVersion(UserUIContext.getUserLocale(), AppUI.getDateFormat(),
                    version, AppUI.getSiteUrl(), UserUIContext.getUserTimeZone()));
            return b;
        });

        tableItem.addGeneratedColumn("duedate", (source, itemId, columnId) -> {
            final Version bugVersion = tableItem.getBeanByIndex(itemId);
            return new ELabel().prettyDate(bugVersion.getDuedate());
        });

        tableItem.addGeneratedColumn("id", (source, itemId, columnId) -> {
            SimpleVersion version = tableItem.getBeanByIndex(itemId);
            return new ProgressBarIndicator(version.getNumBugs(), version.getNumOpenBugs(), false);
        });

        tableItem.addGeneratedColumn("status", (source, itemId, columnId) -> {
            SimpleVersion version = tableItem.getBeanByIndex(itemId);
            return ELabel.i18n(version.getStatus(), StatusI18nEnum.class);
        });

        tableItem.addGeneratedColumn("description", (source, itemId, columnId) -> {
            SimpleVersion version = tableItem.getBeanByIndex(itemId);
            return ELabel.richText(version.getDescription());
        });

        tableItem.setWidth("100%");

        versionListLayout.addComponent(constructTableActionControls());
        versionListLayout.addComponent(tableItem);
    }

    @Override
    public HasSearchHandlers<VersionSearchCriteria> getSearchHandlers() {
        return versionSearchPanel;
    }

    private ComponentContainer constructTableActionControls() {
        final MCssLayout layoutWrapper = new MCssLayout().withFullWidth().withStyleName(WebThemes.TABLE_ACTION_CONTROLS);

        selectOptionButton = new SelectionOptionButton(tableItem);

        tableActionControls = new DefaultMassItemActionHandlerContainer();

        if (CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.VERSIONS)) {
            tableActionControls.addDeleteActionItem();
        }

        tableActionControls.addMailActionItem();
        tableActionControls.addDownloadPdfActionItem();
        tableActionControls.addDownloadExcelActionItem();
        tableActionControls.addDownloadCsvActionItem();

        layoutWrapper.add(selectOptionButton, tableActionControls, selectedItemsNumberLabel);
        return layoutWrapper;
    }

    @Override
    public void enableActionControls(final int numOfSelectedItems) {
        this.tableActionControls.setVisible(true);
        this.selectedItemsNumberLabel.setValue(UserUIContext.getMessage(
                GenericI18Enum.TABLE_SELECTED_ITEM_TITLE, numOfSelectedItems));
    }

    @Override
    public void disableActionControls() {
        this.tableActionControls.setVisible(false);
        this.selectOptionButton.setSelectedCheckbox(false);
        this.selectedItemsNumberLabel.setValue("");
    }

    @Override
    public void showNoItemView() {
        removeAllComponents();
        this.addComponent(new VersionListNoItemView());
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
    public HasSelectableItemHandlers<SimpleVersion> getSelectableItemHandlers() {
        return this.tableItem;
    }

    @Override
    public AbstractPagedBeanTable<VersionSearchCriteria, SimpleVersion> getPagedBeanGrid() {
        return this.tableItem;
    }
}
