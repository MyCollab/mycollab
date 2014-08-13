package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.mobile.module.project.ui.AbstractListViewComp;
import com.esofthead.mycollab.mobile.ui.AbstractPagedBeanList;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ViewComponent;
import com.vaadin.ui.Component;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */

@ViewComponent
public class ProjectListViewImpl extends
		AbstractListViewComp<ProjectSearchCriteria, SimpleProject> implements
		ProjectListView {

	private static final long serialVersionUID = 664947871255886622L;

	public ProjectListViewImpl() {
		this.setCaption(AppContext
				.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_LIST));
		this.setToggleButton(true);
	}

	@Override
	protected AbstractPagedBeanList<ProjectSearchCriteria, SimpleProject> createBeanTable() {
		ProjectListDisplay projectListDisplay = new ProjectListDisplay();
		return projectListDisplay;
	}

	@Override
	protected Component createRightComponent() {
		// TODO Auto-generated method stub
		return null;
	}

}
