package com.esofthead.mycollab.module.project.view.user;

import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.ProjectStatusConstants;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.view.user.MyProjectListComponent.ProjectPagedList;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MyProjectListWindow extends Window {
	private static final long serialVersionUID = -3927621612074942453L;
	ProjectPagedList projectList;

	public MyProjectListWindow() {
		super("My Project");

		VerticalLayout layout = new VerticalLayout();
		layout.setStyleName("myprojectlist");
		layout.setMargin(true);

		projectList = new ProjectPagedList();
		layout.addComponent(projectList);
		this.setContent(layout);
		this.setModal(true);
		this.center();
	}

	@Override
	public void attach() {
		super.attach();

		final ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
		searchCriteria.setInvolvedMember(new StringSearchField(SearchField.AND,
				AppContext.getUsername()));
		searchCriteria.setProjectStatuses(new SetSearchField<String>(
				new String[] { ProjectStatusConstants.OPEN }));
		this.projectList.setSearchCriteria(searchCriteria);
	}



}
