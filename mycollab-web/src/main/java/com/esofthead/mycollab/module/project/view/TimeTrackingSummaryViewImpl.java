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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.esofthead.mycollab.common.TableViewField;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.BooleanSearchField;
import com.esofthead.mycollab.core.arguments.Order;
import com.esofthead.mycollab.core.arguments.RangeDateSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleItemTimeLogging;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ItemTimeLoggingSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.i18n.TimeTrackingI18nEnum;
import com.esofthead.mycollab.module.project.service.ItemTimeLoggingService;
import com.esofthead.mycollab.module.project.service.ProjectMemberService;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.ui.components.AbstractTimeTrackingDisplayComp;
import com.esofthead.mycollab.module.project.ui.components.ItemOrderComboBox;
import com.esofthead.mycollab.module.project.ui.components.TimeTrackingDateOrderComponent;
import com.esofthead.mycollab.module.project.ui.components.TimeTrackingProjectOrderComponent;
import com.esofthead.mycollab.module.project.ui.components.TimeTrackingUserOrderComponent;
import com.esofthead.mycollab.module.project.view.parameters.BugScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.project.view.parameters.TaskScreenData;
import com.esofthead.mycollab.module.project.view.time.TimeTableFieldDef;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
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
import com.esofthead.mycollab.vaadin.ui.ValueComboBox;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickEvent;
import com.esofthead.mycollab.vaadin.ui.table.IPagedBeanTable.TableClickListener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@ViewComponent(scope = ViewScope.PROTOTYPE)
public class TimeTrackingSummaryViewImpl extends AbstractPageView
		implements
			TimeTrackingSummaryView {
	private static final long serialVersionUID = 1L;

	private static final String GROUPBY_PROJECT = "Project";
	private static final String GROUPBY_USER = "User";
	private static final String GROUPBY_DATE = "Date";

	private UserInvolvedProjectsListSelect projectField;
	private UserInvolvedProjectsMemberListSelect userField;
	private PopupDateField fromDateField, toDateField;
	private ComboBox groupField, orderField;

	private Label totalHoursLoggingLabel;
	private SplitButton exportButtonControl;

	private ItemTimeLoggingSearchCriteria searchCriteria;
	private Date fromDate, toDate;
	private ItemTimeLoggingService itemTimeLoggingService;

	private VerticalLayout timeTrackingWrapper;

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

		final GridLayout selectionLayout = new GridLayout(9, 2);
		selectionLayout.setSpacing(true);
		selectionLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		selectionLayout.setMargin(true);
		controlsPanel.addComponent(selectionLayout);

		selectionLayout.addComponent(new Label("From:  "), 0, 0);

		this.fromDateField = new PopupDateField();
		this.fromDateField.setResolution(Resolution.DAY);
		this.fromDateField.setDateFormat(AppContext.getUserDateFormat());
		this.fromDateField.setWidth("100px");
		selectionLayout.addComponent(this.fromDateField, 1, 0);

		selectionLayout.addComponent(new Label("  To:  "), 2, 0);
		this.toDateField = new PopupDateField();
		this.toDateField.setResolution(Resolution.DAY);
		this.toDateField.setDateFormat(AppContext.getUserDateFormat());
		this.toDateField.setWidth("100px");
		selectionLayout.addComponent(this.toDateField, 3, 0);

		selectionLayout.addComponent(new Label("Group:  "), 0, 1);
		this.groupField = new ValueComboBox(false, GROUPBY_PROJECT,
				GROUPBY_DATE, GROUPBY_USER);
		this.groupField.setWidth("100px");
		selectionLayout.addComponent(this.groupField, 1, 1);

		selectionLayout.addComponent(new Label("  Sort:  "), 2, 1);
		this.orderField = new ItemOrderComboBox();
		this.orderField.setWidth("100px");
		selectionLayout.addComponent(this.orderField, 3, 1);

		selectionLayout.addComponent(new Label("  Project:  "), 4, 0);
		this.projectField = new UserInvolvedProjectsListSelect();
		initListSelectStyle(this.projectField);
		selectionLayout.addComponent(this.projectField, 5, 0, 5, 1);

		selectionLayout.addComponent(new Label("  User:  "), 6, 0);
		this.userField = new UserInvolvedProjectsMemberListSelect(
				projectField.getProjectIds());
		initListSelectStyle(this.userField);
		selectionLayout.addComponent(this.userField, 7, 0, 7, 1);

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

		selectionLayout.addComponent(queryBtn, 8, 0);

		controlsPanel.setWidth("100%");
		controlsPanel.setHeight("60px");
		controlsPanel.setSpacing(true);

		loggingPanel.setWidth("100%");
		loggingPanel.setHeight("50px");
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
		exportButtonControl.setWidthUndefined();
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

		this.timeTrackingWrapper = new VerticalLayout();
		this.timeTrackingWrapper.setWidth("100%");
		contentWrapper.addComponent(this.timeTrackingWrapper);
	}
	
	private void initListSelectStyle(ListSelect listSelect) {
		listSelect.setWidth("300px");
		listSelect.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
		listSelect.setNullSelectionAllowed(false);
		listSelect.setMultiSelect(true);
		listSelect.setRows(4);
	}

	private StreamResource constructStreamResource(
			final ReportExportType exportType) {
		LazyStreamSource streamSource = new LazyStreamSource() {
			private static final long serialVersionUID = 1L;

			@Override
			protected StreamSource buildStreamSource() {
				return new SimpleGridExportItemsStreamResource.AllItems<ItemTimeLoggingSearchCriteria, SimpleItemTimeLogging>(
						"Time Tracking Report", new RpParameterBuilder(
								getVisibleFields()), exportType,
						itemTimeLoggingService, searchCriteria,
						SimpleItemTimeLogging.class);
			}
		};
		StreamResource res = new StreamResource(streamSource,
				ExportItemsStreamResource.getDefaultExportFileName(exportType));
		return res;
	}

	private AbstractTimeTrackingDisplayComp buildTimeTrackingComp() {
		String groupBy = (String) groupField.getValue();

		if (groupBy.equals(GROUPBY_PROJECT)) {
			return new TimeTrackingProjectOrderComponent(getVisibleFields(),
					this.tableClickListener);
		} else if (groupBy.equals(GROUPBY_DATE)) {
			return new TimeTrackingDateOrderComponent(getVisibleFields(),
					this.tableClickListener);
		} else if (groupBy.equals(GROUPBY_USER)) {
			return new TimeTrackingUserOrderComponent(getVisibleFields(),
					this.tableClickListener);
		} else {
			throw new MyCollabException("Do not support view type: " + groupBy);
		}
	}

	private List<TableViewField> getVisibleFields() {
		String groupBy = (String) groupField.getValue();

		if (groupBy.equals(GROUPBY_PROJECT)) {
			return Arrays.asList(TimeTableFieldDef.summary,
					TimeTableFieldDef.logForDate, TimeTableFieldDef.logUser,
					TimeTableFieldDef.logValue, TimeTableFieldDef.billable);
		} else if (groupBy.equals(GROUPBY_DATE)) {
			return Arrays.asList(TimeTableFieldDef.summary,
					TimeTableFieldDef.logUser, TimeTableFieldDef.project,
					TimeTableFieldDef.logValue, TimeTableFieldDef.billable);
		} else if (groupBy.equals(GROUPBY_USER)) {
			return Arrays.asList(TimeTableFieldDef.summary,
					TimeTableFieldDef.logForDate, TimeTableFieldDef.project,
					TimeTableFieldDef.logValue, TimeTableFieldDef.billable);
		} else {
			throw new MyCollabException("Do not support view type: " + groupBy);
		}
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

		searchCriteria.setRangeDate(new RangeDateSearchField(fromDate, toDate));
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private void searchTimeReporting() {
		final Collection<String> selectedUsers = (Collection<String>) this.userField
				.getValue();
		if (selectedUsers != null && selectedUsers.size() > 0) {
			searchCriteria.setLogUsers(new SetSearchField(SearchField.AND,
					selectedUsers));
		} else {
			searchCriteria.setLogUsers(new SetSearchField(SearchField.AND,
					this.userField.getUsernames()));
		}

		final Collection<Integer> selectedProjects = (Collection<Integer>) this.projectField
				.getValue();
		if (selectedProjects != null && selectedProjects.size() > 0) {
			searchCriteria.setProjectIds(new SetSearchField(SearchField.AND,
					selectedProjects));
		} else {
			searchCriteria.setProjectIds(new SetSearchField(SearchField.AND,
					this.projectField.getProjectIds()));
		}

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

		timeTrackingWrapper.removeAllComponents();

		AbstractTimeTrackingDisplayComp timeDisplayComp = buildTimeTrackingComp();
		timeTrackingWrapper.addComponent(timeDisplayComp);
		timeDisplayComp.queryData(searchCriteria,
				(Order) this.orderField.getValue());
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

	private class UserInvolvedProjectsListSelect extends ListSelect {
		private static final long serialVersionUID = 1L;

		private List<SimpleProject> projects;

		public UserInvolvedProjectsListSelect() {
			projects = ApplicationContextUtil.getSpringBean(
					ProjectService.class).getProjectsUserInvolved(
					AppContext.getUsername(), AppContext.getAccountId());

			for (SimpleProject project : projects) {
				this.addItem(project.getId());
				this.setItemCaption(project.getId(), project.getName());
			}
		}

		public List<Integer> getProjectIds() {
			List<Integer> keys = new ArrayList<Integer>();
			for (SimpleProject project : projects) {
				keys.add(project.getId());
			}
			return keys;
		}
	}

	private class UserInvolvedProjectsMemberListSelect extends ListSelect {
		private static final long serialVersionUID = 1L;

		private List<SimpleUser> users;

		public UserInvolvedProjectsMemberListSelect(List<Integer> projectIds) {
			users = ApplicationContextUtil.getSpringBean(
					ProjectMemberService.class).getActiveUsersInProjects(
					projectIds, AppContext.getAccountId());

			for (SimpleUser user : users) {
				this.addItem(user.getUsername());
				this.setItemCaption(user.getUsername(), user.getDisplayName());
			}
		}

		public List<String> getUsernames() {
			List<String> keys = new ArrayList<String>();
			for (SimpleUser user : users) {
				keys.add(user.getUsername());
			}
			return keys;
		}
	}
}
