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

import com.esofthead.mycollab.module.project.domain.FollowingTicket;
import com.esofthead.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectFollowingTicketService;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.reporting.RpFieldsBuilder;
import com.esofthead.mycollab.reporting.SimpleReportTemplateExecutor;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.resources.LazyStreamSource;
import com.esofthead.mycollab.vaadin.web.ui.OptionPopupContent;
import com.esofthead.mycollab.vaadin.web.ui.SplitButton;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.esofthead.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class FollowingTicketViewImpl extends AbstractPageView implements FollowingTicketView {
    private static final long serialVersionUID = 1L;

    private SplitButton exportButtonControl;
    private FollowingTicketTableDisplay ticketTable;

    private FollowingTicketSearchPanel searchPanel;

    public FollowingTicketViewImpl() {
        this.withWidth("100%").withMargin(true);

        MHorizontalLayout header = new MHorizontalLayout().withWidth("100%");

        Label layoutHeader = new Label(FontAwesome.EYE.getHtml() + " My Following Tickets", ContentMode.HTML);
        layoutHeader.addStyleName(ValoTheme.LABEL_H2);
        layoutHeader.setWidthUndefined();

        Button exportBtn = new Button("Export", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                exportButtonControl.setPopupVisible(true);
            }
        });
        exportButtonControl = new SplitButton(exportBtn);
        exportButtonControl.setWidthUndefined();
        exportButtonControl.addStyleName(UIConstants.THEME_GRAY_LINK);
        exportButtonControl.setIcon(FontAwesome.EXTERNAL_LINK);

        OptionPopupContent popupButtonsControl = new OptionPopupContent();
        exportButtonControl.setContent(popupButtonsControl);

        Button exportPdfBtn = new Button("Pdf");
        FileDownloader pdfDownloader = new FileDownloader(constructStreamResource(ReportExportType.PDF));
        pdfDownloader.extend(exportPdfBtn);
        exportPdfBtn.setIcon(FontAwesome.FILE_PDF_O);
        popupButtonsControl.addOption(exportPdfBtn);

        Button exportExcelBtn = new Button("Excel");
        FileDownloader excelDownloader = new FileDownloader(constructStreamResource(ReportExportType.EXCEL));
        excelDownloader.extend(exportExcelBtn);
        exportExcelBtn.setIcon(FontAwesome.FILE_EXCEL_O);
        popupButtonsControl.addOption(exportExcelBtn);

        header.with(layoutHeader, exportButtonControl).withAlign(layoutHeader, Alignment.MIDDLE_LEFT).withAlign(exportButtonControl,
                Alignment.MIDDLE_RIGHT);
        this.addComponent(layoutHeader);

        searchPanel = new FollowingTicketSearchPanel();
        this.addComponent(searchPanel);

        this.ticketTable = new FollowingTicketTableDisplay();
        this.ticketTable.setMargin(new MarginInfo(true, false, false, false));
        this.addComponent(this.ticketTable);
    }

    private StreamResource constructStreamResource(final ReportExportType exportType) {
        LazyStreamSource streamSource = new LazyStreamSource() {
            private static final long serialVersionUID = 1L;

            @Override
            protected StreamSource buildStreamSource() {
                SimpleReportTemplateExecutor reportTemplateExecutor = new SimpleReportTemplateExecutor.AllItems<>("Following Tickets",
                        new RpFieldsBuilder(ticketTable.getDisplayColumns()), exportType, FollowingTicket.class,
                        ApplicationContextUtil.getSpringBean(ProjectFollowingTicketService.class));
                //TODO: correct the report of following tickets
                return null;
            }
        };

        return new StreamResource(streamSource, exportType.getDefaultFileName());
    }

    @Override
    public void displayTickets() {
        searchPanel.doSearch();
    }

    @Override
    public HasSearchHandlers<FollowingTicketSearchCriteria> getSearchHandlers() {
        return searchPanel;
    }

    @Override
    public AbstractPagedBeanTable<FollowingTicketSearchCriteria, FollowingTicket> getPagedBeanTable() {
        return this.ticketTable;
    }
}
