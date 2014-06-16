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
package com.esofthead.mycollab.module.project.view.bug;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectRolePermissionCollections;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.events.BugEvent;
import com.esofthead.mycollab.module.project.i18n.BugI18nEnum;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberListSelect;
import com.esofthead.mycollab.module.tracker.domain.criteria.BugSearchCriteria;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.DynamicQueryParamLayout;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
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
public class BugSearchPanel extends
		DefaultGenericSearchPanel<BugSearchCriteria> {

	private static final long serialVersionUID = 1L;

	private final SimpleProject project;
	protected BugSearchCriteria searchCriteria;
	protected Label bugtitle;
	private ComponentContainer rightComponent;

	private static Param[] paramFields = new Param[] {
			BugSearchCriteria.p_textDesc, BugSearchCriteria.p_priority,
			BugSearchCriteria.p_severity, BugSearchCriteria.p_status,
			BugSearchCriteria.p_assignee, BugSearchCriteria.p_resolveddate,
			BugSearchCriteria.p_duedate, BugSearchCriteria.p_createdtime,
			BugSearchCriteria.p_lastupdatedtime };

	public BugSearchPanel() {
		this("Bugs");
	}

	public BugSearchPanel(final String title) {
		this.project = CurrentProjectVariables.getProject();
		this.bugtitle = new Label(title);
	}

	public void setBugTitle(final String title) {
		this.bugtitle.setValue(title);
	}

	public void addRightComponent(ComponentContainer c) {
		rightComponent.addComponent(c);
	}

	private ComponentContainer constructHeader() {
		Image titleIcon = new Image(null,
				MyCollabResource.newResource("icons/24/project/bug.png"));
		Label headerText = new Label(
				AppContext.getMessage(BugI18nEnum.LIST_VIEW_TITLE));

		final Button createBtn = new Button(
				AppContext.getMessage(BugI18nEnum.NEW_BUG_ACTION),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new BugEvent.GotoAdd(this, null));
					}
				});
		createBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		createBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		createBtn.setEnabled(CurrentProjectVariables
				.canWrite(ProjectRolePermissionCollections.BUGS));

		HorizontalLayout header = new HorizontalLayout();
		headerText.setStyleName(UIConstants.HEADER_TEXT);

		rightComponent = new HorizontalLayout();

		UiUtils.addComponent(header, titleIcon, Alignment.MIDDLE_LEFT);
		UiUtils.addComponent(header, headerText, Alignment.MIDDLE_LEFT);
		UiUtils.addComponent(header, createBtn, Alignment.MIDDLE_RIGHT);
		UiUtils.addComponent(header, rightComponent, Alignment.MIDDLE_RIGHT);
		header.setExpandRatio(headerText, 1.0f);

		header.setStyleName(UIConstants.HEADER_VIEW);
		header.setWidth("100%");
		header.setSpacing(true);
		header.setMargin(new MarginInfo(true, false, true, false));
		return header;
	}

	@Override
	protected SearchLayout<BugSearchCriteria> createBasicSearchLayout() {
		return new BugBasicSearchLayout();
	}

	@Override
	protected SearchLayout<BugSearchCriteria> createAdvancedSearchLayout() {
		return new BugAdvancedSearchLayout();
	}

	private class BugBasicSearchLayout extends
			BasicSearchLayout<BugSearchCriteria> {

		public BugBasicSearchLayout() {
			super(BugSearchPanel.this);
		}

		private static final long serialVersionUID = 1L;
		private TextField nameField;
		private CheckBox myItemCheckbox;

		@SuppressWarnings("serial")
		@Override
		public ComponentContainer constructBody() {
			final HorizontalLayout basicSearchBody = new HorizontalLayout();
			basicSearchBody.setSpacing(true);
			basicSearchBody.setMargin(true);
			UiUtils.addComponent(basicSearchBody, new Label("Name:"),
					Alignment.MIDDLE_LEFT);

			this.nameField = new TextField();
			this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
			UiUtils.addComponent(basicSearchBody, this.nameField,
					Alignment.MIDDLE_CENTER);

			this.myItemCheckbox = new CheckBox(
					AppContext
							.getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
			UiUtils.addComponent(basicSearchBody, this.myItemCheckbox,
					Alignment.MIDDLE_CENTER);

			final Button searchBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH_LABEL));
			searchBtn.setIcon(MyCollabResource
					.newResource("icons/16/search.png"));
			searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

			searchBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {

					BugBasicSearchLayout.this.callSearchAction();
				}
			});
			searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			UiUtils.addComponent(basicSearchBody, searchBtn,
					Alignment.MIDDLE_LEFT);

			final Button cancelBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR_LABEL));
			cancelBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					BugBasicSearchLayout.this.nameField.setValue("");
				}
			});
			cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
			basicSearchBody.addComponent(cancelBtn);

			final Button advancedSearchBtn = new Button(
					AppContext
							.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
					new Button.ClickListener() {
						@Override
						public void buttonClick(final ClickEvent event) {
							BugSearchPanel.this.moveToAdvancedSearchLayout();
						}
					});
			advancedSearchBtn.setStyleName("link");
			UiUtils.addComponent(basicSearchBody, advancedSearchBtn,
					Alignment.MIDDLE_CENTER);

			return basicSearchBody;
		}

		@Override
		protected BugSearchCriteria fillupSearchCriteria() {
			BugSearchPanel.this.searchCriteria = new BugSearchCriteria();
			BugSearchPanel.this.searchCriteria
					.setProjectId(new NumberSearchField(SearchField.AND,
							BugSearchPanel.this.project.getId()));
			BugSearchPanel.this.searchCriteria
					.setSummary(new StringSearchField(this.nameField.getValue()
							.toString().trim()));
			if (this.myItemCheckbox.getValue()) {
				BugSearchPanel.this.searchCriteria
						.setAssignuser(new StringSearchField(SearchField.AND,
								AppContext.getUsername()));
			} else {
				BugSearchPanel.this.searchCriteria.setAssignuser(null);
			}
			return BugSearchPanel.this.searchCriteria;
		}

		@Override
		public ComponentContainer constructHeader() {
			return BugSearchPanel.this.constructHeader();
		}
	}

	private class BugAdvancedSearchLayout extends
			DynamicQueryParamLayout<BugSearchCriteria> {
		private static final long serialVersionUID = 1L;

		public BugAdvancedSearchLayout() {
			super(BugSearchPanel.this, ProjectTypeConstants.BUG);
		}

		@Override
		public ComponentContainer constructHeader() {
			return BugSearchPanel.this.constructHeader();
		}

		@Override
		protected Class<BugSearchCriteria> getType() {
			return BugSearchCriteria.class;
		}

		@Override
		public Param[] getParamFields() {
			return paramFields;
		}

		@Override
		protected Component buildSelectionComp(String fieldId) {
			if ("bug-assignuser".equals(fieldId)) {
				return new ProjectMemberListSelect(false);
			}
			return null;
		}
	}
}
