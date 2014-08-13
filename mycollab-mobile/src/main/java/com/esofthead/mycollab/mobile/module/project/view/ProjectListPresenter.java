package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.mobile.module.project.ui.ProjectModuleNavigationMenu;
import com.esofthead.mycollab.mobile.ui.ListPresenter;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 *
 * @since 4.4.0
 *
 */
public class ProjectListPresenter extends
		ListPresenter<ProjectListView, ProjectSearchCriteria, SimpleProject> {

	private static final long serialVersionUID = 35574182873793474L;

	public ProjectListPresenter() {
		super(ProjectListView.class);
	}

	@Override
	protected void onGo(ComponentContainer container, ScreenData<?> data) {
		super.onGo(container, data);
		ProjectModuleNavigationMenu projectModuleMenu = new ProjectModuleNavigationMenu();
		projectModuleMenu.selectButton(AppContext
				.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_LIST));

		MobileNavigationManager currentNavigationManager = (MobileNavigationManager) UI
				.getCurrent().getContent();
		currentNavigationManager.setNavigationMenu(projectModuleMenu);
	}

}
