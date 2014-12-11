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
package com.esofthead.mycollab.module.project.view.settings;

import org.vaadin.maddon.layouts.MHorizontalLayout;

import com.esofthead.mycollab.common.MyCollabSession;
import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.ProjectRoleSearchCriteria;
import com.esofthead.mycollab.module.project.events.ProjectRoleEvent;
import com.esofthead.mycollab.module.project.i18n.ProjectMemberI18nEnum;
import com.esofthead.mycollab.module.project.i18n.ProjectRoleI18nEnum;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
public class ProjectRoleSearchPanel extends
		GenericSearchPanel<ProjectRoleSearchCriteria> {
	private static final long serialVersionUID = 1L;
	private final SimpleProject project;
	protected ProjectRoleSearchCriteria searchCriteria;

	public ProjectRoleSearchPanel() {
		this.project = (SimpleProject) MyCollabSession.getVariable("project");
	}

	@Override
	public void attach() {
		super.attach();
		this.createBasicSearchLayout();
	}

	private void createBasicSearchLayout() {

		this.setCompositionRoot(new ProjectRoleBasicSearchLayout());
	}

	@SuppressWarnings("rawtypes")
	private class ProjectRoleBasicSearchLayout extends BasicSearchLayout {

		@SuppressWarnings("unchecked")
		public ProjectRoleBasicSearchLayout() {
			super(ProjectRoleSearchPanel.this);
		}

		private static final long serialVersionUID = 1L;
		private TextField nameField;
		private CheckBox myItemCheckbox;

		@Override
		public ComponentContainer constructBody() {
			final MHorizontalLayout basicSearchBody = new MHorizontalLayout()
					.withSpacing(true).withMargin(true);
			basicSearchBody.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);

			basicSearchBody.addComponent(new Label("Name"));
			this.nameField = new TextField();
			this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
			basicSearchBody.addComponent(this.nameField);
			this.myItemCheckbox = new CheckBox(
					AppContext
							.getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
			basicSearchBody.addComponent(this.myItemCheckbox);

			final Button searchBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final Button.ClickEvent event) {
							ProjectRoleBasicSearchLayout.this
									.callSearchAction();
						}
					});
			searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			searchBtn.setIcon(MyCollabResource
					.newResource(WebResourceIds._16_search));
			basicSearchBody.addComponent(searchBtn);

			final Button clearBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final Button.ClickEvent event) {
							ProjectRoleBasicSearchLayout.this.nameField
									.setValue("");
						}
					});
			clearBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
			basicSearchBody.addComponent(clearBtn);
			return basicSearchBody;
		}

		@Override
		protected SearchCriteria fillupSearchCriteria() {
			ProjectRoleSearchPanel.this.searchCriteria = new ProjectRoleSearchCriteria();
			ProjectRoleSearchPanel.this.searchCriteria
					.setProjectId(new NumberSearchField(SearchField.AND,
							ProjectRoleSearchPanel.this.project.getId()));
			return ProjectRoleSearchPanel.this.searchCriteria;
		}

		@Override
		public ComponentContainer constructHeader() {
			Image titleIcon = new Image(null,
					MyCollabResource.newResource("icons/24/project/user.png"));
			Label headerText = new Label(
					AppContext.getMessage(ProjectRoleI18nEnum.VIEW_LIST_TITLE));

			final Button createBtn = new Button(
					AppContext
							.getMessage(ProjectMemberI18nEnum.BUTTON_NEW_ROLE),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							EventBusFactory.getInstance().post(
									new ProjectRoleEvent.GotoAdd(this, null));
						}
					});
			createBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			createBtn.setIcon(MyCollabResource
					.newResource(WebResourceIds._16_addRecord));
			createBtn.setEnabled(CurrentProjectVariables
					.canWrite(ProjectRolePermissionCollections.ROLES));

			MHorizontalLayout header = new MHorizontalLayout()
					.withStyleName(UIConstants.HEADER_VIEW).withWidth("100%")
					.withSpacing(true)
					.withMargin(new MarginInfo(true, false, true, false))
					.with(titleIcon, headerText, createBtn)
					.withAlign(titleIcon, Alignment.MIDDLE_LEFT)
					.withAlign(headerText, Alignment.MIDDLE_LEFT)
					.withAlign(createBtn, Alignment.MIDDLE_RIGHT)
					.expand(headerText);

			return header;
		}
	}

}
