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
package com.mycollab.module.project.view;

import com.mycollab.common.i18n.FileI18nEnum;
import com.mycollab.common.i18n.FollowerI18nEnum;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.module.project.domain.FollowingTicket;
import com.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria;
import com.mycollab.module.project.service.ProjectFollowingTicketService;
import com.mycollab.reporting.ReportExportType;
import com.mycollab.reporting.RpFieldsBuilder;
import com.mycollab.reporting.SimpleReportTemplateExecutor;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.events.HasSearchHandlers;
import com.mycollab.vaadin.mvp.AbstractVerticalPageView;
import com.mycollab.vaadin.mvp.ViewComponent;
import com.mycollab.vaadin.resources.LazyStreamSource;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.SplitButton;
import com.mycollab.vaadin.web.ui.WebThemes;
import com.mycollab.vaadin.web.ui.table.AbstractPagedBeanTable;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class FollowingTicketViewImpl extends AbstractVerticalPageView implements FollowingTicketView {
    private static final long serialVersionUID = 1L;

    private SplitButton exportButtonControl;
    private FollowingTicketTableDisplay ticketTable;
    private FollowingTicketSearchPanel searchPanel;

    public FollowingTicketViewImpl() {
        this.withFullWidth().withMargin(true);
    }

    @Override
    public void initContent() {
        removeAllComponents();
        MHorizontalLayout header = new MHorizontalLayout().withFullWidth();

        ELabel layoutHeader = ELabel.h2(FontAwesome.EYE.getHtml() + " " + UserUIContext.getMessage(FollowerI18nEnum
                .OPT_MY_FOLLOWING_TICKETS, 0)).withWidthUndefined();

        Button exportBtn = new Button(UserUIContext.getMessage(GenericI18Enum.ACTION_EXPORT), clickEvent -> exportButtonControl.setPopupVisible(true));
        exportButtonControl = new SplitButton(exportBtn);
        exportButtonControl.addStyleName(WebThemes.BUTTON_OPTION);
        exportButtonControl.setIcon(FontAwesome.EXTERNAL_LINK);

        OptionPopupContent popupButtonsControl = new OptionPopupContent();
        exportButtonControl.setContent(popupButtonsControl);

        Button exportPdfBtn = new Button(UserUIContext.getMessage(FileI18nEnum.PDF));
        FileDownloader pdfDownloader = new FileDownloader(constructStreamResource(ReportExportType.PDF));
        pdfDownloader.extend(exportPdfBtn);
        exportPdfBtn.setIcon(FontAwesome.FILE_PDF_O);
        popupButtonsControl.addOption(exportPdfBtn);

        Button exportExcelBtn = new Button(UserUIContext.getMessage(FileI18nEnum.EXCEL));
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
                SimpleReportTemplateExecutor reportTemplateExecutor = new SimpleReportTemplateExecutor.AllItems<>(
                        UserUIContext.getUserTimeZone(), UserUIContext.getUserLocale(), "Following Tickets",
                        new RpFieldsBuilder(ticketTable.getDisplayColumns()), exportType, FollowingTicket.class,
                        AppContextUtil.getSpringBean(ProjectFollowingTicketService.class));
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
