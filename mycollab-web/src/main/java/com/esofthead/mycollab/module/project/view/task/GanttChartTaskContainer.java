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
package com.esofthead.mycollab.module.project.view.task;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.tltv.gantt.Gantt;
import org.tltv.gantt.Gantt.MoveEvent;
import org.tltv.gantt.Gantt.ResizeEvent;
import org.tltv.gantt.client.shared.Step;
import org.tltv.gantt.client.shared.Step.CaptionMode;
import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.ProjectResources;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.i18n.TaskI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.1
 * 
 */
class GanttChartTaskContainer extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private List<SimpleTask> taskList;

	private Gantt gantt;
	private LinkedHashMap<Step, SimpleTask> stepMap;
	private NativeSelect reso;

	private TaskTableDisplay taskTable;

	private final ProjectTaskService taskService;

	private DateField start;
	private DateField end;

	public GanttChartTaskContainer() {
		taskService = ApplicationContextUtil
				.getSpringBean(ProjectTaskService.class);
		constructGanttChart();
		Panel controls = createControls();
		this.setStyleName("gantt-view");
		this.addComponent(controls);
		HorizontalLayout mainLayout = new HorizontalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setStyleName("gantt-wrap");
		mainLayout.addComponent(taskTable);
		mainLayout.addComponent(gantt);
		this.addComponent(mainLayout);
	}

	private void constructGanttChart() {
		stepMap = new LinkedHashMap<Step, SimpleTask>();

		taskTable = new TaskTableDisplay(Arrays.asList(
				TaskTableFieldDef.taskname, TaskTableFieldDef.startdate,
				TaskTableFieldDef.duedate, TaskTableFieldDef.assignee));
		taskTable.setWidth("100%");
		taskTable.setHeightUndefined();
		taskTable.addStyleName("gantt-table");

		gantt = new Gantt();
		gantt.setWidth(100, Unit.PERCENTAGE);
		gantt.setResizableSteps(true);
		gantt.setMovableSteps(true);
		gantt.setVerticalScrollDelegateTarget(taskTable);

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -14);

		gantt.setStartDate(cal.getTime());
		cal.add(Calendar.DATE, 28);
		gantt.setEndDate(cal.getTime());

		gantt.addMoveListener(new Gantt.MoveListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onGanttMove(MoveEvent event) {
				// Notification.show("Moved " + event.getStep().getCaption());
				SimpleTask task = stepMap.get(event.getStep());
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());

				GregorianCalendar gc = new GregorianCalendar();

				/* check endate after deadline */
				gc.setTimeInMillis(event.getEndDate());
				if (task.getEnddate() != null
						&& task.getEnddate().after(gc.getTime())) {
					task.setEnddate(null);
				}
				task.setDeadline(gc.getTime());

				gc.setTimeInMillis(event.getStartDate());
				task.setStartdate(gc.getTime());

				taskService.updateWithSession(task, AppContext.getUsername());
				taskTable.setItems(stepMap.values());
			}
		});

		gantt.addResizeListener(new Gantt.ResizeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void onGanttResize(ResizeEvent event) {
				// Notification.show("Resized " + event.getStep().getCaption());
				SimpleTask task = stepMap.get(event.getStep());
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());

				GregorianCalendar gc = new GregorianCalendar();

				/* check endate after deadline */
				gc.setTimeInMillis(event.getEndDate());
				gc.setTimeInMillis(event.getEndDate());
				if (task.getEnddate() != null
						&& task.getEnddate().after(gc.getTime())) {
					task.setEnddate(null);
				}
				task.setDeadline(gc.getTime());

				gc.setTimeInMillis(event.getStartDate());
				task.setStartdate(gc.getTime());

				taskService.updateWithSession(task, AppContext.getUsername());
				taskTable.setItems(stepMap.values());
			}
		});
	}

	public void displayChart() {
		updateStepList();
	}

	@SuppressWarnings("unchecked")
	private void updateStepList() {

		TaskSearchCriteria criteria = new TaskSearchCriteria();
		criteria.setProjectid(new NumberSearchField(CurrentProjectVariables
				.getProjectId()));
		taskList = taskService
				.findPagableListByCriteria(new SearchRequest<TaskSearchCriteria>(
						criteria, 0, Integer.MAX_VALUE));

		/* Clear current Gantt chart */
		if (stepMap != null) {
			for (Step key : stepMap.keySet()) {
				gantt.removeStep(key);
			}
			stepMap = new LinkedHashMap<Step, SimpleTask>();
		}

		/* Add steps */
		if (!taskList.isEmpty()) {

			for (SimpleTask task : taskList) {

				Date startDate = null;
				Date endDate = null;

				/* Check for date */
				if (task.getActualstartdate() != null) {
					startDate = task.getActualstartdate();
				} else if (task.getStartdate() != null) {
					startDate = task.getStartdate();
				}

				if (task.getDeadline() != null) {
					endDate = task.getDeadline();
				} else if (task.getActualenddate() != null) {
					endDate = task.getActualenddate();
				}

				/* Add row block if both stardate and endate avalable */
				if (startDate != null && endDate != null
						&& gantt.getStartDate().before(startDate)
						&& gantt.getEndDate().after(startDate)) {
					Step step = new Step();
					step.setCaption(tooltipGenerate(task));
					step.setCaptionMode(CaptionMode.HTML);
					step.setStartDate(startDate.getTime());
					step.setEndDate(endDate.getTime());

					/* Add style for row block */
					if (task.getPercentagecomplete() != null
							&& 100d == task.getPercentagecomplete()) {
						step.setBackgroundColor("53C540");
						step.setStyleName("completed");
					} else {
						if ("Pending".equals(task.getStatus())) {
							step.setBackgroundColor("e2f852");
						} else if ("Open".equals(task.getStatus())
								&& endDate.before(new GregorianCalendar()
										.getTime())) {
							step.setBackgroundColor("FC4350");
						}
					}
					stepMap.put(step, task);
				}

			}

			taskTable.setItems(stepMap.values());
		}

		if (stepMap != null) {
			for (Step key : stepMap.keySet()) {
				gantt.addStep(key);
			}
		}

	}

	private String tooltipGenerate(SimpleTask task) {
		String content = "";

		// --------------Item hidden div tooltip----------------
		String randomStrId = UUID.randomUUID().toString();
		String idDivSeverData = "projectOverViewserverdata" + randomStrId + "";
		String idToopTipDiv = "projectOverViewtooltip" + randomStrId + "";
		String idStickyToolTipDiv = "projectOverViewmystickyTooltip"
				+ randomStrId;
		String idtagA = "projectOverViewtagA" + randomStrId;

		String arg0 = ProjectResources
				.getResourceLink(ProjectTypeConstants.TASK);
		String arg1 = idtagA;
		String arg2 = ProjectLinkGenerator.generateTaskPreviewLink(
				task.getTaskkey(), task.getProjectShortname());
		String arg3 = "'" + randomStrId + "'";
		String arg4 = "'" + ProjectTypeConstants.TASK + "'";
		String arg5 = "'" + task.getId() + "'";
		String arg6 = "'" + AppContext.getSiteUrl() + "tooltip/'";
		String arg7 = "'" + task.getSaccountid() + "'";
		String arg8 = "'" + AppContext.getSiteUrl() + "'";
		String arg9 = AppContext.getSession().getTimezone();
		String arg10 = "'" + AppContext.getUserLocale().toString() + "'";
		String arg11 = task.getTaskname();

		String arg12 = idStickyToolTipDiv;
		String arg13 = idToopTipDiv;
		String arg14 = idDivSeverData;

		content = AppContext.getMessage(
				ProjectCommonI18nEnum.TOOLTIP_GANTT_CHART_TITLE, arg0, arg1,
				arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11,
				arg12, arg13, arg14);
		return content;
	}

	private Panel createControls() {

		Panel panel = new Panel();
		panel.setWidth(100, Unit.PERCENTAGE);

		HorizontalLayout controls = new HorizontalLayout();
		controls.setSpacing(true);
		controls.setMargin(true);
		panel.setContent(controls);

		start = new DateField(
				AppContext.getMessage(TaskI18nEnum.FORM_START_DATE));
		start.setValue(gantt.getStartDate());
		start.setResolution(Resolution.DAY);
		start.setImmediate(true);
		start.addValueChangeListener(startDateValueChangeListener);

		end = new DateField(AppContext.getMessage(TaskI18nEnum.FORM_END_DATE));
		end.setValue(gantt.getEndDate());
		end.setResolution(Resolution.DAY);
		end.setImmediate(true);
		end.addValueChangeListener(endDateValueChangeListener);

		reso = new NativeSelect("Resolution");
		reso.setNullSelectionAllowed(false);
		reso.addItem(org.tltv.gantt.client.shared.Resolution.Hour);
		reso.addItem(org.tltv.gantt.client.shared.Resolution.Day);
		reso.addItem(org.tltv.gantt.client.shared.Resolution.Week);
		reso.setValue(gantt.getResolution());
		reso.setImmediate(true);
		reso.addValueChangeListener(resolutionValueChangeListener);

		controls.addComponent(start);
		controls.addComponent(end);
		controls.addComponent(reso);
		panel.setStyleName(UIConstants.THEME_NO_BORDER);

		return panel;
	}

	private ValueChangeListener startDateValueChangeListener = new ValueChangeListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			gantt.setStartDate((Date) event.getProperty().getValue());
			updateStepList();
		}
	};

	private ValueChangeListener endDateValueChangeListener = new ValueChangeListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			gantt.setEndDate((Date) event.getProperty().getValue());
			updateStepList();
		}
	};

	private ValueChangeListener resolutionValueChangeListener = new ValueChangeListener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void valueChange(ValueChangeEvent event) {
			org.tltv.gantt.client.shared.Resolution res = (org.tltv.gantt.client.shared.Resolution) event
					.getProperty().getValue();
			if (validateResolutionChange(res)) {
				gantt.setResolution(res);
			}
		}
	};

	private boolean validateResolutionChange(
			final org.tltv.gantt.client.shared.Resolution res) {
		long max = 4 * 7 * 24 * 60 * 60000L;
		if (res == org.tltv.gantt.client.shared.Resolution.Hour
				&& (gantt.getEndDate().getTime() - gantt.getStartDate()
						.getTime()) > max) {

			// revert to previous resolution
			setResolution(gantt.getResolution());

			// make user to confirm hour resolution, if the timeline range is
			// more than one week long.

			ConfirmDialogExt
					.show(UI.getCurrent(),
							AppContext
									.getMessage(GenericI18Enum.WINDOW_WARNING_TITLE),
							"Timeline range is a quite long for hour resolution. Rendering may be slow. Continue anyway?",
							AppContext
									.getMessage(GenericI18Enum.BUTTON_YES),
							AppContext
									.getMessage(GenericI18Enum.BUTTON_NO),
							new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void onClose(final ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {

										setResolution(res);
										gantt.setResolution(res);

									}
								}
							});
			return false;
		}
		return true;
	}

	private void setResolution(
			org.tltv.gantt.client.shared.Resolution resolution) {
		reso.removeValueChangeListener(resolutionValueChangeListener);
		try {
			reso.setValue(resolution);
		} finally {
			reso.addValueChangeListener(resolutionValueChangeListener);
		}
	}

}
