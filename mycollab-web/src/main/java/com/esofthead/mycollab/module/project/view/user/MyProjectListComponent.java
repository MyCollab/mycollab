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
package com.esofthead.mycollab.module.project.view.user;

import java.util.List;

import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.ProjectStatusConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.localization.ProjectCommonI18nEnum;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.ButtonLink;
import com.esofthead.mycollab.vaadin.ui.Depot;
import com.esofthead.mycollab.vaadin.ui.ProgressBarIndicator;
import com.esofthead.mycollab.web.MyCollabResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class MyProjectListComponent extends Depot {
	private static final long serialVersionUID = 1L;

	private ProjectPagedList projectList;

	public MyProjectListComponent() {
		super(LocalizationHelper
				.getMessage(ProjectCommonI18nEnum.MY_PROJECTS_TITLE),
				new VerticalLayout());

		this.projectList = new ProjectPagedList();
		this.addStyleName("activity-panel");
		this.addStyleName("myprojectlist");
		((VerticalLayout) this.bodyContent).setMargin(false);
	}

	public void showProjects(final List<Integer> prjKeys) {
		this.bodyContent.removeAllComponents();
		this.bodyContent.addComponent(this.projectList);
		final ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
		searchCriteria.setInvolvedMember(new StringSearchField(SearchField.AND,
				AppContext.getUsername()));
		searchCriteria.setProjectStatuses(new SetSearchField<String>(
				new String[] { ProjectStatusConstants.OPEN }));
		this.projectList.setSearchCriteria(searchCriteria);
	}

	static class ProjectPagedList extends
			BeanList<ProjectService, ProjectSearchCriteria, SimpleProject> {
		private static final long serialVersionUID = 1L;

		public ProjectPagedList() {
			super(null, ApplicationContextUtil
					.getSpringBean(ProjectService.class),
					ProjectRowDisplayHandler.class);
		}
	}

	public static class ProjectRowDisplayHandler implements
			BeanList.RowDisplayHandler<SimpleProject> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component generateRow(final SimpleProject project,
				final int rowIndex) {
			final CssLayout layout = new CssLayout();
			layout.setWidth("100%");
			layout.setStyleName("activity-stream");

			final CssLayout header = new CssLayout();
			header.setStyleName("stream-content");
			header.setWidth("100%");

			final HorizontalLayout projectLayout = new HorizontalLayout();
			projectLayout.setWidth("100%");
			projectLayout.addStyleName("project-status");

			final CssLayout linkWrapper = new CssLayout();
			linkWrapper.setWidth("145px");
			linkWrapper.setHeight("100%");
			linkWrapper.addStyleName("projectlink-wrapper");
			final HorizontalLayout linkIconFix = new HorizontalLayout();
			linkIconFix.setWidth("100%");
			final ButtonLink projectLink = new ButtonLink(project.getName(),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							EventBus.getInstance().fireEvent(
									new ProjectEvent.GotoMyProject(this,
											new PageActionChain(
													new ProjectScreenData.Goto(
															project.getId()))));
						}
					});
			final Image projectIcon = new Image(null,
					MyCollabResource
							.newResource("icons/16/project/project.png"));
			linkIconFix.addComponent(projectIcon);
			linkIconFix.addComponent(projectLink);
			linkIconFix.setExpandRatio(projectLink, 1.0f);
			projectLink.setWidth("100%");
			linkWrapper.addComponent(linkIconFix);
			projectLayout.addComponent(linkWrapper);

			final VerticalLayout projectStatusLayout = new VerticalLayout();
			projectStatusLayout.setSpacing(true);
			projectStatusLayout.setMargin(true);

			final HorizontalLayout taskStatus = new HorizontalLayout();
			taskStatus.setWidth("100%");
			taskStatus.setSpacing(true);
			final Label taskLbl = new Label("Tasks :");
			final Image taskIcon = new Image(null,
					MyCollabResource.newResource("icons/16/project/task.png"));
			taskStatus.addComponent(taskIcon);
			taskLbl.setWidth("45px");
			taskStatus.addComponent(taskLbl);
			final ProgressBarIndicator progressTask = new ProgressBarIndicator(
					project.getNumTasks(), project.getNumOpenTasks());
			progressTask.setWidth("100%");
			taskStatus.addComponent(progressTask);
			taskStatus.setExpandRatio(progressTask, 1.0f);
			projectStatusLayout.addComponent(taskStatus);

			final HorizontalLayout bugStatus = new HorizontalLayout();
			bugStatus.setWidth("100%");
			bugStatus.setSpacing(true);
			final Label bugLbl = new Label("Bugs :");
			final Image bugIcon = new Image(null,
					MyCollabResource.newResource("icons/16/project/bug.png"));
			bugStatus.addComponent(bugIcon);
			bugLbl.setWidth("45px");
			bugStatus.addComponent(bugLbl);
			final ProgressBarIndicator progressBug = new ProgressBarIndicator(
					project.getNumBugs(), project.getNumOpenBugs());
			progressBug.setWidth("100%");
			bugStatus.addComponent(progressBug);
			bugStatus.setExpandRatio(progressBug, 1.0f);
			projectStatusLayout.addComponent(bugStatus);

			projectLayout.addComponent(projectStatusLayout);
			projectStatusLayout.setWidth("100%");

			projectLayout.setExpandRatio(projectStatusLayout, 1.0f);

			header.addComponent(projectLayout);

			layout.addComponent(header);
			return layout;
		}
	}

}
