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

import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.i18n.TaskGroupI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectTaskService;
import com.esofthead.mycollab.module.project.view.task.calendar.GenericTaskEvent;
import com.esofthead.mycollab.module.project.view.task.calendar.GenericTaskProvider;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractPageView;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.ui.ToggleButtonGroup;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.components.calendar.CalendarComponentEvents;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import com.vaadin.ui.components.calendar.handler.BasicEventMoveHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventResizeHandler;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.vaadin.peter.buttongroup.ButtonGroup;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * @author MyCollab Ltd
 * @since 5.2.0
 */
@ViewComponent
public class CalendarViewImpl extends AbstractPageView implements CalendarView {
    private static final DateTimeFormatter MY_FORMATTER = DateTimeFormat.forPattern("MMMM, yyyy");

    private ApplicationEventListener<TaskEvent.NewTaskAdded> taskChangeHandler = new ApplicationEventListener<TaskEvent.NewTaskAdded>() {
        @Override
        @Subscribe
        public void handle(TaskEvent.NewTaskAdded event) {
            Integer taskId = (Integer) event.getData();
            ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
            SimpleTask task = taskService.findById(taskId, AppContext.getAccountId());
            GenericTaskEvent taskEvent = new GenericTaskEvent(task);
            GenericTaskProvider provider = (GenericTaskProvider) calendar.getEventProvider();
            if (provider.containsEvent(taskEvent)) {
                provider.removeEvent(taskEvent);
                provider.addEvent(taskEvent);
            } else {
                provider.addEvent(taskEvent);
            }
        }
    };

    private Label headerLbl, billableHoursLbl, nonBillableHoursLbl, assignMeLbl, assignOtherLbl, nonAssigneeLbl;
    private Calendar calendar;
    private LocalDate baseDate;

    public CalendarViewImpl() {
        this.withMargin(true).withSpacing(true);
        baseDate = new LocalDate();
    }

    @Override
    public void attach() {
        EventBusFactory.getInstance().register(taskChangeHandler);
        super.attach();
    }

    @Override
    public void detach() {
        EventBusFactory.getInstance().unregister(taskChangeHandler);
        super.detach();
    }

    @Override
    public void display() {
        calendar = new Calendar();
        calendar.addStyleName("assignment-calendar");
        calendar.setWidth("100%");
        calendar.setHeight("100%");
        calendar.setHandler(new CalendarComponentEvents.EventClickHandler() {
            @Override
            public void eventClick(CalendarComponentEvents.EventClick event) {
                if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                    GenericTaskEvent calendarEvent = (GenericTaskEvent) event.getCalendarEvent();
                    SimpleTask task = calendarEvent.getAssignment();
                    UI.getCurrent().addWindow(new TaskAddWindow(task));
                }
            }
        });

        calendar.setHandler(new CalendarComponentEvents.DateClickHandler() {
            @Override
            public void dateClick(CalendarComponentEvents.DateClickEvent dateClickEvent) {
                if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                    SimpleTask task = new SimpleTask();
                    task.setStartdate(dateClickEvent.getDate());
                    task.setEnddate(dateClickEvent.getDate());
                    UI.getCurrent().addWindow(new TaskAddWindow(task));
                }
            }
        });

        calendar.setHandler(new BasicEventMoveHandler() {
            @Override
            public void eventMove(CalendarComponentEvents.MoveEvent event) {
                if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                    super.eventMove(event);
                    GenericTaskEvent calendarEvent = (GenericTaskEvent) event.getCalendarEvent();
                    SimpleTask task = calendarEvent.getAssignment();
                    ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                    taskService.updateWithSession(task, AppContext.getUsername());
                }
            }
        });

        calendar.setHandler(new BasicEventResizeHandler() {
            @Override
            public void eventResize(CalendarComponentEvents.EventResize event) {
                if (CurrentProjectVariables.canWrite(ProjectRolePermissionCollections.TASKS)) {
                    super.eventResize(event);
                    GenericTaskEvent calendarEvent = (GenericTaskEvent) event.getCalendarEvent();
                    SimpleTask task = calendarEvent.getAssignment();
                    ProjectTaskService taskService = ApplicationContextUtil.getSpringBean(ProjectTaskService.class);
                    taskService.updateWithSession(task, AppContext.getUsername());
                }
            }
        });
        MHorizontalLayout noteContainer = new MHorizontalLayout().withMargin(new MarginInfo(true, false, true, false))
                .withWidth("100%");
        MVerticalLayout helpBlock = new MVerticalLayout().withMargin(false);
        assignMeLbl = new ELabel("").withStyleName("owner").withWidth("200px");
        assignOtherLbl = new ELabel("").withStyleName("otheruser").withWidth("200px");
        nonAssigneeLbl = new ELabel("").withStyleName("nonowner").withWidth("200px");
        helpBlock.with(assignMeLbl, assignOtherLbl, nonAssigneeLbl);
        billableHoursLbl = new Label("", ContentMode.HTML);
        nonBillableHoursLbl = new Label("", ContentMode.HTML);
        MVerticalLayout hoursNoteContainer = new MVerticalLayout().withMargin(false);
        hoursNoteContainer.with(billableHoursLbl, nonBillableHoursLbl);
        hoursNoteContainer.setWidthUndefined();
        noteContainer.with(helpBlock, hoursNoteContainer).withAlign(helpBlock, Alignment.TOP_LEFT)
                .withAlign(hoursNoteContainer, Alignment.TOP_RIGHT);
        this.with(buildHeader(), noteContainer, calendar);
        displayMonthView();
    }

    private MHorizontalLayout buildHeader() {
        MHorizontalLayout header = new MHorizontalLayout().withWidth("100%");

        MHorizontalLayout headerLeftContainer = new MHorizontalLayout();
        Button todayBtn = new Button("Today", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                baseDate = new LocalDate();
                displayMonthView();
            }
        });
        todayBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        ButtonGroup navigationBtns = new ButtonGroup();
        navigationBtns.setStyleName("navigation-btns");
        Button previousBtn = new Button("", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                baseDate = baseDate.minusMonths(1);
                displayMonthView();
            }
        });
        previousBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        previousBtn.setIcon(FontAwesome.CHEVRON_LEFT);
        navigationBtns.addButton(previousBtn);

        Button nextBtn = new Button("", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                baseDate = baseDate.plusMonths(1);
                displayMonthView();
            }
        });
        nextBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        nextBtn.setIcon(FontAwesome.CHEVRON_RIGHT);
        navigationBtns.addButton(nextBtn);

        headerLeftContainer.with(todayBtn, navigationBtns);
        header.with(headerLeftContainer).withAlign(headerLeftContainer, Alignment.MIDDLE_LEFT);

        CssLayout titleWrapper = new CssLayout();
        headerLbl = new Label();
        headerLbl.setStyleName("h1");
        titleWrapper.addComponent(headerLbl);
        Button advanceDisplayBtn = new Button(null, new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new TaskEvent.GotoDashboard(CalendarViewImpl.this, null));
            }
        });
        advanceDisplayBtn.setIcon(FontAwesome.SITEMAP);
        advanceDisplayBtn.setDescription(AppContext.getMessage(TaskGroupI18nEnum.ADVANCED_VIEW_TOOLTIP));

        Button calendarBtn = new Button();
        calendarBtn.setDescription("Calendar View");
        calendarBtn.setIcon(FontAwesome.CALENDAR);

        Button chartDisplayBtn = new Button(null, new Button.ClickListener() {
            private static final long serialVersionUID = -5707546605789537298L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                EventBusFactory.getInstance().post(new TaskEvent.GotoGanttChart(CalendarViewImpl.this, null));
            }
        });
        chartDisplayBtn.setDescription("Display Gantt chart");
        chartDisplayBtn.setIcon(FontAwesome.BAR_CHART_O);

        Button kanbanBtn = new Button(null, new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EventBusFactory.getInstance().post(new TaskEvent.GotoKanbanView(CalendarViewImpl.this, null));
            }
        });
        kanbanBtn.setDescription("Kanban View");
        kanbanBtn.setIcon(FontAwesome.TH);

        ToggleButtonGroup viewButtons = new ToggleButtonGroup();
        viewButtons.addButton(advanceDisplayBtn);
        viewButtons.addButton(calendarBtn);
        viewButtons.addButton(kanbanBtn);
        viewButtons.addButton(chartDisplayBtn);
        viewButtons.setDefaultButton(calendarBtn);

        header.with(titleWrapper, viewButtons).withAlign(titleWrapper, Alignment.MIDDLE_CENTER).withAlign(viewButtons,
                Alignment.MIDDLE_RIGHT);
        return header;
    }

    private void displayMonthView() {
        LocalDate firstDayOfMonth = baseDate.dayOfMonth().withMinimumValue();
        LocalDate lastDayOfMonth = baseDate.dayOfMonth().withMaximumValue();
        calendar.setStartDate(firstDayOfMonth.toDate());
        calendar.setEndDate(lastDayOfMonth.toDate());
        headerLbl.setValue(baseDate.toString(MY_FORMATTER));
        final GenericTaskProvider provider = new GenericTaskProvider();
        provider.addEventSetChangeListener(new CalendarEventProvider.EventSetChangeListener() {
            @Override
            public void eventSetChange(CalendarEventProvider.EventSetChangeEvent event) {
                assignMeLbl.setValue("Assign to me (" + provider.getAssignMeNum() + ")");
                assignOtherLbl.setValue("Assign to others (" + provider.getAssignOthersNum() + ")");
                nonAssigneeLbl.setValue("Not be assigned (" + provider.getNotAssignNum() + ")");
                billableHoursLbl.setValue(FontAwesome.MONEY.getHtml() + " Billable hours: " + provider
                        .getTotalBillableHours());
                nonBillableHoursLbl.setValue(FontAwesome.GIFT.getHtml() + " Non billable hours: " + provider
                        .getTotalNonBillableHours());
            }
        });
        provider.loadEvents(firstDayOfMonth.toDate(), lastDayOfMonth.toDate());
        calendar.setEventProvider(provider);
    }
}
