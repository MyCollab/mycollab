package com.esofthead.mycollab.mobile.module.project.view;

import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.eventmanager.ApplicationEventListener;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.AbstractController;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.mvp.PresenterResolver;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.vaadin.mobilecomponent.MobileNavigationManager;
import com.google.common.eventbus.Subscribe;

/**
 * @author MyCollab Inc.
 * 
 * @since 4.3.1
 */
public class ProjectModuleController extends AbstractController {

	private static final long serialVersionUID = 8999456416358169209L;

	private final MobileNavigationManager navManager;

	public ProjectModuleController(MobileNavigationManager navigationManager) {
		this.navManager = navigationManager;

		bindProjectEvents();
	}

	private void bindProjectEvents() {
		this.register(new ApplicationEventListener<ProjectEvent.GotoProjectList>() {

			private static final long serialVersionUID = -9006615798118115613L;

			@Subscribe
			@Override
			public void handle(ProjectEvent.GotoProjectList event) {
				ProjectListPresenter presenter = PresenterResolver
						.getPresenter(ProjectListPresenter.class);
				ProjectSearchCriteria criteria = new ProjectSearchCriteria();
				criteria.setInvolvedMember(new StringSearchField(AppContext
						.getUsername()));
				criteria.setProjectStatuses(new SetSearchField<String>(
						new String[] { StatusI18nEnum.Open.name() }));
				presenter.go(navManager,
						new ScreenData.Search<ProjectSearchCriteria>(criteria));
			}
		});
		this.register(new ApplicationEventListener<ProjectEvent.GotoMyProject>() {
			private static final long serialVersionUID = 2554670937118159116L;

			@Subscribe
			@Override
			public void handle(ProjectEvent.GotoMyProject event) {
				ProjectViewPresenter presenter = PresenterResolver
						.getPresenter(ProjectViewPresenter.class);
				presenter.handleChain(navManager,
						(PageActionChain) event.getData());
			}
		});
	}

}
