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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.common.i18n.FileI18nEnum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.service.BugService;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.reporting.RpParameterBuilder;
import com.esofthead.mycollab.reporting.SimpleGridExportItemsStreamResource;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasMassItemActionHandlers;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectableItemHandlers;
import com.esofthead.mycollab.vaadin.events.HasSelectionOptionHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.resources.StreamResourceFactory;
import com.esofthead.mycollab.vaadin.resources.StreamWrapperFileDownloader;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.hene.popupbutton.PopupButton;

import java.util.Arrays;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class BugListViewImpl extends AbstractPageView implements BugListView {

    private static final long serialVersionUID = 1L;
    private final BugSearchPanel bugSearchPanel;
    private BugTableDisplay tableItem;
    private final VerticalLayout bugListLayout;

    public BugListViewImpl() {
        this.setMargin(new MarginInfo(false, true, false, true));

        this.bugSearchPanel = new BugSearchPanel();
        this.bugListLayout = new VerticalLayout();
        this.generateDisplayTable();
        constructTableActionControls();
        with(bugSearchPanel, bugListLayout);

    }

    private void generateDisplayTable() {
        this.tableItem = new BugTableDisplay(BugListView.VIEW_DEF_ID,
                BugTableFieldDef.action, Arrays.asList(
                BugTableFieldDef.summary, BugTableFieldDef.assignUser,
                BugTableFieldDef.severity, BugTableFieldDef.resolution,
                BugTableFieldDef.duedate));

        this.tableItem.addTableListener(new TableClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void itemClick(final TableClickEvent event) {
                final SimpleBug bug = (SimpleBug) event.getData();
                if ("summary".equals(event.getFieldName())) {
                    EventBusFactory.getInstance().post(
                            new BugEvent.GotoRead(BugListViewImpl.this, bug
                                    .getId()));
                }
            }
        });
        this.bugListLayout.addComponent(this.tableItem);
    }

    @Override
    public HasSearchHandlers<BugSearchCriteria> getSearchHandlers() {
        return this.bugSearchPanel;
    }

    private void constructTableActionControls() {
        Button customizeViewBtn = new Button("", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                UI.getCurrent().addWindow(
                        new BugListCustomizeWindow(BugListView.VIEW_DEF_ID,
                                tableItem));

            }
        });
        customizeViewBtn.setIcon(FontAwesome.COG);
        customizeViewBtn.setDescription("Layout Options");
        customizeViewBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
        bugSearchPanel.addHeaderRight(customizeViewBtn);

        PopupButton exportButtonControl = new PopupButton();
        exportButtonControl.addStyleName(UIConstants.THEME_GRAY_LINK);
        exportButtonControl.setIcon(FontAwesome.EXTERNAL_LINK);
        exportButtonControl.setDescription(AppContext
                .getMessage(FileI18nEnum.EXPORT_FILE));
        bugSearchPanel.addHeaderRight(exportButtonControl);

        VerticalLayout popupButtonsControl = new VerticalLayout();
        exportButtonControl.setContent(popupButtonsControl);

        Button exportPdfBtn = new Button(
                AppContext.getMessage(FileI18nEnum.PDF));

        StreamWrapperFileDownloader fileDownloader = new StreamWrapperFileDownloader(
                new StreamResourceFactory() {

                    @Override
                    public StreamResource getStreamResource() {
                        String title = "Bugs of Project "
                                + ((CurrentProjectVariables.getProject() != null && CurrentProjectVariables
                                .getProject().getName() != null) ? CurrentProjectVariables
                                .getProject().getName() : "");
                        BugSearchCriteria searchCriteria = new BugSearchCriteria();
                        searchCriteria.setProjectId(new NumberSearchField(
                                SearchField.AND, CurrentProjectVariables
                                .getProject().getId()));

                        return new StreamResource(
                                new SimpleGridExportItemsStreamResource.AllItems<>(
                                        title,
                                        new RpParameterBuilder(tableItem
                                                .getDisplayColumns()),
                                        ReportExportType.PDF,
                                        ApplicationContextUtil
                                                .getSpringBean(BugService.class),
                                        searchCriteria, SimpleBug.class),
                                "export.pdf");
                    }

                });
        fileDownloader.extend(exportPdfBtn);
        exportPdfBtn.setIcon(FontAwesome.FILE_PDF_O);
        exportPdfBtn.setStyleName("link");
        popupButtonsControl.addComponent(exportPdfBtn);

        Button exportExcelBtn = new Button(
                AppContext.getMessage(FileI18nEnum.EXCEL));
        StreamWrapperFileDownloader excelDownloader = new StreamWrapperFileDownloader(
                new StreamResourceFactory() {

                    @Override
                    public StreamResource getStreamResource() {
                        String title = "Bugs of Project "
                                + ((CurrentProjectVariables.getProject() != null && CurrentProjectVariables
                                .getProject().getName() != null) ? CurrentProjectVariables
                                .getProject().getName() : "");
                        BugSearchCriteria searchCriteria = new BugSearchCriteria();
                        searchCriteria.setProjectId(new NumberSearchField(
                                SearchField.AND, CurrentProjectVariables
                                .getProject().getId()));

                        return new StreamResource(
                                new SimpleGridExportItemsStreamResource.AllItems<>(
                                        title,
                                        new RpParameterBuilder(tableItem
                                                .getDisplayColumns()),
                                        ReportExportType.EXCEL,
                                        ApplicationContextUtil
                                                .getSpringBean(BugService.class),
                                        searchCriteria, SimpleBug.class),
                                "export.xlsx");
                    }
                });
        excelDownloader.extend(exportExcelBtn);
        exportExcelBtn.setIcon(FontAwesome.FILE_EXCEL_O);
        exportExcelBtn.setStyleName("link");
        popupButtonsControl.addComponent(exportExcelBtn);

        Button exportCsvBtn = new Button(
                AppContext.getMessage(FileI18nEnum.CSV));

        StreamWrapperFileDownloader csvFileDownloader = new StreamWrapperFileDownloader(
                new StreamResourceFactory() {

                    @Override
                    public StreamResource getStreamResource() {
                        String title = "Bugs of Project "
                                + ((CurrentProjectVariables.getProject() != null && CurrentProjectVariables
                                .getProject().getName() != null) ? CurrentProjectVariables
                                .getProject().getName() : "");
                        BugSearchCriteria searchCriteria = new BugSearchCriteria();
                        searchCriteria.setProjectId(new NumberSearchField(
                                SearchField.AND, CurrentProjectVariables
                                .getProject().getId()));

                        return new StreamResource(
                                new SimpleGridExportItemsStreamResource.AllItems<>(
                                        title,
                                        new RpParameterBuilder(tableItem
                                                .getDisplayColumns()),
                                        ReportExportType.CSV,
                                        ApplicationContextUtil
                                                .getSpringBean(BugService.class),
                                        searchCriteria, SimpleBug.class),
                                "export.csv");
                    }
                });
        csvFileDownloader.extend(exportCsvBtn);

        exportCsvBtn.setIcon(FontAwesome.FILE_TEXT_O);
        exportCsvBtn.setStyleName("link");
        popupButtonsControl.addComponent(exportCsvBtn);
    }

    @Override
    public HasSelectableItemHandlers<SimpleBug> getSelectableItemHandlers() {
        return this.tableItem;
    }

    @Override
    public void enableActionControls(int numOfSelectedItem) {
        throw new UnsupportedOperationException(
                "This view doesn't support this operation");
    }

    @Override
    public void disableActionControls() {
        throw new UnsupportedOperationException(
                "This view doesn't support this operation");
    }

    @Override
    public HasSelectionOptionHandlers getOptionSelectionHandlers() {
        throw new UnsupportedOperationException(
                "This view doesn't support this operation");
    }

    @Override
    public HasMassItemActionHandlers getPopupActionHandlers() {
        throw new UnsupportedOperationException(
                "This view doesn't support this operation");
    }

    @Override
    public AbstractPagedBeanTable<BugSearchCriteria, SimpleBug> getPagedBeanTable() {
        return this.tableItem;
    }
}
