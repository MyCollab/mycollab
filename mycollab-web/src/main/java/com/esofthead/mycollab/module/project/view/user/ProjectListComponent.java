package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectStatusConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;


public class ProjectListComponent extends CssLayout {
	private static final long serialVersionUID = 6930971885172125913L;

	private HorizontalLayout headerLayout;
	private Label componentHeader;

	private CssLayout contentLayout;

	private ProjectPagedList projectList;

	private boolean isExpanded = false;

	public ProjectListComponent() {
		super();
		setWidth("100%");
		setStyleName("project-list-comp");

		headerLayout = new HorizontalLayout();
		headerLayout.setStyleName("project-list-comp-hdr");
		headerLayout.setWidth("100%");
		headerLayout.setMargin(true);
		headerLayout.setSpacing(true);
		headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
		headerLayout.addLayoutClickListener(new LayoutClickListener() {
			private static final long serialVersionUID = -1112701510070343608L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				isExpanded = !isExpanded;
				updateListState();
			}
		});

		componentHeader = new Label();
		componentHeader.setStyleName("h2");

		headerLayout.addComponent(new Image(null, MyCollabResource.newResource("icons/project_dropdown.png")));

		headerLayout.addComponent(componentHeader);
		headerLayout.setExpandRatio(componentHeader, 1.0f);
		addComponent(headerLayout);

		contentLayout = new CssLayout();
		contentLayout.setStyleName("project-list-comp-content");

		projectList = new ProjectPagedList();
		addComponent(contentLayout);

	}

	public void showProjects() {
		final ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
		searchCriteria.setInvolvedMember(new StringSearchField(SearchField.AND,
				AppContext.getUsername()));
		searchCriteria.setProjectStatuses(new SetSearchField<String>(
				new String[] { ProjectStatusConstants.OPEN }));
		this.projectList.setSearchCriteria(searchCriteria);
		this.componentHeader.setValue(CurrentProjectVariables.getProject().getName());
		isExpanded = false;
		updateListState();
	}

	protected void updateListState() {
		if (isExpanded) {
			this.addStyleName("isExpanded");
			contentLayout.addComponent(projectList);
		} else {
			this.removeStyleName("isExpanded");
			contentLayout.removeComponent(projectList);
		}
	}

	private class ProjectPagedList extends
	BeanList<ProjectService, ProjectSearchCriteria, SimpleProject> {
		private static final long serialVersionUID = 1L;

		public ProjectPagedList() {
			super(null, ApplicationContextUtil
					.getSpringBean(ProjectService.class),
					ProjectRowDisplayHandler.class);
		}

		@Override
		public int setSearchCriteria(ProjectSearchCriteria searchCriteria) {
			SearchRequest<ProjectSearchCriteria> searchRequest = new SearchRequest<ProjectSearchCriteria>(searchCriteria, 0, 3);
			return setSearchRequest(searchRequest);
		}
	}	

	public static class ProjectRowDisplayHandler implements
	BeanList.RowDisplayHandler<SimpleProject> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component generateRow(final SimpleProject obj, int rowIndex) {
			final VerticalLayout layout = new VerticalLayout();
			layout.setWidth("100%");
			layout.setStyleName("project-name");
			layout.setMargin(new MarginInfo(true, false, true, true));

			if (obj.getId() == CurrentProjectVariables.getProject().getId()) {
				layout.addStyleName("current-project");
			}

			Label prjName = new Label(obj.getName());
			layout.addComponent(prjName);

			layout.addLayoutClickListener(new LayoutClickListener() {				
				private static final long serialVersionUID = -329135249853828402L;

				@Override
				public void layoutClick(LayoutClickEvent event) {
					EventBus.getInstance().fireEvent(
							new ProjectEvent.GotoMyProject(this,
									new PageActionChain(
											new ProjectScreenData.Goto(
													obj.getId()))));
				}
			});

			return layout;
		}
	}
}
