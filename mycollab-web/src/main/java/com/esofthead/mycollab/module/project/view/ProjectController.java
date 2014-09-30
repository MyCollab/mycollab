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

import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.arguments.DateSearchField;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.BeanUtility;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.module.page.domain.Page;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectMemberStatusConstants;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.domain.Problem;
import com.esofthead.mycollab.module.project.domain.ProjectMember;
import com.esofthead.mycollab.module.project.domain.ProjectRole;
import com.esofthead.mycollab.module.project.domain.Risk;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleStandupReport;
import com.esofthead.mycollab.module.project.domain.SimpleTask;
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
import com.esofthead.mycollab.module.project.events.CustomizeUIEvent;
import com.esofthead.mycollab.module.project.events.MessageEvent;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.events.PageEvent;
import com.esofthead.mycollab.module.project.events.ProblemEvent;
import com.esofthead.mycollab.module.project.events.ProjectContentEvent;
import com.esofthead.mycollab.module.project.events.ProjectContentEvent.GotoDashboard;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.events.ProjectRoleEvent;
import com.esofthead.mycollab.module.project.events.RiskEvent;
import com.esofthead.mycollab.module.project.events.StandUpEvent;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.i18n.OptionI18nEnum.BugStatus;
import com.esofthead.mycollab.module.project.service.StandupReportService;
import com.esofthead.mycollab.module.project.view.file.IFilePresenter;
import com.esofthead.mycollab.module.project.view.message.MessagePresenter;
import com.esofthead.mycollab.module.project.view.parameters.BugFilterParameter;
import com.esofthead.mycollab.module.project.view.parameters.BugScreenData;
import com.esofthead.mycollab.module.project.view.parameters.ComponentScreenData;
import com.esofthead.mycollab.module.project.view.parameters.FileScreenData;
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
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractController;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.google.common.eventbus.Subscribe;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.4.0
 *
 */
@SuppressWarnings("serial")
public class ProjectController extends AbstractController {
	private ProjectView projectView;

	public ProjectController(ProjectView projectView) {
		this.projectView = projectView;

		bindProjectEvents();
		bindTaskListEvents();
		bindTaskEvents();
		bindRiskEvents();
		bindProblemEvents();
		bindBugEvents();
		bindMessageEvents();
		bindMilestoneEvents();
		bindStandupEvents();
		bindUserGroupEvents();
		bindFileEvents();
		bindPageEvents();
	}

	private void bindProjectEvents() {
		this.register(new ApplicationEventListener<ProjectEvent.GotoEdit>() {

			@Subscribe
			@Override
			public void handle(ProjectEvent.GotoEdit event) {
				SimpleProject project = (SimpleProject) event.getData();
				CurrentProjectVariables.setProject(project);
				ProjectDashboardPresenter presenter = PresenterResolver
						.getPresenter(ProjectDashboardPresenter.class);
				presenter.go(projectView, new ProjectScreenData.Edit(project));
			}
		});
	}

	private void bindTaskListEvents() {
		this.register(new ApplicationEventListener<TaskListEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskListEvent.GotoRead event) {
				TaskGroupScreenData.Read data = new TaskGroupScreenData.Read(
						(Integer) event.getData());
				projectView.gotoTaskList(data);
			}
		});

		this.register(new ApplicationEventListener<TaskListEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskListEvent.GotoEdit event) {
				TaskGroupScreenData.Edit data = new TaskGroupScreenData.Edit(
						(TaskList) event.getData());
				projectView.gotoTaskList(data);
			}
		});

		this.register(new ApplicationEventListener<TaskListEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskListEvent.GotoAdd event) {
				TaskList taskList = new TaskList();
				taskList.setProjectid(CurrentProjectVariables.getProjectId());
				taskList.setStatus(StatusI18nEnum.Open.name());
				TaskGroupScreenData.Add data = new TaskGroupScreenData.Add(
						taskList);
				projectView.gotoTaskList(data);
			}
		});

		this.register(new ApplicationEventListener<TaskListEvent.GotoTaskListScreen>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskListEvent.GotoTaskListScreen event) {
				projectView.gotoTaskList(null);
			}
		});

		this.register(new ApplicationEventListener<TaskListEvent.ReoderTaskList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskListEvent.ReoderTaskList event) {
				TaskGroupScreenData.ReorderTaskListRequest data = new TaskGroupScreenData.ReorderTaskListRequest();
				projectView.gotoTaskList(data);
			}
		});

		this.register(new ApplicationEventListener<TaskListEvent.GotoGanttChartView>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskListEvent.GotoGanttChartView event) {
				TaskGroupScreenData.GotoGanttChartView data = new TaskGroupScreenData.GotoGanttChartView();
				projectView.gotoTaskList(data);
			}
		});

	}

	private void bindTaskEvents() {
		this.register(new ApplicationEventListener<TaskEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskEvent.GotoRead event) {
				TaskScreenData.Read data = new TaskScreenData.Read(
						(Integer) event.getData());
				projectView.gotoTaskList(data);
			}
		});

		this.register(new ApplicationEventListener<TaskEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskEvent.GotoAdd event) {
				TaskScreenData.Add data = new TaskScreenData.Add(
						new SimpleTask());
				projectView.gotoTaskList(data);
			}
		});

		this.register(new ApplicationEventListener<TaskEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskEvent.GotoEdit event) {
				TaskScreenData.Edit data = new TaskScreenData.Edit(
						(SimpleTask) event.getData());
				projectView.gotoTaskList(data);
			}
		});

		this.register(new ApplicationEventListener<TaskEvent.Filter>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskEvent.Filter event) {
				TaskScreenData.Filter data = new TaskScreenData.Filter(
						(TaskFilterParameter) event.getData());
				projectView.gotoTaskList(data);
			}
		});

		this.register(new ApplicationEventListener<TaskEvent.Search>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(TaskEvent.Search event) {
				TaskScreenData.Search data = new TaskScreenData.Search(
						(TaskFilterParameter) event.getData());
				projectView.gotoTaskList(data);
			}
		});

	}

	private void bindRiskEvents() {
		this.register(new ApplicationEventListener<RiskEvent.GotoAdd>() {

			@Subscribe
			@Override
			public void handle(RiskEvent.GotoAdd event) {
				RiskScreenData.Add data = new RiskScreenData.Add(new Risk());
				projectView.gotoRiskView(data);
			}
		});

		this.register(new ApplicationEventListener<RiskEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(RiskEvent.GotoEdit event) {
				RiskScreenData.Edit data = new RiskScreenData.Edit((Risk) event
						.getData());
				projectView.gotoRiskView(data);
			}
		});

		this.register(new ApplicationEventListener<RiskEvent.GotoRead>() {
			@Subscribe
			@Override
			public void handle(RiskEvent.GotoRead event) {
				RiskScreenData.Read data = new RiskScreenData.Read(
						(Integer) event.getData());
				projectView.gotoRiskView(data);
			}
		});

		this.register(new ApplicationEventListener<RiskEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(RiskEvent.GotoList event) {
				RiskSearchCriteria criteria = new RiskSearchCriteria();

				criteria.setProjectId(new NumberSearchField(SearchField.AND,
						CurrentProjectVariables.getProjectId()));
				projectView.gotoRiskView(new RiskScreenData.Search(criteria));
			}
		});
	}

	private void bindProblemEvents() {
		this.register(new ApplicationEventListener<ProblemEvent.GotoAdd>() {
			@Subscribe
			@Override
			public void handle(ProblemEvent.GotoAdd event) {
				ProblemScreenData.Add data = new ProblemScreenData.Add(
						new Problem());
				IProblemPresenter presenter = PresenterResolver
						.getPresenter(IProblemPresenter.class);
				presenter.go(projectView, data);
			}
		});

		this.register(new ApplicationEventListener<ProblemEvent.GotoRead>() {
			@Subscribe
			@Override
			public void handle(ProblemEvent.GotoRead event) {
				ProblemScreenData.Read data = new ProblemScreenData.Read(
						(Integer) event.getData());
				IProblemPresenter presenter = PresenterResolver
						.getPresenter(IProblemPresenter.class);
				presenter.go(projectView, data);
			}
		});

		this.register(new ApplicationEventListener<ProblemEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(ProblemEvent.GotoList event) {

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

		this.register(new ApplicationEventListener<ProblemEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(ProblemEvent.GotoEdit event) {
				ProblemScreenData.Edit data = new ProblemScreenData.Edit(
						(Problem) event.getData());
				IProblemPresenter presenter = PresenterResolver
						.getPresenter(IProblemPresenter.class);
				presenter.go(projectView, data);
			}
		});
	}

	private void bindBugEvents() {
		this.register(new ApplicationEventListener<BugEvent.GotoDashboard>() {
			@Subscribe
			@Override
			public void handle(BugEvent.GotoDashboard event) {
				projectView.gotoBugView(null);
			}
		});

		this.register(new ApplicationEventListener<BugEvent.GotoAdd>() {
			@Subscribe
			@Override
			public void handle(BugEvent.GotoAdd event) {
				BugScreenData.Add data = new BugScreenData.Add(new SimpleBug());
				projectView.gotoBugView(data);
			}
		});

		this.register(new ApplicationEventListener<BugEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(BugEvent.GotoEdit event) {
				BugScreenData.Edit data = new BugScreenData.Edit(
						(SimpleBug) event.getData());
				projectView.gotoBugView(data);
			}
		});

		this.register(new ApplicationEventListener<BugEvent.GotoRead>() {
			@Subscribe
			@Override
			public void handle(BugEvent.GotoRead event) {
				BugScreenData.Read data = new BugScreenData.Read(
						(Integer) event.getData());
				projectView.gotoBugView(data);
			}
		});

		this.register(new ApplicationEventListener<BugEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(BugEvent.GotoList event) {
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

		this.register(new ApplicationEventListener<BugComponentEvent.GotoAdd>() {
			@Subscribe
			@Override
			public void handle(BugComponentEvent.GotoAdd event) {
				ComponentScreenData.Add data = new ComponentScreenData.Add(
						new Component());
				projectView.gotoBugView(data);
			}
		});

		this.register(new ApplicationEventListener<BugComponentEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(BugComponentEvent.GotoEdit event) {
				ComponentScreenData.Edit data = new ComponentScreenData.Edit(
						(Component) event.getData());
				projectView.gotoBugView(data);
			}
		});

		this.register(new ApplicationEventListener<BugComponentEvent.GotoRead>() {
			@Subscribe
			@Override
			public void handle(BugComponentEvent.GotoRead event) {
				ComponentScreenData.Read data = new ComponentScreenData.Read(
						(Integer) event.getData());
				projectView.gotoBugView(data);
			}
		});

		this.register(new ApplicationEventListener<BugComponentEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(BugComponentEvent.GotoList event) {
				ComponentSearchCriteria criteria = new ComponentSearchCriteria();
				criteria.setProjectid(new NumberSearchField(
						CurrentProjectVariables.getProjectId()));
				projectView
						.gotoBugView(new ComponentScreenData.Search(criteria));
			}
		});

		this.register(new ApplicationEventListener<BugVersionEvent.GotoAdd>() {
			@Subscribe
			@Override
			public void handle(BugVersionEvent.GotoAdd event) {
				VersionScreenData.Add data = new VersionScreenData.Add(
						new Version());
				projectView.gotoBugView(data);
			}
		});

		this.register(new ApplicationEventListener<BugVersionEvent.GotoEdit>() {
			@Subscribe
			@Override
			public void handle(BugVersionEvent.GotoEdit event) {
				VersionScreenData.Edit data = new VersionScreenData.Edit(
						(Version) event.getData());
				projectView.gotoBugView(data);
			}
		});

		this.register(new ApplicationEventListener<BugVersionEvent.GotoRead>() {
			@Subscribe
			@Override
			public void handle(BugVersionEvent.GotoRead event) {
				VersionScreenData.Read data = new VersionScreenData.Read(
						(Integer) event.getData());
				projectView.gotoBugView(data);
			}
		});

		this.register(new ApplicationEventListener<BugVersionEvent.GotoList>() {
			@Subscribe
			@Override
			public void handle(BugVersionEvent.GotoList event) {
				VersionSearchCriteria criteria = new VersionSearchCriteria();
				criteria.setProjectId(new NumberSearchField(
						CurrentProjectVariables.getProjectId()));
				projectView.gotoBugView(new VersionScreenData.Search(criteria));
			}
		});
	}

	private void bindMessageEvents() {
		this.register(new ApplicationEventListener<MessageEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(MessageEvent.GotoRead event) {
				MessageScreenData.Read data = new MessageScreenData.Read(
						(Integer) event.getData());
				MessagePresenter presenter = PresenterResolver
						.getPresenter(MessagePresenter.class);
				presenter.go(projectView, data);
			}
		});

		this.register(new ApplicationEventListener<MessageEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(MessageEvent.GotoList event) {
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
		this.register(new ApplicationEventListener<MilestoneEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(MilestoneEvent.GotoAdd event) {
				MilestoneScreenData.Add data = new MilestoneScreenData.Add(
						new Milestone());
				projectView.gotoMilestoneView(data);
			}
		});

		this.register(new ApplicationEventListener<MilestoneEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(MilestoneEvent.GotoRead event) {
				MilestoneScreenData.Read data = new MilestoneScreenData.Read(
						(Integer) event.getData());
				projectView.gotoMilestoneView(data);
			}
		});

		this.register(new ApplicationEventListener<MilestoneEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(MilestoneEvent.GotoList event) {
				MilestoneSearchCriteria criteria = new MilestoneSearchCriteria();

				criteria.setProjectId(new NumberSearchField(SearchField.AND,
						CurrentProjectVariables.getProjectId()));
				projectView.gotoMilestoneView(new MilestoneScreenData.Search(
						criteria));
			}
		});

		this.register(new ApplicationEventListener<MilestoneEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(MilestoneEvent.GotoEdit event) {
				MilestoneScreenData.Edit data = new MilestoneScreenData.Edit(
						(Milestone) event.getData());
				projectView.gotoMilestoneView(data);
			}
		});
	}

	private void bindStandupEvents() {
		this.register(new ApplicationEventListener<StandUpEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(StandUpEvent.GotoAdd event) {
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

		this.register(new ApplicationEventListener<StandUpEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(StandUpEvent.GotoList event) {
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

	private void bindUserGroupEvents() {

		this.register(new ApplicationEventListener<ProjectRoleEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectRoleEvent.GotoList event) {
				SimpleProject project = CurrentProjectVariables.getProject();
				ProjectRoleSearchCriteria criteria = new ProjectRoleSearchCriteria();
				criteria.setProjectId(new NumberSearchField(project.getId()));
				projectView.gotoUsersAndGroup(new ProjectRoleScreenData.Search(
						criteria));
			}
		});

		this.register(new ApplicationEventListener<ProjectRoleEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectRoleEvent.GotoAdd event) {
				ProjectRoleScreenData.Add data = new ProjectRoleScreenData.Add(
						new ProjectRole());
				projectView.gotoUsersAndGroup(data);
			}
		});

		this.register(new ApplicationEventListener<ProjectRoleEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectRoleEvent.GotoEdit event) {
				ProjectRoleScreenData.Add data = new ProjectRoleScreenData.Add(
						(ProjectRole) event.getData());
				projectView.gotoUsersAndGroup(data);
			}
		});

		this.register(new ApplicationEventListener<ProjectRoleEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectRoleEvent.GotoRead event) {
				ProjectRoleScreenData.Read data = new ProjectRoleScreenData.Read(
						(Integer) event.getData());
				projectView.gotoUsersAndGroup(data);
			}
		});

		this.register(new ApplicationEventListener<ProjectMemberEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectMemberEvent.GotoList event) {
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

		this.register(new ApplicationEventListener<ProjectMemberEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectMemberEvent.GotoRead event) {
				ProjectMemberScreenData.Read data = new ProjectMemberScreenData.Read(
						event.getData());
				projectView.gotoUsersAndGroup(data);
			}
		});

		this.register(new ApplicationEventListener<ProjectMemberEvent.GotoInviteMembers>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectMemberEvent.GotoInviteMembers event) {
				ProjectMemberScreenData.InviteProjectMembers data = new ProjectMemberScreenData.InviteProjectMembers();
				projectView.gotoUsersAndGroup(data);
			}
		});

		this.register(new ApplicationEventListener<ProjectMemberEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(ProjectMemberEvent.GotoEdit event) {
				ProjectMemberScreenData.Add data = new ProjectMemberScreenData.Add(
						(ProjectMember) event.getData());
				projectView.gotoUsersAndGroup(data);
			}
		});

		this.register(new ApplicationEventListener<CustomizeUIEvent.UpdateFeaturesList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(CustomizeUIEvent.UpdateFeaturesList event) {
				projectView.updateProjectFeatures();
			}
		});
	}

	private void bindFileEvents() {
		this.register(new ApplicationEventListener<ProjectContentEvent.GotoDashboard>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(GotoDashboard event) {
				IFilePresenter presenter = PresenterResolver
						.getPresenter(IFilePresenter.class);
				presenter.go(projectView, new FileScreenData.GotoDashboard());
			}

		});
	}

	private void bindPageEvents() {
		this.register(new ApplicationEventListener<PageEvent.GotoAdd>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(PageEvent.GotoAdd event) {
				String pagePath = (String) event.getData();
				if (pagePath == null || pagePath.equals("")) {
					pagePath = CurrentProjectVariables.getCurrentPagePath()
							+ "/" + StringUtils.generateSoftUniqueId();
				}

				Page page = new Page();
				page.setPath(pagePath);

				PageScreenData.Add data = new PageScreenData.Add(page);
				projectView.gotoPageView(data);
			}
		});

		this.register(new ApplicationEventListener<PageEvent.GotoEdit>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(PageEvent.GotoEdit event) {
				PageScreenData.Edit data = new PageScreenData.Edit((Page) event
						.getData());
				projectView.gotoPageView(data);
			}
		});

		this.register(new ApplicationEventListener<PageEvent.GotoRead>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(PageEvent.GotoRead event) {
				PageScreenData.Read data = new PageScreenData.Read((Page) event
						.getData());
				projectView.gotoPageView(data);
			}
		});

		this.register(new ApplicationEventListener<PageEvent.GotoList>() {
			private static final long serialVersionUID = 1L;

			@Subscribe
			@Override
			public void handle(PageEvent.GotoList event) {
				projectView.gotoPageView(new PageScreenData.Search(
						(String) event.getData()));
			}
		});
	}

}
