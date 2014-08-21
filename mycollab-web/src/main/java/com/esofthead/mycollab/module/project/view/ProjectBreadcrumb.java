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

import java.util.Date;

import com.esofthead.mycollab.common.UrlEncodeDecoder;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectLinkGenerator;
import com.esofthead.mycollab.module.project.domain.Message;
import com.esofthead.mycollab.module.project.domain.Milestone;
import com.esofthead.mycollab.module.project.domain.Problem;
import com.esofthead.mycollab.module.project.domain.ProjectNotificationSetting;
import com.esofthead.mycollab.module.project.domain.Risk;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.SimpleProjectMember;
import com.esofthead.mycollab.module.project.domain.SimpleProjectRole;
import com.esofthead.mycollab.module.project.domain.Task;
import com.esofthead.mycollab.module.project.domain.TaskList;
import com.esofthead.mycollab.module.project.events.BugComponentEvent;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.events.BugVersionEvent;
import com.esofthead.mycollab.module.project.events.MessageEvent;
import com.esofthead.mycollab.module.project.events.MilestoneEvent;
import com.esofthead.mycollab.module.project.events.PageEvent;
import com.esofthead.mycollab.module.project.events.ProblemEvent;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.events.ProjectMemberEvent;
import com.esofthead.mycollab.module.project.events.ProjectNotificationEvent;
import com.esofthead.mycollab.module.project.events.ProjectRoleEvent;
import com.esofthead.mycollab.module.project.events.RiskEvent;
import com.esofthead.mycollab.module.project.events.StandUpEvent;
import com.esofthead.mycollab.module.project.events.TaskEvent;
import com.esofthead.mycollab.module.project.events.TaskListEvent;
import com.esofthead.mycollab.module.project.i18n.BreadcrumbI18nEnum;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ComponentI18nEnum;
import com.esofthead.mycollab.module.project.i18n.MessageI18nEnum;
import com.esofthead.mycollab.module.project.i18n.MilestoneI18nEnum;
import com.esofthead.mycollab.module.project.i18n.Page18InEnum;
import com.esofthead.mycollab.module.project.i18n.ProblemI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.i18n.RiskI18nEnum;
import com.esofthead.mycollab.module.project.i18n.VersionI18nEnum;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.module.tracker.domain.BugWithBLOBs;
import com.esofthead.mycollab.module.tracker.domain.Component;
import com.esofthead.mycollab.module.tracker.domain.Version;
import com.esofthead.mycollab.module.wiki.domain.Folder;
import com.esofthead.mycollab.module.wiki.domain.Page;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.module.wiki.service.WikiService;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.CacheableComponent;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.esofthead.mycollab.vaadin.ui.CommonUIFactory;
import com.esofthead.mycollab.vaadin.ui.utils.LabelStringGenerator;
import com.lexaden.breadcrumb.Breadcrumb;
import com.lexaden.breadcrumb.BreadcrumbLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewComponent
public class ProjectBreadcrumb extends Breadcrumb implements CacheableComponent {
	private static final long serialVersionUID = 1L;
	private static LabelStringGenerator menuLinkGenerator = new BreadcrumbLabelStringGenerator();

	private SimpleProject project;

	private Button homeBtn;

	public ProjectBreadcrumb() {
		this.setShowAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
		this.setHideAnimationSpeed(Breadcrumb.AnimSpeed.SLOW);
		this.setUseDefaultClickBehaviour(false);
		homeBtn = new Button(null, new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				EventBusFactory.getInstance().post(
						new ProjectEvent.GotoMyProject(this,
								new PageActionChain(new ProjectScreenData.Goto(
										project.getId()))));
			}
		});
		this.addLink(homeBtn);
	}

	@Override
	public void addLink(Button newBtn) {
		if (getComponentCount() > 0)
			homeBtn.setCaption(null);
		else
			homeBtn.setCaption(AppContext
					.getMessage(BreadcrumbI18nEnum.DASHBOARD));

		super.addLink(newBtn);
	}

	@Override
	public void select(int id) {
		if (id == 0) {
			homeBtn.setCaption(AppContext
					.getMessage(BreadcrumbI18nEnum.DASHBOARD));
		} else {
			homeBtn.setCaption(null);
		}
		super.select(id);
	}

	public void setProject(SimpleProject project) {
		this.project = project;
		this.select(0);
	}

	public void gotoMessageList() {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.MESSAGES)));
		AppContext.addFragment(
				ProjectLinkGenerator.generateMessagesLink(project.getId()),
				AppContext.getMessage(MessageI18nEnum.VIEW_LIST_TITLE));
	}

	public void gotoMessage(Message message) {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.MESSAGES),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						EventBusFactory.getInstance().post(
								new MessageEvent.GotoList(this, null));
					}
				}));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(message.getTitle()));
		AppContext.addFragment(ProjectLinkGenerator.generateMessagePreviewLink(
				project.getId(), message.getId()), AppContext.getMessage(
				BreadcrumbI18nEnum.FRA_MESSAGE_READ, message.getTitle()));
	}

	public void gotoRiskList() {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.RISKS)));
		AppContext.addFragment(
				ProjectLinkGenerator.generateRisksLink(project.getId()),
				AppContext.getMessage(RiskI18nEnum.VIEW_LIST_TITLE));
	}

	public void gotoRiskRead(Risk risk) {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.RISKS),
				new GotoRiskListListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(risk.getRiskname()));
		AppContext.addFragment(ProjectLinkGenerator.generateRiskPreviewLink(
				project.getId(), risk.getId()), AppContext.getMessage(
				BreadcrumbI18nEnum.FRA_RISK_READ, risk.getRiskname()));
	}

	public void gotoRiskEdit(final Risk risk) {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.RISKS),
				new GotoRiskListListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(risk.getRiskname(),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						EventBusFactory.getInstance().post(
								new RiskEvent.GotoRead(this, risk.getId()));
					}
				}));
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL)));
		AppContext.addFragment(
				ProjectLinkGenerator.generateRiskEditLink(project.getId(),
						risk.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_RISK_EDIT,
						risk.getRiskname()));
	}

	public void gotoRiskAdd() {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.RISKS),
				new GotoRiskListListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_ADD_LABEL)));
		AppContext.addFragment(
				ProjectLinkGenerator.generateRiskAddLink(project.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_RISK_NEW));
	}

	private static class GotoRiskListListener implements Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			EventBusFactory.getInstance().post(
					new RiskEvent.GotoList(this, null));
		}
	}

	public void gotoMilestoneList() {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.PHASES)));
		AppContext.addFragment(
				ProjectLinkGenerator.generateMilestonesLink(project.getId()),
				AppContext.getMessage(MilestoneI18nEnum.VIEW_LIST_TITLE));
	}

	public void gotoMilestoneRead(Milestone milestone) {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.PHASES),
				new GotoMilestoneListListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(milestone.getName()));
		AppContext.addFragment(ProjectLinkGenerator
				.generateMilestonePreviewLink(project.getId(),
						milestone.getId()), AppContext.getMessage(
				BreadcrumbI18nEnum.FRA_PHASE_READ, milestone.getName()));
	}

	public void gotoMilestoneEdit(final Milestone milestone) {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.PHASES),
				new GotoMilestoneListListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(milestone.getName(),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						EventBusFactory.getInstance().post(
								new MilestoneEvent.GotoRead(this, milestone
										.getId()));
					}
				}));
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL)));
		AppContext
				.addFragment(
						"project/milestone/edit/"
								+ UrlEncodeDecoder.encode(project.getId() + "/"
										+ milestone.getId()), AppContext
								.getMessage(BreadcrumbI18nEnum.FRA_PHASE_EDIT,
										milestone.getName()));
	}

	public void gotoMilestoneAdd() {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.PHASES),
				new GotoMilestoneListListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(MilestoneI18nEnum.VIEW_NEW_TITLE)));
		AppContext.addFragment(
				"project/milestone/add/"
						+ UrlEncodeDecoder.encode(project.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_PHASE_NEW));
	}

	private static class GotoMilestoneListListener implements
			Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			EventBusFactory.getInstance().post(
					new MilestoneEvent.GotoList(this, null));
		}
	}

	private void buildPageBreadcrumbChain() {
		String basePath = CurrentProjectVariables.getBasePagePath();
		String currentPath = CurrentProjectVariables.getCurrentPagePath();

		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.PAGES),
				new GotoPageListListener(basePath)));
		this.setLinkEnabled(true, 1);

		String extraPath = currentPath.substring(basePath.length());
		if (extraPath.startsWith("/")) {
			extraPath = extraPath.substring(1);
		}
		if (!extraPath.equals("")) {
			WikiService wikiService = ApplicationContextUtil
					.getSpringBean(WikiService.class);

			String[] subPath = extraPath.split("/");
			StringBuffer tempPath = new StringBuffer();
			for (String var : subPath) {
				tempPath.append("/").append(var);
				String folderPath = basePath + tempPath.toString();
				Folder folder = wikiService.getFolder(folderPath);
				if (folder != null) {
					this.addLink(new Button(folder.getName(),
							new GotoPageListListener(folderPath)));
				} else {
					return;
				}

			}
		}
	}

	public void gotoPageList() {
		this.select(0);
		buildPageBreadcrumbChain();
		AppContext.addFragment(ProjectLinkGenerator.generatePagesLink(
				project.getId(), CurrentProjectVariables.getCurrentPagePath()),
				AppContext.getMessage(Page18InEnum.VIEW_LIST_TITLE));
	}

	public void gotoPageAdd() {
		this.select(0);
		buildPageBreadcrumbChain();
		this.addLink(new Button(AppContext
				.getMessage(Page18InEnum.VIEW_NEW_TITLE)));
		AppContext.addFragment(ProjectLinkGenerator.generatePageAdd(
				project.getId(), CurrentProjectVariables.getCurrentPagePath()),
				AppContext.getMessage(Page18InEnum.VIEW_NEW_TITLE));
	}

	public void gotoPageRead(Page page) {
		this.select(0);
		buildPageBreadcrumbChain();
		this.addLink(new Button(StringUtils.trim(page.getSubject(), 50)));
		AppContext.addFragment(
				ProjectLinkGenerator.generatePageRead(project.getId(),
						page.getPath()),
				AppContext.getMessage(Page18InEnum.VIEW_READ_TITLE));
	}

	public void gotoPageEdit(Page page) {
		this.select(0);
		buildPageBreadcrumbChain();

		AppContext.addFragment(
				ProjectLinkGenerator.generatePageEdit(project.getId(),
						page.getPath()),
				AppContext.getMessage(Page18InEnum.VIEW_EDIT_TITLE));
	}

	private static class GotoPageListListener implements Button.ClickListener {
		private static final long serialVersionUID = 1L;

		private String path;

		public GotoPageListListener(String path) {
			this.path = path;
		}

		@Override
		public void buttonClick(ClickEvent event) {
			EventBusFactory.getInstance().post(
					new PageEvent.GotoList(this, path));
		}
	}

	public void gotoProblemList() {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.PROBLEMS)));
		AppContext.addFragment(
				ProjectLinkGenerator.generateProblemsLink(project.getId()),
				AppContext.getMessage(ProblemI18nEnum.VIEW_LIST_TITLE));
	}

	public void gotoProblemRead(Problem problem) {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.PROBLEMS),
				new GotoProblemListListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(problem.getIssuename()));
		AppContext.addFragment(ProjectLinkGenerator.generateProblemPreviewLink(
				project.getId(), problem.getId()), AppContext.getMessage(
				BreadcrumbI18nEnum.FRA_PROBLEM_READ, problem.getIssuename()));
	}

	public void gotoProblemEdit(final Problem problem) {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.PROBLEMS),
				new GotoProblemListListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(problem.getIssuename(),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						EventBusFactory.getInstance()
								.post(new ProblemEvent.GotoRead(this, problem
										.getId()));
					}
				}));
		this.setLinkEnabled(true, 2);
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL)));
		AppContext.addFragment(
				"project/problem/edit/"
						+ UrlEncodeDecoder.encode(project.getId() + "/"
								+ problem.getId()), AppContext.getMessage(
						BreadcrumbI18nEnum.FRA_PROBLEM_EDIT,
						problem.getIssuename()));
	}

	public void gotoProblemAdd() {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.PROBLEMS),
				new GotoProblemListListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_ADD_LABEL)));
		AppContext.addFragment(
				"project/problem/add/"
						+ UrlEncodeDecoder.encode(project.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_PROBLEM_NEW));
	}

	private static class GotoProblemListListener implements
			Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			EventBusFactory.getInstance().post(
					new ProblemEvent.GotoList(this, null));
		}
	}

	public void gotoTaskDashboard() {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.TASKS)));
		AppContext
				.addFragment(ProjectLinkGenerator
						.generateTaskDashboardLink(project.getId()), AppContext
						.getMessage(BreadcrumbI18nEnum.FRA_TASK_DASHBOARD));
	}

	public void gotoTaskListReorder() {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.TASKS),
				new GotoTaskAssignmentDashboard()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.FRA_TASKGROUP_REORDER)));
		AppContext
				.addFragment("project/task/dashboard/reorder/"
						+ UrlEncodeDecoder.encode(project.getId()), AppContext
						.getMessage(BreadcrumbI18nEnum.FRA_TASKGROUP_REORDER));
	}

	public void gotoTaskGroupAdd() {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.TASKS),
				new GotoTaskAssignmentDashboard()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.FRA_TASKGROUP_NEW)));
		AppContext.addFragment(
				"project/task/taskgroup/add/"
						+ UrlEncodeDecoder.encode(project.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_TASKGROUP_NEW));
	}

	public void gotoTaskGroupRead(TaskList taskList) {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.TASKS),
				new GotoTaskAssignmentDashboard()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(AppContext.getMessage(
				BreadcrumbI18nEnum.FRA_TASKGROUP_READ, taskList.getName())));
		AppContext.addFragment(
				ProjectLinkGenerator.generateTaskGroupPreviewLink(
						project.getId(), taskList.getId()), AppContext
						.getMessage(BreadcrumbI18nEnum.FRA_TASKGROUP_READ,
								taskList.getName()));
	}

	public void gotoTaskGroupEdit(final TaskList taskList) {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.TASKS),
				new GotoTaskAssignmentDashboard()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(AppContext.getMessage(
				BreadcrumbI18nEnum.FRA_TASKGROUP_READ, taskList.getName()),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						EventBusFactory.getInstance().post(
								new TaskListEvent.GotoRead(this, taskList
										.getId()));
					}
				}));
		this.setLinkEnabled(true, 2);
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL)));
		AppContext.addFragment(
				"project/task/taskgroup/edit/"
						+ UrlEncodeDecoder.encode(project.getId() + "/"
								+ taskList.getId()), AppContext.getMessage(
						BreadcrumbI18nEnum.FRA_TASKGROUP_EDIT,
						taskList.getName()));
	}

	public void gotoTaskAdd() {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.TASKS),
				new GotoTaskAssignmentDashboard()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.FRA_TASK_NEW)));
		AppContext.addFragment(
				"project/task/task/add/"
						+ UrlEncodeDecoder.encode(project.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_TASK_NEW));
	}

	public void gotoTaskFilter() {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.TASKS),
				new GotoTaskAssignmentDashboard()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button("Task: Filter"));
	}

	public void gotoTaskRead(Task task) {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.TASKS),
				new GotoTaskAssignmentDashboard()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(AppContext.getMessage(
				BreadcrumbI18nEnum.FRA_TASK_READ, task.getTaskname())));
		AppContext.addFragment(ProjectLinkGenerator.generateTaskPreviewLink(
				project.getId(), task.getId()), AppContext.getMessage(
				BreadcrumbI18nEnum.FRA_TASK_READ, task.getTaskname()));
	}

	public void gotoTaskEdit(final Task task) {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.TASKS),
				new GotoTaskAssignmentDashboard()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_TASK_READ,
						task.getTaskname()), new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						EventBusFactory.getInstance().post(
								new TaskEvent.GotoRead(this, task.getId()));
					}
				}));
		this.setLinkEnabled(true, 2);
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL)));
		AppContext.addFragment(
				"project/task/task/edit/"
						+ UrlEncodeDecoder.encode(project.getId() + "/"
								+ task.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_TASK_EDIT,
						task.getTaskname()));
	}

	public class GotoTaskAssignmentDashboard implements Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			EventBusFactory.getInstance().post(
					new TaskListEvent.GotoTaskListScreen(this, null));
		}
	}

	public void gotoBugDashboard() {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.BUGS)));
		AppContext.addFragment(
				ProjectLinkGenerator.generateBugDashboardLink(project.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_BUG_DASHBOARD));
	}

	public void gotoBugList() {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.BUGS),
				new GotoBugDashboardListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.FRA_BUG_LIST)));
		AppContext.addFragment(
				"project/bug/list/" + UrlEncodeDecoder.encode(project.getId()),
				AppContext.getMessage(BugI18nEnum.VIEW_LIST_TITLE));
	}

	public void gotoBugAdd() {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.BUGS),
				new GotoBugDashboardListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.FRA_BUG_NEW)));
		AppContext.addFragment(
				"project/bug/add/" + UrlEncodeDecoder.encode(project.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_BUG_NEW));
	}

	public void gotoBugEdit(final BugWithBLOBs bug) {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.BUGS),
				new GotoBugDashboardListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(bug.getSummary(),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						EventBusFactory.getInstance().post(
								new BugEvent.GotoRead(this, bug.getId()));
					}
				}));
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL)));
		AppContext.addFragment(
				"project/bug/edit/"
						+ UrlEncodeDecoder.encode(project.getId() + "/"
								+ bug.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_BUG_EDIT,
						bug.getSummary()));
	}

	public void gotoBugRead(BugWithBLOBs bug) {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.BUGS),
				new GotoBugDashboardListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(bug.getSummary()));
		AppContext.addFragment(ProjectLinkGenerator.generateBugPreviewLink(
				bug.getProjectid(), bug.getId()), AppContext.getMessage(
				BreadcrumbI18nEnum.FRA_BUG_READ, bug.getSummary()));
	}

	public void gotoVersionList() {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.BUGS),
				new GotoBugDashboardListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.VERSIONS)));
		AppContext.addFragment(
				"project/bug/version/list/"
						+ UrlEncodeDecoder.encode(project.getId()),
				AppContext.getMessage(VersionI18nEnum.VIEW_LIST_TITLE));
	}

	public void gotoVersionAdd() {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.BUGS),
				new GotoBugDashboardListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.VERSIONS),
				new GotoVersionListener()));
		this.setLinkEnabled(true, 2);
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_ADD_LABEL)));
		AppContext.addFragment(
				"project/bug/version/add/"
						+ UrlEncodeDecoder.encode(project.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_VERSION_NEW));
	}

	public void gotoVersionEdit(final Version version) {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.BUGS),
				new GotoBugDashboardListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.VERSIONS),
				new GotoVersionListener()));
		this.setLinkEnabled(true, 2);
		this.addLink(generateBreadcrumbLink(version.getVersionname(),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						EventBusFactory.getInstance().post(
								new BugVersionEvent.GotoRead(this, version
										.getId()));
					}
				}));
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL)));
		AppContext.addFragment(
				"project/bug/version/edit/"
						+ UrlEncodeDecoder.encode(project.getId() + "/"
								+ version.getId()), AppContext.getMessage(
						BreadcrumbI18nEnum.FRA_VERSION_EDIT,
						version.getVersionname()));
	}

	public void gotoVersionRead(Version version) {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.BUGS),
				new GotoBugDashboardListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.VERSIONS),
				new GotoVersionListener()));
		this.setLinkEnabled(true, 2);
		this.addLink(generateBreadcrumbLink(version.getVersionname()));
		AppContext.addFragment(
				ProjectLinkGenerator.generateBugVersionPreviewLink(
						project.getId(), version.getId()), AppContext
						.getMessage(BreadcrumbI18nEnum.FRA_VERSION_READ,
								version.getVersionname()));
	}

	private class GotoVersionListener implements Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			EventBusFactory.getInstance().post(
					new BugVersionEvent.GotoList(this, null));
		}
	}

	public void gotoComponentList() {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.BUGS),
				new GotoBugDashboardListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.COMPONENTS)));
		AppContext.addFragment(
				"project/bug/component/list/"
						+ UrlEncodeDecoder.encode(project.getId()),
				AppContext.getMessage(ComponentI18nEnum.VIEW_LIST_TITLE));
	}

	public void gotoComponentAdd() {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.BUGS),
				new GotoBugDashboardListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.COMPONENTS),
				new GotoComponentListener()));
		this.setLinkEnabled(true, 2);
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_ADD_LABEL)));
		AppContext.addFragment(
				"project/bug/component/add/"
						+ UrlEncodeDecoder.encode(project.getId()),
				AppContext.getMessage(BugI18nEnum.BUTTON_NEW_COMPONENT));
	}

	public void gotoComponentEdit(final Component component) {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.BUGS),
				new GotoBugDashboardListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.COMPONENTS),
				new GotoComponentListener()));
		this.setLinkEnabled(true, 2);
		this.addLink(generateBreadcrumbLink(component.getComponentname(),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(ClickEvent event) {
						EventBusFactory.getInstance().post(
								new BugComponentEvent.GotoRead(this, component
										.getId()));
					}
				}));
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL)));
		AppContext.addFragment(
				"project/bug/component/edit/"
						+ UrlEncodeDecoder.encode(project.getId() + "/"
								+ component.getId()), AppContext.getMessage(
						BreadcrumbI18nEnum.FRA_COMPONENT_EDIT,
						component.getComponentname()));
	}

	public void gotoComponentRead(Component component) {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.BUGS),
				new GotoBugDashboardListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.COMPONENTS),
				new GotoComponentListener()));
		this.addLink(generateBreadcrumbLink(component.getComponentname()));
		AppContext.addFragment(ProjectLinkGenerator
				.generateBugComponentPreviewLink(project.getId(),
						component.getId()), AppContext.getMessage(
				BreadcrumbI18nEnum.FRA_COMPONENT_READ,
				component.getComponentname()));
	}

	public void gotoTimeTrackingList() {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.TIME_TRACKING)));
		AppContext.addFragment(
				ProjectLinkGenerator.generateTimeReportLink(project.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_TIME_TRACKING));
	}

	public void gotoFileList() {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.FILES)));
		AppContext
				.addFragment(ProjectLinkGenerator
						.generateFileDashboardLink(project.getId()), AppContext
						.getMessage(BreadcrumbI18nEnum.FRA_FILES));
	}

	public void gotoStandupList(Date onDate) {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.STANDUP)));
		if (onDate == null) {
			AppContext.addFragment(
					"project/standup/list/"
							+ UrlEncodeDecoder.encode(project.getId()),
					AppContext.getMessage(BreadcrumbI18nEnum.FRA_STANDUP));
		} else {
			AppContext.addFragment(
					"project/standup/list/"
							+ UrlEncodeDecoder.encode(project.getId() + "/"
									+ AppContext.formatDate(onDate)),
					AppContext.getMessage(BreadcrumbI18nEnum.FRA_STANDUP));
		}

	}

	public void gotoStandupAdd(Date date) {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.STANDUP),
				new GotoStandupListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_ADD_LABEL)));

		AppContext.addFragment(
				"project/standup/add/"
						+ UrlEncodeDecoder.encode(CurrentProjectVariables
								.getProjectId()
								+ "/"
								+ AppContext.formatDate(date)), AppContext
						.getMessage(BreadcrumbI18nEnum.FRA_STANDUP_FOR_DAY,
								AppContext.formatDate(date)));
	}

	public void gotoUserList() {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.USERS)));
		AppContext.addFragment(
				ProjectLinkGenerator.generateUsersLink(project.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_MEMBERS));
	}

	public void gotoUserAdd() {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.USERS),
				new GotoUserListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_INVITEES)));
		AppContext.addFragment(
				"project/user/add/" + UrlEncodeDecoder.encode(project.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_INVITE_MEMBERS));
	}

	public void gotoUserRead(SimpleProjectMember member) {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.USERS),
				new GotoUserListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(member.getMemberFullName()));
		AppContext.addFragment(
				"project/user/preview/"
						+ UrlEncodeDecoder.encode(project.getId() + "/"
								+ member.getUsername()), AppContext.getMessage(
						BreadcrumbI18nEnum.FRA_MEMBER_READ,
						member.getMemberFullName()));
	}

	public void gotoUserEdit(SimpleProjectMember member) {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.USERS),
				new GotoUserListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(member.getMemberFullName()));
		AppContext.addFragment(
				"project/user/edit/"
						+ UrlEncodeDecoder.encode(project.getId() + "/"
								+ member.getId()), AppContext.getMessage(
						BreadcrumbI18nEnum.FRA_MEMBER_EDIT,
						member.getMemberFullName()));
	}

	public void gotoRoleList() {
		this.select(0);
		this.addLink(new Button(AppContext.getMessage(BreadcrumbI18nEnum.ROLES)));
		AppContext
				.addFragment(
						"project/role/list/"
								+ UrlEncodeDecoder.encode(project.getId()),
						AppContext.getMessage(BreadcrumbI18nEnum.FRA_ROLES));
	}

	public void gotoRoleRead(SimpleProjectRole role) {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.ROLES),
				new GotoRoleListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(role.getRolename()));
		AppContext.addFragment(
				"project/role/preview/"
						+ UrlEncodeDecoder.encode(project.getId() + "/"
								+ role.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_ROLE_READ,
						role.getRolename()));
	}

	public void gotoProjectSetting() {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(BreadcrumbI18nEnum.FRA_SETTING),
				new GotoNotificationSetttingListener()));
		AppContext.addFragment("project/setting/"
				+ UrlEncodeDecoder.encode(project.getId()), AppContext
				.getMessage(BreadcrumbI18nEnum.FRA_SETTING));
	}

	public void gotoRoleAdd() {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.ROLES),
				new GotoRoleListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_ADD_LABEL)));
		AppContext.addFragment(
				"project/role/add/" + UrlEncodeDecoder.encode(project.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_ROLE_NEW));
	}

	public void gotoRoleEdit(SimpleProjectRole role) {
		this.select(0);
		this.addLink(new Button(
				AppContext.getMessage(BreadcrumbI18nEnum.ROLES),
				new GotoUserListener()));
		this.setLinkEnabled(true, 1);
		this.addLink(generateBreadcrumbLink(role.getRolename()));
		AppContext.addFragment(
				"project/role/edit/"
						+ UrlEncodeDecoder.encode(project.getId() + "/"
								+ role.getId()),
				AppContext.getMessage(BreadcrumbI18nEnum.FRA_ROLE_EDIT,
						role.getRolename()));
	}

	private static class GotoNotificationSetttingListener implements
			Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			EventBusFactory.getInstance().post(
					new ProjectNotificationEvent.GotoList(this, null));
		}
	}

	private static class GotoRoleListener implements Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			EventBusFactory.getInstance().post(
					new ProjectRoleEvent.GotoList(this, null));
		}
	}

	private static class GotoStandupListener implements Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			EventBusFactory.getInstance().post(
					new StandUpEvent.GotoList(this, null));
		}
	}

	private static class GotoUserListener implements Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			EventBusFactory.getInstance().post(
					new ProjectMemberEvent.GotoList(this, null));
		}
	}

	private static class GotoComponentListener implements Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			EventBusFactory.getInstance().post(
					new BugComponentEvent.GotoList(this, null));
		}
	}

	private static class GotoBugDashboardListener implements
			Button.ClickListener {
		private static final long serialVersionUID = 1L;

		@Override
		public void buttonClick(ClickEvent event) {
			EventBusFactory.getInstance().post(
					new BugEvent.GotoDashboard(this, null));
		}
	}

	public void gotoProjectDashboard() {
		this.select(0);
		AppContext.addFragment(
				ProjectLinkGenerator.generateProjectLink(project.getId()),
				"Dashboard");
	}

	public void gotoProjectEdit() {
		this.select(0);
		this.addLink(new Button(AppContext
				.getMessage(GenericI18Enum.BUTTON_EDIT_LABEL)));
		AppContext.addFragment(
				"project/edit/" + UrlEncodeDecoder.encode(project.getId()),
				"Edit Project: " + project.getName());
	}

	@Override
	public int getComponentCount() {
		if (getCompositionRoot() != null) {
			final BreadcrumbLayout compositionRoot = (BreadcrumbLayout) getCompositionRoot();
			return compositionRoot.getComponentCount();
		}
		return super.getComponentCount();
	}

	private static Button generateBreadcrumbLink(String linkname) {
		return CommonUIFactory.createButtonTooltip(
				menuLinkGenerator.handleText(linkname), linkname);
	}

	private static Button generateBreadcrumbLink(String linkname,
			Button.ClickListener listener) {
		return CommonUIFactory.createButtonTooltip(
				menuLinkGenerator.handleText(linkname), linkname, listener);
	}

	private static class BreadcrumbLabelStringGenerator implements
			LabelStringGenerator {

		@Override
		public String handleText(String value) {
			if (value.length() > 35) {
				return value.substring(0, 35) + "...";
			}
			return value;
		}

	}
}
