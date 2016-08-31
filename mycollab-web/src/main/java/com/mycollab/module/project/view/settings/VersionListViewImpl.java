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
package com.mycollab.module.project.view.settings;

import com.mycollab.common.TableViewField;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.ProjectLinkBuilder;
import com.mycollab.module.project.ProjectRolePermissionCollections;
import com.mycollab.module.project.ProjectTooltipGenerator;
import com.mycollab.module.tracker.domain.SimpleVersion;
import com.mycollab.module.tracker.domain.Version;
import com.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.mycollab.module.tracker.service.VersionService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.events.HasMassItemActionHandler;
import com.mycollab.vaadin.events.HasSearchHandlers;
import com.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.mycollab.vaadin.mvp.AbstractPageView;
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
import java.util.GregorianCalendar;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class VersionListViewImpl extends AbstractPageView implements VersionListView {
    private static final long serialVersionUID = 1L;

    private final VersionSearchPanel versionSearchPanel;
    private SelectionOptionButton selectOptionButton;
    private DefaultPagedBeanTable<VersionService, VersionSearchCriteria, SimpleVersion> tableItem;
    private VerticalLayout versionListLayout;
    private DefaultMassItemActionHandlerContainer tableActionControls;
    private Label selectedItemsNumberLabel = new Label();

    public VersionListViewImpl() {
        this.setMargin(new MarginInfo(false, true, true, true));
        this.versionSearchPanel = new VersionSearchPanel();
        this.versionListLayout = new VerticalLayout();
        this.with(versionSearchPanel, versionListLayout);
        this.generateDisplayTable();
    }

    private void generateDisplayTable() {
        tableItem = new DefaultPagedBeanTable<>(AppContextUtil.getSpringBean(VersionService.class),
                SimpleVersion.class,
                new TableViewField(null, "selected", WebUIConstants.TABLE_CONTROL_WIDTH),
                Arrays.asList(new TableViewField(GenericI18Enum.FORM_NAME, "versionname", WebUIConstants.TABLE_EX_LABEL_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_STATUS, "status", WebUIConstants.TABLE_M_LABEL_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_DESCRIPTION, "description", 2 * WebUIConstants.TABLE_EX_LABEL_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_DUE_DATE, "duedate", WebUIConstants.TABLE_DATE_TIME_WIDTH),
                        new TableViewField(GenericI18Enum.FORM_PROGRESS, "id", WebUIConstants.TABLE_EX_LABEL_WIDTH)));

        tableItem.addGeneratedColumn("selected", (source, itemId, columnId) -> {
            final SimpleVersion version = tableItem.getBeanByIndex(itemId);
            final CheckBoxDecor cb = new CheckBoxDecor("", version.isSelected());
            cb.setImmediate(true);
            cb.addValueChangeListener(valueChangeEvent -> tableItem.fireSelectItemEvent(version));
            version.setExtraData(cb);
            return cb;
        });

        tableItem.addGeneratedColumn("versionname", (source, itemId, columnId) -> {
            final Version bugVersion = tableItem.getBeanByIndex(itemId);
            final LabelLink b = new LabelLink(bugVersion.getVersionname(), ProjectLinkBuilder
                    .generateBugVersionPreviewFullLink(bugVersion.getProjectid(), bugVersion.getId()));
            if (bugVersion.getStatus() != null && bugVersion.getStatus().equals(StatusI18nEnum.Closed.name())) {
                b.addStyleName(WebUIConstants.LINK_COMPLETED);
            } else if (bugVersion.getDuedate() != null && (bugVersion.getDuedate().before(new GregorianCalendar().getTime()))) {
                b.addStyleName(WebUIConstants.LINK_OVERDUE);
            }
            b.setDescription(ProjectTooltipGenerator.generateToolTipVersion(AppContext.getUserLocale(), AppContext.getDateFormat(),
                    bugVersion, AppContext.getSiteUrl(), AppContext.getUserTimeZone()));
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
        final CssLayout layoutWrapper = new CssLayout();
        layoutWrapper.setWidth("100%");
        MHorizontalLayout layout = new MHorizontalLayout();
        layoutWrapper.addStyleName(WebUIConstants.TABLE_ACTION_CONTROLS);
        layoutWrapper.addComponent(layout);

        this.selectOptionButton = new SelectionOptionButton(this.tableItem);
        layout.addComponent(this.selectOptionButton);

        tableActionControls = new DefaultMassItemActionHandlerContainer();

        if (CurrentProjectVariables.canAccess(ProjectRolePermissionCollections.VERSIONS)) {
            tableActionControls.addDeleteActionItem();
        }

        tableActionControls.addMailActionItem();
        tableActionControls.addDownloadPdfActionItem();
        tableActionControls.addDownloadExcelActionItem();
        tableActionControls.addDownloadCsvActionItem();

        layout.with(tableActionControls, selectedItemsNumberLabel).withAlign(selectedItemsNumberLabel, Alignment.MIDDLE_CENTER);
        return layoutWrapper;
    }

    @Override
    public void enableActionControls(final int numOfSelectedItems) {
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
    public AbstractPagedBeanTable<VersionSearchCriteria, SimpleVersion> getPagedBeanTable() {
        return this.tableItem;
    }
}
