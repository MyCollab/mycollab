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

import java.util.GregorianCalendar;
import java.util.List;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.file.domain.criteria.FileSearchCriteria;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.domain.Problem;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.ProjectRole;
import com.esofthead.mycollab.module.project.domain.Risk;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleStandupReport;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.domain.TaskList;
import com.esofthead.mycollab.module.project.domain.criteria.MessageSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.MilestoneSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.ProblemSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectMemberSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.RiskSearchCriteria;
import com.esofthead.mycollab.module.project.domain.criteria.StandupReportSearchCriteria;
import com.esofthead.mycollab.module.project.events.BugComponentEvent;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.events.BugVersionEvent;
import com.esofthead.mycollab.module.project.events.FollowingTicketEvent;
import com.esofthead.mycollab.module.project.events.MessageEvent;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.events.PageEvent;
import com.esofthead.mycollab.module.project.events.ProblemEvent;
import com.esofthead.mycollab.module.project.events.ProjectContentEvent;
import com.esofthead.mycollab.module.project.events.ProjectContentEvent.GotoDashboard;
import com.esofthead.mycollab.module.project.events.ProjectContentEvent.Search;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.events.ProjectRoleEvent;
import com.esofthead.mycollab.module.project.events.RiskEvent;
import com.esofthead.mycollab.module.project.events.StandUpEvent;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.events.TimeTrackingEvent;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.service.StandupReportService;
import com.esofthead.mycollab.module.project.view.file.IFilePresenter;
import com.esofthead.mycollab.module.project.view.message.MessagePresenter;
import com.esofthead.mycollab.module.project.view.parameters.BugFilterParameter;
import com.esofthead.mycollab.module.project.view.parameters.BugScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ComponentScreenData;
import com.esofthead.mycollab.module.project.view.parameters.FileScreenData;
import com.esofthead.mycollab.module.project.view.parameters.FollowingTicketsScreenData;
import com.esofthead.mycollab.module.project.view.parameters.MessageScreenData;
import com.esofthead.mycollab.module.project.view.parameters.MilestoneScreenData;
import com.esofthead.mycollab.module.project.view.parameters.PageScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProblemScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectMemberScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectRoleScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.project.view.parameters.RiskScreenData;
import com.esofthead.mycollab.module.project.view.parameters.StandupScreenData;
import com.esofthead.mycollab.module.project.view.parameters.TaskFilterParameter;
import com.esofthead.mycollab.module.project.view.parameters.TaskGroupScreenData;
import com.esofthead.mycollab.module.project.view.parameters.TaskScreenData;
import com.esofthead.mycollab.module.project.view.parameters.VersionScreenData;
import com.esofthead.mycollab.module.project.view.problem.IProblemPresenter;
import com.esofthead.mycollab.module.project.view.user.ProjectDashboardPresenter;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.domain.SimpleBug;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.module.tracker.domain.criteria.ComponentSearchCriteria;
import com.esofthead.mycollab.module.tracker.domain.criteria.VersionSearchCriteria;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.IController;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectController implements IController {
	private static final long serialVersionUID = 1L;

	private ProjectModule container;
	private EventBus eventBus;

	public ProjectController(ProjectModule container) {
		this.container = container;
		this.eventBus = EventBusFactory.getInstance();
		bindProjectEvents();
		bindFollowingTicketEvents();
		bindTimeTrackingEvents();
		bindRiskEvents();
		bindPageEvents();
		bindProblemEvents();
		bindTaskListEvents();
		bindTaskEvents();
		bindBugEvents();
		bindMessageEvents();
		bindMilestoneEvents();
		bindFileEvents();
		bindStandupEvents();
		bindUserGroupEvents();
	}

	@SuppressWarnings("serial")
	private void bindProjectEvents() {

		eventBus.register(new ApplicationEventListener<ProjectEvent.GotoEdit>() {

			@Subscribe
			@Override
			public void handle(ProjectEvent.GotoEdit event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);

				SimpleProject project = (SimpleProject) event.getData();
				CurrentProjectVariables.setProject(project);
				ProjectDashboardPresenter presenter = PresenterResolver
						.getPresenter(ProjectDashboardPresenter.class);
				presenter.go(projectView, new ProjectScreenData.Edit(project));
			}
		});

		eventBus.register(new ApplicationEventListener<ProjectEvent.GotoMyProject>() {

			@Subscribe
			@Override
			public void handle(ProjectEvent.GotoMyProject event) {
				ProjectViewPresenter presenter = PresenterResolver
						.getPresenter(ProjectViewPresenter.class);
				presenter.handleChain(container,
						(PageActionChain) event.getData());
			}
		});
	}

	private void bindFollowingTicketEvents() {
		eventBus.register(new ApplicationEventListener<FollowingTicketEvent.GotoMyFollowingItems>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(FollowingTicketEvent.GotoMyFollowingItems event) {
				FollowingTicketPresenter presenter = PresenterResolver
						.getPresenter(FollowingTicketPresenter.class);
				presenter.go(container,
						new FollowingTicketsScreenData.GotoMyFollowingItems(
								(List<Integer>) event.getData()));
			}
		});
	}

	private void bindTimeTrackingEvents() {
		eventBus.register(new ApplicationEventListener<TimeTrackingEvent.GotoTimeTrackingView>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TimeTrackingEvent.GotoTimeTrackingView event) {
				TimeTrackingSummaryPresenter presenter = PresenterResolver
						.getPresenter(TimeTrackingSummaryPresenter.class);
				presenter.go(container, null);
			}
		});
	}

	private void bindTaskListEvents() {
		eventBus.register(new ApplicationEventListener<TaskListEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskListEvent.GotoRead event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				TaskGroupScreenData.Read data = new TaskGroupScreenData.Read(
						(Integer) event.getData());
				projectView.gotoTaskList(data);
			}
		});

		eventBus.register(new ApplicationEventListener<TaskListEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskListEvent.GotoEdit event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				TaskGroupScreenData.Edit data = new TaskGroupScreenData.Edit(
						(TaskList) event.getData());
				projectView.gotoTaskList(data);
			}
		});

		eventBus.register(new ApplicationEventListener<TaskListEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskListEvent.GotoAdd event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				TaskList taskList = new TaskList();
				taskList.setProjectid(CurrentProjectVariables.getProjectId());
				taskList.setStatus("Open");
				TaskGroupScreenData.Add data = new TaskGroupScreenData.Add(
						taskList);
				projectView.gotoTaskList(data);
			}
		});

		eventBus.register(new ApplicationEventListener<TaskListEvent.GotoTaskListScreen>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskListEvent.GotoTaskListScreen event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				projectView.gotoTaskList(null);
			}
		});

		eventBus.register(new ApplicationEventListener<TaskListEvent.ReoderTaskList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskListEvent.ReoderTaskList event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				TaskGroupScreenData.ReorderTaskListRequest data = new TaskGroupScreenData.ReorderTaskListRequest();
				projectView.gotoTaskList(data);
			}
		});

		eventBus.register(new ApplicationEventListener<TaskListEvent.GotoGanttChartView>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskListEvent.GotoGanttChartView event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				TaskGroupScreenData.GotoGanttChartView data = new TaskGroupScreenData.GotoGanttChartView();
				projectView.gotoTaskList(data);
			}
		});

	}

	private void bindTaskEvents() {
		eventBus.register(new ApplicationEventListener<TaskEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskEvent.GotoRead event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				TaskScreenData.Read data = new TaskScreenData.Read(
						(Integer) event.getData());
				projectView.gotoTaskList(data);
			}
		});

		eventBus.register(new ApplicationEventListener<TaskEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskEvent.GotoAdd event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				TaskScreenData.Add data = new TaskScreenData.Add(new Task());
				projectView.gotoTaskList(data);
			}
		});

		eventBus.register(new ApplicationEventListener<TaskEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskEvent.GotoEdit event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				TaskScreenData.Edit data = new TaskScreenData.Edit((Task) event
						.getData());
				projectView.gotoTaskList(data);
			}
		});

		eventBus.register(new ApplicationEventListener<TaskEvent.Filter>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskEvent.Filter event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				TaskScreenData.Filter data = new TaskScreenData.Filter(
						(TaskFilterParameter) event.getData());
				projectView.gotoTaskList(data);
			}
		});

		eventBus.register(new ApplicationEventListener<TaskEvent.Search>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskEvent.Search event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				TaskScreenData.Search data = new TaskScreenData.Search(
						(TaskFilterParameter) event.getData());
				projectView.gotoTaskList(data);
			}
		});

	}

	@SuppressWarnings("serial")
	private void bindRiskEvents() {
		eventBus.register(new ApplicationEventListener<RiskEvent.GotoAdd>() {

			@Subscribe
			@Override
			public void handle(RiskEvent.GotoAdd event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				RiskScreenData.Add data = new RiskScreenData.Add(new Risk());
				projectView.gotoRiskView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<RiskEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(RiskEvent.GotoEdit event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				RiskScreenData.Edit data = new RiskScreenData.Edit((Risk) event
						.getData());
				projectView.gotoRiskView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<RiskEvent.GotoRead>() {
			@Subscribe
			@Override
			public void handle(RiskEvent.GotoRead event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				RiskScreenData.Read data = new RiskScreenData.Read(
						(Integer) event.getData());
				projectView.gotoRiskView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<RiskEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(RiskEvent.GotoList event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);

				RiskSearchCriteria criteria = new RiskSearchCriteria();

				criteria.setProjectId(new NumberSearchField(SearchField.AND,
						CurrentProjectVariables.getProjectId()));
				projectView.gotoRiskView(new RiskScreenData.Search(criteria));
			}
		});
	}

	@SuppressWarnings("serial")
	private void bindProblemEvents() {
		eventBus.register(new ApplicationEventListener<ProblemEvent.GotoAdd>() {
			@Subscribe
			@Override
			public void handle(ProblemEvent.GotoAdd event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				ProblemScreenData.Add data = new ProblemScreenData.Add(
						new Problem());
				IProblemPresenter presenter = PresenterResolver
						.getPresenter(IProblemPresenter.class);
				presenter.go(projectView, data);
			}
		});

		eventBus.register(new ApplicationEventListener<ProblemEvent.GotoRead>() {
			@Subscribe
			@Override
			public void handle(ProblemEvent.GotoRead event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				ProblemScreenData.Read data = new ProblemScreenData.Read(
						(Integer) event.getData());
				IProblemPresenter presenter = PresenterResolver
						.getPresenter(IProblemPresenter.class);
				presenter.go(projectView, data);
			}
		});

		eventBus.register(new ApplicationEventListener<ProblemEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(ProblemEvent.GotoList event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);

				ProblemSearchCriteria criteria = new ProblemSearchCriteria();

				criteria.setProjectId(new NumberSearchField(SearchField.AND,
						CurrentProjectVariables.getProjectId()));
				ProblemScreenData.Search data = new ProblemScreenData.Search(
						criteria);
				IProblemPresenter presenter = PresenterResolver
						.getPresenter(IProblemPresenter.class);
				presenter.go(projectView, data);
			}
		});

		eventBus.register(new ApplicationEventListener<ProblemEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(ProblemEvent.GotoEdit event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				ProblemScreenData.Edit data = new ProblemScreenData.Edit(
						(Problem) event.getData());
				IProblemPresenter presenter = PresenterResolver
						.getPresenter(IProblemPresenter.class);
				presenter.go(projectView, data);
			}
		});
	}

	@SuppressWarnings("serial")
	private void bindBugEvents() {
		eventBus.register(new ApplicationEventListener<BugEvent.GotoDashboard>() {
			@Subscribe
			@Override
			public void handle(BugEvent.GotoDashboard event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				projectView.gotoBugView(null);
			}
		});

		eventBus.register(new ApplicationEventListener<BugEvent.GotoAdd>() {
			@Subscribe
			@Override
			public void handle(BugEvent.GotoAdd event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				BugScreenData.Add data = new BugScreenData.Add(new SimpleBug());
				projectView.gotoBugView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<BugEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(BugEvent.GotoEdit event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				BugScreenData.Edit data = new BugScreenData.Edit(
						(SimpleBug) event.getData());
				projectView.gotoBugView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<BugEvent.GotoRead>() {
			@Subscribe
			@Override
			public void handle(BugEvent.GotoRead event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				BugScreenData.Read data = new BugScreenData.Read(
						(Integer) event.getData());
				projectView.gotoBugView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<BugEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(BugEvent.GotoList event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);

				Object params = event.getData();
				if (params == null) {
					BugSearchCriteria criteria = new BugSearchCriteria();

					criteria.setProjectId(new NumberSearchField(
							SearchField.AND, CurrentProjectVariables
									.getProjectId()));
					criteria.setStatuses(new SetSearchField<String>(
							SearchField.AND, new String[] {
									BugStatus.InProgress.name(),
									BugStatus.Open.name(),
									BugStatus.ReOpened.name() }));
					BugFilterParameter parameter = new BugFilterParameter(
							"Open Bugs", criteria);
					projectView
							.gotoBugView(new BugScreenData.Search(parameter));
				} else if (params instanceof BugScreenData.Search) {
					projectView.gotoBugView((BugScreenData.Search) params);
				} else {
					throw new MyCollabException("Invalid search parameter: "
							+ BeanUtility.printBeanObj(params));
				}

			}
		});

		eventBus.register(new ApplicationEventListener<BugComponentEvent.GotoAdd>() {
			@Subscribe
			@Override
			public void handle(BugComponentEvent.GotoAdd event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				ComponentScreenData.Add data = new ComponentScreenData.Add(
						new Component());
				projectView.gotoBugView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<BugComponentEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(BugComponentEvent.GotoEdit event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				ComponentScreenData.Edit data = new ComponentScreenData.Edit(
						(Component) event.getData());
				projectView.gotoBugView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<BugComponentEvent.GotoRead>() {
			@Subscribe
			@Override
			public void handle(BugComponentEvent.GotoRead event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				ComponentScreenData.Read data = new ComponentScreenData.Read(
						(Integer) event.getData());
				projectView.gotoBugView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<BugComponentEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(BugComponentEvent.GotoList event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				ComponentSearchCriteria criteria = new ComponentSearchCriteria();
				criteria.setProjectid(new NumberSearchField(
						CurrentProjectVariables.getProjectId()));
				projectView
						.gotoBugView(new ComponentScreenData.Search(criteria));
			}
		});

		eventBus.register(new ApplicationEventListener<BugVersionEvent.GotoAdd>() {
			@Subscribe
			@Override
			public void handle(BugVersionEvent.GotoAdd event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				VersionScreenData.Add data = new VersionScreenData.Add(
						new Version());
				projectView.gotoBugView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<BugVersionEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(BugVersionEvent.GotoEdit event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				VersionScreenData.Edit data = new VersionScreenData.Edit(
						(Version) event.getData());
				projectView.gotoBugView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<BugVersionEvent.GotoRead>() {
			@Subscribe
			@Override
			public void handle(BugVersionEvent.GotoRead event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				VersionScreenData.Read data = new VersionScreenData.Read(
						(Integer) event.getData());
				projectView.gotoBugView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<BugVersionEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(BugVersionEvent.GotoList event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				VersionSearchCriteria criteria = new VersionSearchCriteria();
				criteria.setProjectId(new NumberSearchField(
						CurrentProjectVariables.getProjectId()));
				projectView.gotoBugView(new VersionScreenData.Search(criteria));
			}
		});
	}

	private void bindMessageEvents() {
		eventBus.register(new ApplicationEventListener<MessageEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(MessageEvent.GotoRead event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				MessageScreenData.Read data = new MessageScreenData.Read(
						(Integer) event.getData());
				MessagePresenter presenter = PresenterResolver
						.getPresenter(MessagePresenter.class);
				presenter.go(projectView, data);
			}
		});

		eventBus.register(new ApplicationEventListener<MessageEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(MessageEvent.GotoList event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				MessageSearchCriteria searchCriteria = new MessageSearchCriteria();
				searchCriteria.setProjectids(new SetSearchField<Integer>(
						CurrentProjectVariables.getProjectId()));
				MessageScreenData.Search data = new MessageScreenData.Search(
						searchCriteria);
				MessagePresenter presenter = PresenterResolver
						.getPresenter(MessagePresenter.class);
				presenter.go(projectView, data);
			}
		});
	}

	private void bindMilestoneEvents() {
		eventBus.register(new ApplicationEventListener<MilestoneEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(MilestoneEvent.GotoAdd event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				MilestoneScreenData.Add data = new MilestoneScreenData.Add(
						new Milestone());
				projectView.gotoMilestoneView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<MilestoneEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(MilestoneEvent.GotoRead event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				MilestoneScreenData.Read data = new MilestoneScreenData.Read(
						(Integer) event.getData());
				projectView.gotoMilestoneView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<MilestoneEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(MilestoneEvent.GotoList event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);

				MilestoneSearchCriteria criteria = new MilestoneSearchCriteria();

				criteria.setProjectId(new NumberSearchField(SearchField.AND,
						CurrentProjectVariables.getProjectId()));
				projectView.gotoMilestoneView(new MilestoneScreenData.Search(
						criteria));
			}
		});

		eventBus.register(new ApplicationEventListener<MilestoneEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(MilestoneEvent.GotoEdit event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				MilestoneScreenData.Edit data = new MilestoneScreenData.Edit(
						(Milestone) event.getData());
				projectView.gotoMilestoneView(data);
			}
		});
	}

	private void bindPageEvents() {
		eventBus.register(new ApplicationEventListener<PageEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(PageEvent.GotoAdd event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				PageScreenData.Add data = new PageScreenData.Add(new Page());
				projectView.gotoPageView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<PageEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(PageEvent.GotoEdit event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				PageScreenData.Edit data = new PageScreenData.Edit((Page) event
						.getData());
				projectView.gotoPageView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<PageEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(PageEvent.GotoRead event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				PageScreenData.Read data = new PageScreenData.Read((Page) event
						.getData());
				projectView.gotoPageView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<PageEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(PageEvent.GotoList event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);

				projectView.gotoPageView(new PageScreenData.Search());
			}
		});
	}

	private void bindStandupEvents() {
		eventBus.register(new ApplicationEventListener<StandUpEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(StandUpEvent.GotoAdd event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				StandupReportService reportService = ApplicationContextUtil
						.getSpringBean(StandupReportService.class);
				SimpleStandupReport report = reportService
						.findStandupReportByDateUser(
								CurrentProjectVariables.getProjectId(),
								AppContext.getUsername(),
								new GregorianCalendar().getTime(),
								AppContext.getAccountId());
				if (report == null) {
					report = new SimpleStandupReport();
				}
				StandupScreenData.Add data = new StandupScreenData.Add(report);
				projectView.gotoStandupReportView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<StandUpEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(StandUpEvent.GotoRead event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				StandupScreenData.Read data = new StandupScreenData.Read(
						(Integer) event.getData());
				projectView.gotoStandupReportView(data);
			}
		});

		eventBus.register(new ApplicationEventListener<StandUpEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(StandUpEvent.GotoList event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);

				StandupReportSearchCriteria criteria = new StandupReportSearchCriteria();

				criteria.setProjectId(new NumberSearchField(SearchField.AND,
						CurrentProjectVariables.getProjectId()));
				criteria.setOnDate(new DateSearchField(SearchField.AND,
						new GregorianCalendar().getTime()));
				projectView.gotoStandupReportView(new StandupScreenData.Search(
						criteria));
			}
		});
	}

	private void bindFileEvents() {
		eventBus.register(new ApplicationEventListener<ProjectContentEvent.GotoDashboard>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(GotoDashboard event) {
				IFilePresenter presenter = PresenterResolver
						.getPresenter(IFilePresenter.class);
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				presenter.go(projectView, new FileScreenData.GotoDashboard());
			}

		});

		eventBus.register(new ApplicationEventListener<ProjectContentEvent.Search>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(Search event) {
				IFilePresenter presenter = PresenterResolver
						.getPresenter(IFilePresenter.class);
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				presenter.go(projectView, new FileScreenData.Search(
						(FileSearchCriteria) event.getData()));
			}

		});
	}

	private void bindUserGroupEvents() {

		eventBus.register(new ApplicationEventListener<ProjectRoleEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectRoleEvent.GotoList event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);

				SimpleProject project = CurrentProjectVariables.getProject();
				ProjectRoleSearchCriteria criteria = new ProjectRoleSearchCriteria();
				criteria.setProjectId(new NumberSearchField(project.getId()));
				projectView.gotoUsersAndGroup(new ProjectRoleScreenData.Search(
						criteria));
			}
		});

		eventBus.register(new ApplicationEventListener<ProjectRoleEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectRoleEvent.GotoAdd event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				ProjectRoleScreenData.Add data = new ProjectRoleScreenData.Add(
						new ProjectRole());
				projectView.gotoUsersAndGroup(data);
			}
		});

		eventBus.register(new ApplicationEventListener<ProjectRoleEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectRoleEvent.GotoEdit event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				ProjectRoleScreenData.Add data = new ProjectRoleScreenData.Add(
						(ProjectRole) event.getData());
				projectView.gotoUsersAndGroup(data);
			}
		});

		eventBus.register(new ApplicationEventListener<ProjectRoleEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectRoleEvent.GotoRead event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				ProjectRoleScreenData.Read data = new ProjectRoleScreenData.Read(
						(Integer) event.getData());
				projectView.gotoUsersAndGroup(data);
			}
		});

		eventBus.register(new ApplicationEventListener<ProjectMemberEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectMemberEvent.GotoList event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);

				SimpleProject project = CurrentProjectVariables.getProject();
				ProjectMemberSearchCriteria criteria = new ProjectMemberSearchCriteria();
				criteria.setProjectId(new NumberSearchField(project.getId()));
				criteria.setSaccountid(new NumberSearchField(AppContext
						.getAccountId()));
				criteria.setStatus(new StringSearchField(
						ProjectMemberStatusConstants.ACTIVE));
				projectView
						.gotoUsersAndGroup(new ProjectMemberScreenData.Search(
								criteria));
			}
		});

		eventBus.register(new ApplicationEventListener<ProjectMemberEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectMemberEvent.GotoRead event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				ProjectMemberScreenData.Read data = new ProjectMemberScreenData.Read(
						event.getData());
				projectView.gotoUsersAndGroup(data);
			}
		});

		eventBus.register(new ApplicationEventListener<ProjectMemberEvent.GotoInviteMembers>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectMemberEvent.GotoInviteMembers event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				ProjectMemberScreenData.InviteProjectMembers data = new ProjectMemberScreenData.InviteProjectMembers();
				projectView.gotoUsersAndGroup(data);
			}
		});

		eventBus.register(new ApplicationEventListener<ProjectMemberEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectMemberEvent.GotoEdit event) {
				ProjectView projectView = ViewManager
						.getView(ProjectView.class);
				ProjectMemberScreenData.Add data = new ProjectMemberScreenData.Add(
						(ProjectMember) event.getData());
				projectView.gotoUsersAndGroup(data);
			}
		});
	}
}
