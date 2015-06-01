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

import com.esofthead.mycollab.common.i18n.FollowerI18nEnum;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.domain.FollowingTicket;
import com.esofthead.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectFollowingTicketService;
import com.esofthead.mycollab.reporting.ExportItemsStreamResource;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.reporting.RpParameterBuilder;
import com.esofthead.mycollab.reporting.SimpleGridExportItemsStreamResource;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.HasSearchHandlers;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.resources.LazyStreamSource;
import com.esofthead.mycollab.vaadin.ui.OptionPopupContent;
import com.esofthead.mycollab.vaadin.ui.SplitButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.AbstractPagedBeanTable;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent
public class FollowingTicketViewImpl extends AbstractPageView implements
		FollowingTicketView {
	private static final long serialVersionUID = 1L;

	private SplitButton exportButtonControl;
	private FollowingTicketTableDisplay ticketTable;

	private FollowingTicketSearchPanel searchPanel;

	public FollowingTicketViewImpl() {
		this.setWidth("100%");

		MVerticalLayout headerWrapper = new MVerticalLayout().withSpacing(false).withMargin(false).withWidth
				("100%").withStyleName("projectfeed-hdr-wrapper");

		MHorizontalLayout header = new MHorizontalLayout().withWidth("100%");

		Label layoutHeader = new Label(FontAwesome.EYE.getHtml() +  " My Following Tickets", ContentMode.HTML);
		layoutHeader.addStyleName("h2");
		header.with(layoutHeader).withAlign(layoutHeader, Alignment.MIDDLE_LEFT).expand(layoutHeader);

		headerWrapper.addComponent(header);
		this.addComponent(headerWrapper);

		MHorizontalLayout controlBtns = new MHorizontalLayout().withSpacing(false).withMargin(new MarginInfo(true,
				false, true, false)).withWidth("100%");

		MVerticalLayout contentWrapper = new MVerticalLayout().withSpacing(false).withMargin(false).withWidth("100%");
		contentWrapper.addStyleName("content-wrapper");

		contentWrapper.addComponent(controlBtns);
		this.addComponent(contentWrapper);

		Button backBtn = new Button(AppContext.getMessage(FollowerI18nEnum.BUTTON_BACK_TO_WORKBOARD));
		backBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				EventBusFactory.getInstance().post(new ShellEvent.GotoProjectModule(FollowingTicketViewImpl.this, null));
			}
		});

		backBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
		backBtn.setIcon(FontAwesome.ARROW_LEFT);

		controlBtns.with(backBtn).withAlign(backBtn, Alignment.MIDDLE_LEFT);

		Button exportBtn = new Button("Export", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
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

		controlBtns.with(exportButtonControl).withAlign(exportButtonControl, Alignment.MIDDLE_RIGHT);

		searchPanel = new FollowingTicketSearchPanel();
		contentWrapper.addComponent(searchPanel);

		this.ticketTable = new FollowingTicketTableDisplay();
		this.ticketTable.addStyleName("full-border-table");
		this.ticketTable.setMargin(new MarginInfo(true, false, false, false));
		contentWrapper.addComponent(this.ticketTable);
	}

	private StreamResource constructStreamResource(
			final ReportExportType exportType) {

		LazyStreamSource streamSource = new LazyStreamSource() {
			private static final long serialVersionUID = 1L;

			@Override
			protected StreamSource buildStreamSource() {
				return new SimpleGridExportItemsStreamResource.AllItems<>(
						"Following Tickets Report", new RpParameterBuilder(ticketTable.getDisplayColumns()),
						exportType, ApplicationContextUtil.getSpringBean(ProjectFollowingTicketService.class),
						new FollowingTicketSearchCriteria(), FollowingTicket.class);
			}
		};

		return new StreamResource(streamSource, ExportItemsStreamResource.getDefaultExportFileName(exportType));
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
