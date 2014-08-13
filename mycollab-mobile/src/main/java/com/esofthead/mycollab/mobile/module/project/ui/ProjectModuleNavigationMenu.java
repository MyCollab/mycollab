package com.esofthead.mycollab.mobile.module.project.ui;

import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.mobile.module.project.events.ProjectEvent;
import com.esofthead.mycollab.mobile.ui.AbstractNavigationMenu;
import com.esofthead.mycollab.module.project.i18n.ProjectCommonI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * @author MyCollab Ltd.
 * 
 * @since 4.4.0
 * 
 */
public class ProjectModuleNavigationMenu extends AbstractNavigationMenu {

	private static final long serialVersionUID = -4087918174690391176L;

	public ProjectModuleNavigationMenu() {
		super();

		final MenuButton prjListBtn = new MenuButton(
				AppContext
						.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_LIST),
				"&#xe614;");
		addMenu(prjListBtn);

		final MenuButton activityBtn = new MenuButton(
				AppContext
						.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_ACTIVITIES),
				"&#xe610;");
		addMenu(activityBtn);

		final MenuButton ticketBtn = new MenuButton(
				AppContext
						.getMessage(ProjectCommonI18nEnum.M_VIEW_PROJECT_FOLLOWING_TICKETS),
				"&#xe613;");
		addMenu(ticketBtn);
	}

	@Override
	protected ClickListener createDefaultButtonClickListener() {
		return new Button.ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(final ClickEvent event) {
				final String caption = ((MenuButton) event.getButton())
						.getBtnId();
				if (AppContext.getMessage(
						ProjectCommonI18nEnum.M_VIEW_PROJECT_LIST).equals(
						caption)) {
					EventBusFactory.getInstance().post(
							new ProjectEvent.GotoProjectList(this, null));
				}
			}
		};
	}

}
