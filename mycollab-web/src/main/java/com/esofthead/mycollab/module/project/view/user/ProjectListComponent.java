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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.common.i18n.OptionI18nEnum.StatusI18nEnum;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SearchRequest;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectEvent;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.ui.AssetsManager;
import com.esofthead.mycollab.module.project.view.ProjectAddWindow;
import com.esofthead.mycollab.module.project.view.parameters.ProjectScreenData;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.mvp.PageActionChain;
import com.esofthead.mycollab.vaadin.ui.BeanList;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;

import java.util.List;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class ProjectListComponent extends MVerticalLayout {
	private static final long serialVersionUID = 6930971885172125913L;

	final private PopupButton headerPopupButton;

	private MVerticalLayout contentLayout;

	private ProjectPagedList projectList;

	private Label projectDesc;

	public ProjectListComponent() {
		super();
		withSpacing(true).withMargin(false).withWidth("100%").withStyleName("project-list-comp");

		MHorizontalLayout headerBar = new MHorizontalLayout();

		headerPopupButton = new PopupButton();
		headerPopupButton.setStyleName("project-list-comp-hdr");
		headerPopupButton.setWidth("100%");

		Label componentHeader = new Label();
		componentHeader.setStyleName("h2");

		headerPopupButton.setIcon(AssetsManager.getAsset(ProjectTypeConstants.PROJECT));
		headerBar.with(headerPopupButton);

		if (AppContext.canBeYes(RolePermissionCollections.CREATE_NEW_PROJECT)) {
			final Button createProjectBtn = new Button("+",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final Button.ClickEvent event) {
							final ProjectAddWindow projectNewWindow = new ProjectAddWindow();
							UI.getCurrent().addWindow(projectNewWindow);
						}
					});
			createProjectBtn.setStyleName("add-project-btn");
			createProjectBtn.setDescription("New Project");
			createProjectBtn.setWidth("20px");
			createProjectBtn.setHeight("20px");

			headerBar.with(createProjectBtn).withAlign(createProjectBtn,
					Alignment.MIDDLE_RIGHT);
		}

		headerBar.withWidth("100%").withSpacing(true).expand(headerPopupButton);

		this.addComponent(headerBar);

		contentLayout = new MVerticalLayout().withStyleName(
				"project-list-comp-content").withWidth("205px");

		projectList = new ProjectPagedList();
		headerPopupButton.setContent(projectList);

		projectDesc = new Label("", ContentMode.HTML);
		projectDesc.setStyleName("project-description");
		addComponent(projectDesc);
	}

	public void showProjects() {
		if (headerPopupButton.isPopupVisible()) {
			headerPopupButton.setPopupVisible(false);
		}
		final ProjectSearchCriteria searchCriteria = new ProjectSearchCriteria();
		searchCriteria.setInvolvedMember(new StringSearchField(SearchField.AND,
				AppContext.getUsername()));
		searchCriteria.setProjectStatuses(new SetSearchField<>(
				new String[] { StatusI18nEnum.Open.name() }));
		this.projectList.setSearchCriteria(searchCriteria);
		this.headerPopupButton.setCaption(CurrentProjectVariables.getProject()
				.getName());

		String desc = CurrentProjectVariables.getProject().getDescription();
		desc = StringUtils.trim(desc, 150, true);
		this.projectDesc.setValue(desc);
	}

	private class ProjectPagedList extends
			BeanList<ProjectService, ProjectSearchCriteria, SimpleProject> {
		private static final long serialVersionUID = 1L;
		protected ProjectSearchCriteria currentCriteria;

		public ProjectPagedList() {
			super(null, ApplicationContextUtil
					.getSpringBean(ProjectService.class),
					ProjectRowDisplayHandler.class, contentLayout);
		}

		@Override
		public int setSearchCriteria(ProjectSearchCriteria searchCriteria) {
			currentCriteria = searchCriteria;
			SearchRequest<ProjectSearchCriteria> searchRequest = new SearchRequest<>(
					searchCriteria, 0, 3);
			return setSearchRequest(searchRequest);
		}

		@Override
		public void loadItems(List<SimpleProject> currentListData) {
			super.loadItems(currentListData);

			if (searchService.getTotalCount(currentCriteria) > 3) {
				MVerticalLayout btnWrap = new MVerticalLayout().withWidth(
						"100%").withMargin(true);

				final MyProjectListWindow projectListWindow = new MyProjectListWindow();

				Button showMoreBtn = new Button(
						AppContext.getMessage(GenericI18Enum.BUTTON_MORE),
						new Button.ClickListener() {
							private static final long serialVersionUID = -2178412846807704534L;

							@Override
							public void buttonClick(ClickEvent event) {
								headerPopupButton.setPopupVisible(false);
								UI.getCurrent().addWindow(projectListWindow);
							}
						});
				showMoreBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
				showMoreBtn.setWidth("100%");
				btnWrap.addComponent(showMoreBtn);
				getContentLayout().addComponent(btnWrap);
			}
		}
	}

	public static class ProjectRowDisplayHandler extends
			BeanList.RowDisplayHandler<SimpleProject> {
		private static final long serialVersionUID = 1L;

		@Override
		public Component generateRow(final SimpleProject obj, int rowIndex) {
			final MVerticalLayout layout = new MVerticalLayout()
					.withWidth("100%").withStyleName("project-name")
					.withMargin(new MarginInfo(true, false, true, true));

			if (obj.getId() == CurrentProjectVariables.getProject().getId()) {
				layout.addStyleName("current-project");
			}

			Label prjName = new Label(obj.getName());
			layout.addComponent(prjName);

			layout.addLayoutClickListener(new LayoutClickListener() {
				private static final long serialVersionUID = -329135249853828402L;

				@Override
				public void layoutClick(LayoutClickEvent event) {
					EventBusFactory.getInstance().post(
							new ProjectEvent.GotoMyProject(this,
									new PageActionChain(
											new ProjectScreenData.Goto(obj
													.getId()))));
				}
			});

			return layout;
		}
	}
}
