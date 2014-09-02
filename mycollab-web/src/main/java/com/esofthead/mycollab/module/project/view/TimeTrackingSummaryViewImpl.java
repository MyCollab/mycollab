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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.BooleanSearchField;
import com.esofthead.mycollab.core.arguments.RangeDateSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.utils.DateTimeUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.module.project.view.parameters.BugScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.project.view.parameters.TaskScreenData;
import com.esofthead.mycollab.module.project.view.time.TimeTableFieldDef;
import com.esofthead.mycollab.module.project.view.time.TimeTrackingTableDisplay;
import com.esofthead.mycollab.reporting.ExportItemsStreamResource;
import com.esofthead.mycollab.reporting.ReportExportType;
import com.esofthead.mycollab.reporting.RpParameterBuilder;
import com.esofthead.mycollab.reporting.SimpleGridExportItemsStreamResource;
import com.esofthead.mycollab.shell.events.ShellEvent;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.mvp.ViewScope;
import com.esofthead.mycollab.vaadin.resource.LazyStreamSource;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.SplitButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Sizeable;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class TimeTrackingSummaryViewImpl extends AbstractPageView implements
		TimeTrackingSummaryView {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
			"EEEE, dd MMMM yyyy");
	private static final List<TableViewField> FIELDS = Arrays.asList(
			TimeTableFieldDef.summary, TimeTableFieldDef.logUser,
			TimeTableFieldDef.project, TimeTableFieldDef.logValue,
			TimeTableFieldDef.billable);

	private static final long serialVersionUID = 1L;

	private PopupDateField fromDateField;
	private PopupDateField toDateField;

	private Label totalHoursLoggingLabel;
	private SplitButton exportButtonControl;

	private ItemTimeLoggingSearchCriteria searchCriteria;
	private Date fromDate, toDate;
	private ItemTimeLoggingService itemTimeLoggingService;

	private VerticalLayout layoutItem;

	public TimeTrackingSummaryViewImpl() {
		this.setWidth("100%");

		itemTimeLoggingService = ApplicationContextUtil
				.getSpringBean(ItemTimeLoggingService.class);

		final CssLayout headerWrapper = new CssLayout();
		headerWrapper.setWidth("100%");
		headerWrapper.setStyleName("projectfeed-hdr-wrapper");

		final HorizontalLayout header = new HorizontalLayout();
		header.setWidth("100%");
		header.setSpacing(true);

		HorizontalLayout controlsPanel = new HorizontalLayout();
		HorizontalLayout loggingPanel = new HorizontalLayout();
		HorizontalLayout controlBtns = new HorizontalLayout();

		final Embedded timeIcon = new Embedded();
		timeIcon.setSource(MyCollabResource
				.newResource("icons/24/time_tracking.png"));
		header.addComponent(timeIcon);

		final Label layoutHeader = new Label("Time Tracking");
		layoutHeader.addStyleName("h2");
		header.addComponent(layoutHeader);
		header.setComponentAlignment(layoutHeader, Alignment.MIDDLE_LEFT);
		header.setExpandRatio(layoutHeader, 1.0f);

		final CssLayout contentWrapper = new CssLayout();
		contentWrapper.setWidth("100%");
		contentWrapper.addStyleName(UIConstants.CONTENT_WRAPPER);

		headerWrapper.addComponent(header);
		this.addComponent(headerWrapper);
		contentWrapper.addComponent(controlBtns);
		contentWrapper.addComponent(controlsPanel);
		contentWrapper.addComponent(loggingPanel);
		this.addComponent(contentWrapper);

		final Button backBtn = new Button("Back to Workboard");
		backBtn.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				EventBusFactory.getInstance().post(
						new ShellEvent.GotoProjectModule(
								TimeTrackingSummaryViewImpl.this, null));

			}
		});

		backBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
		backBtn.setIcon(MyCollabResource.newResource("icons/16/back.png"));

		controlBtns.setMargin(new MarginInfo(true, false, true, false));
		controlBtns.addComponent(backBtn);

		final HorizontalLayout dateSelectionLayout = new HorizontalLayout();
		dateSelectionLayout.setSpacing(true);
		dateSelectionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		dateSelectionLayout
				.setMargin(new MarginInfo(false, false, true, false));
		controlsPanel.addComponent(dateSelectionLayout);

		dateSelectionLayout.addComponent(new Label("From:  "));

		fromDateField = new PopupDateField();
		fromDateField.setResolution(Resolution.DAY);
		dateSelectionLayout.addComponent(fromDateField);

		dateSelectionLayout.addComponent(new Label("  To:  "));
		toDateField = new PopupDateField();
		toDateField.setResolution(Resolution.DAY);
		dateSelectionLayout.addComponent(toDateField);

		final Button queryBtn = new Button(
				AppContext.getMessage(GenericI18Enum.BUTTON_SUBMIT_LABEL),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						fromDate = fromDateField.getValue();
						toDate = toDateField.getValue();
						searchCriteria.setRangeDate(new RangeDateSearchField(
								fromDate, toDate));
						searchTimeReporting();
					}
				});
		queryBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

		dateSelectionLayout.addComponent(queryBtn);

		controlsPanel.setWidth("100%");
		controlsPanel.setHeight("30px");
		controlsPanel.setSpacing(true);

		loggingPanel.setWidth("100%");
		loggingPanel.setHeight("30px");
		loggingPanel.setSpacing(true);

		totalHoursLoggingLabel = new Label("Total Hours Logging: 0 Hrs",
				ContentMode.HTML);
		totalHoursLoggingLabel.addStyleName(UIConstants.LAYOUT_LOG);
		totalHoursLoggingLabel.addStyleName(UIConstants.TEXT_LOG_DATE_FULL);
		loggingPanel.addComponent(totalHoursLoggingLabel);
		loggingPanel.setExpandRatio(totalHoursLoggingLabel, 1.0f);
		loggingPanel.setComponentAlignment(totalHoursLoggingLabel,
				Alignment.MIDDLE_LEFT);

		Button exportBtn = new Button("Export", new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				exportButtonControl.setPopupVisible(true);

			}
		});
		exportButtonControl = new SplitButton(exportBtn);
		exportButtonControl.setWidth(Sizeable.SIZE_UNDEFINED,
				Sizeable.Unit.PIXELS);
		exportButtonControl.addStyleName(UIConstants.THEME_GRAY_LINK);
		exportButtonControl.setIcon(MyCollabResource
				.newResource("icons/16/export.png"));

		VerticalLayout popupButtonsControl = new VerticalLayout();
		exportButtonControl.setContent(popupButtonsControl);

		Button exportPdfBtn = new Button("Pdf");
		FileDownloader pdfDownloader = new FileDownloader(
				constructStreamResource(ReportExportType.PDF));
		pdfDownloader.extend(exportPdfBtn);
		exportPdfBtn.setIcon(MyCollabResource
				.newResource("icons/16/filetypes/pdf.png"));
		exportPdfBtn.setStyleName("link");
		popupButtonsControl.addComponent(exportPdfBtn);

		Button exportExcelBtn = new Button("Excel");
		FileDownloader excelDownloader = new FileDownloader(
				constructStreamResource(ReportExportType.EXCEL));
		excelDownloader.extend(exportExcelBtn);
		exportExcelBtn.setIcon(MyCollabResource
				.newResource("icons/16/filetypes/excel.png"));
		exportExcelBtn.setStyleName("link");
		popupButtonsControl.addComponent(exportExcelBtn);

		controlBtns.addComponent(exportButtonControl);
		controlBtns.setComponentAlignment(exportButtonControl,
				Alignment.TOP_RIGHT);
		controlBtns.setComponentAlignment(backBtn, Alignment.TOP_LEFT);
		controlBtns.setSizeFull();

		this.layoutItem = new VerticalLayout();
		this.layoutItem.addStyleName(UIConstants.LAYOUT_LOG);
		this.layoutItem.setWidth("100%");
		contentWrapper.addComponent(this.layoutItem);
	}

	private StreamResource constructStreamResource(
			final ReportExportType exportType) {
		LazyStreamSource streamSource = new LazyStreamSource() {
			private static final long serialVersionUID = 1L;

			@Override
			protected StreamSource buildStreamSource() {
				return new SimpleGridExportItemsStreamResource.AllItems<ItemTimeLoggingSearchCriteria, SimpleItemTimeLogging>(
						"Time Tracking Report", new RpParameterBuilder(FIELDS),
						exportType, itemTimeLoggingService, searchCriteria,
						SimpleItemTimeLogging.class);
			}
		};
		StreamResource res = new StreamResource(streamSource,
				ExportItemsStreamResource.getDefaultExportFileName(exportType));
		return res;
	}

	@Override
	public void display(Collection<Integer> projectIds) {
		Calendar date = new GregorianCalendar();
		date.set(Calendar.DAY_OF_MONTH, 1);

		fromDate = date.getTime();
		date.add(Calendar.DAY_OF_MONTH,
				date.getActualMaximum(Calendar.DAY_OF_MONTH));
		toDate = date.getTime();

		fromDateField.setValue(fromDate);
		toDateField.setValue(toDate);

		searchCriteria = new ItemTimeLoggingSearchCriteria();
		searchCriteria.setLogUsers(new SetSearchField<String>(SearchField.AND,
				new String[] { AppContext.getUsername() }));
		searchCriteria.setProjectIds(new SetSearchField<Integer>(
				((Integer[]) projectIds.toArray(new Integer[0]))));
		searchCriteria.setRangeDate(new RangeDateSearchField(fromDate, toDate));
	}

	private void searchTimeReporting() {
		this.layoutItem.removeAllComponents();

		searchCriteria.setIsBillable(new BooleanSearchField(true));
		Double billableHour = this.itemTimeLoggingService
				.getTotalHoursByCriteria(searchCriteria);
		if (billableHour == null || billableHour < 0) {
			billableHour = 0d;
		}

		searchCriteria.setIsBillable(new BooleanSearchField(false));
		Double nonbillableHour = this.itemTimeLoggingService
				.getTotalHoursByCriteria(searchCriteria);
		if (nonbillableHour == null || nonbillableHour < 0) {
			nonbillableHour = 0d;
		}

		searchCriteria.setIsBillable(null);
		final Double totalHour = this.itemTimeLoggingService
				.getTotalHoursByCriteria(searchCriteria);

		if (totalHour == null || totalHour < 0) {
			totalHoursLoggingLabel.setValue("Total hours logging: 0 Hrs");
		} else {
			totalHoursLoggingLabel
					.setValue(AppContext
							.getMessage(
									TimeTrackingI18nEnum.TASK_LIST_RANGE_WITH_TOTAL_HOUR,
									fromDate, toDate, totalHour, billableHour,
									nonbillableHour));
		}

		@SuppressWarnings("unchecked")
		List<SimpleItemTimeLogging> itemTimeLoggingList = itemTimeLoggingService
				.findPagableListByCriteria(new SearchRequest<ItemTimeLoggingSearchCriteria>(
						searchCriteria));
		Date current = new Date(0);
		double billable = 0, nonbillable = 0;
		List<SimpleItemTimeLogging> list = new ArrayList<SimpleItemTimeLogging>();

		for (SimpleItemTimeLogging itemTimeLogging : itemTimeLoggingList) {
			if (DateTimeUtils.compareByDate(itemTimeLogging.getLogforday(),
					current) > 0) {
				showRecord(current, list, billable, nonbillable);

				current = itemTimeLogging.getLogforday();
				list.clear();
				billable = nonbillable = 0;
			}

			list.add(itemTimeLogging);
			billable += itemTimeLogging.getIsbillable() ? itemTimeLogging
					.getLogvalue() : 0;
			nonbillable += !itemTimeLogging.getIsbillable() ? itemTimeLogging
					.getLogvalue() : 0;
		}
		showRecord(current, list, billable, nonbillable);
	}

	private void showRecord(Date date, List<SimpleItemTimeLogging> list,
			Double billable, Double nonbillable) {
		if (list.size() > 0) {
			Label logForDay = new Label(DATE_FORMAT.format(date));
			logForDay.addStyleName(UIConstants.TEXT_LOG_DATE);
			this.layoutItem.addComponent(logForDay);

			TimeTrackingTableDisplay table = new TimeTrackingTableDisplay(
					FIELDS);
			table.addStyleName(UIConstants.FULL_BORDER_TABLE);
			table.addTableListener(this.tableClickListener);
			table.setMargin(new MarginInfo(true, false, false, false));
			table.setCurrentDataList(list);
			this.layoutItem.addComponent(table);

			Label labelTotalHours = new Label(
					("Total Hours: " + (billable + nonbillable)));
			labelTotalHours.addStyleName(UIConstants.TEXT_LOG_HOURS_TOTAL);
			this.layoutItem.addComponent(labelTotalHours);

			Label labelBillableHours = new Label(
					("Billable Hours: " + billable));
			labelBillableHours.setStyleName(UIConstants.TEXT_LOG_HOURS);
			this.layoutItem.addComponent(labelBillableHours);

			Label labelNonbillableHours = new Label(
					("Non Billable Hours: " + nonbillable));
			labelNonbillableHours.setStyleName(UIConstants.TEXT_LOG_HOURS);
			this.layoutItem.addComponent(labelNonbillableHours);
		}
	}

	private TableClickListener tableClickListener = new TableClickListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void itemClick(final TableClickEvent event) {
			final SimpleItemTimeLogging itemLogging = (SimpleItemTimeLogging) event
					.getData();
			if ("summary".equals(event.getFieldName())) {
				final int typeId = itemLogging.getTypeid();
				final int projectId = itemLogging.getProjectid();

				if (ProjectTypeConstants.BUG.equals(itemLogging.getType())) {
					final PageActionChain chain = new PageActionChain(
							new ProjectScreenData.Goto(projectId),
							new BugScreenData.Read(typeId));
					EventBusFactory.getInstance().post(
							new ProjectEvent.GotoMyProject(this, chain));
				} else if (ProjectTypeConstants.TASK.equals(itemLogging
						.getType())) {
					final PageActionChain chain = new PageActionChain(
							new ProjectScreenData.Goto(projectId),
							new TaskScreenData.Read(typeId));
					EventBusFactory.getInstance().post(
							new ProjectEvent.GotoMyProject(this, chain));
				}
			} else if ("projectName".equals(event.getFieldName())) {
				final PageActionChain chain = new PageActionChain(
						new ProjectScreenData.Goto(itemLogging.getProjectid()));
				EventBusFactory.getInstance().post(
						new ProjectEvent.GotoMyProject(this, chain));
			}
		}
	};
}
