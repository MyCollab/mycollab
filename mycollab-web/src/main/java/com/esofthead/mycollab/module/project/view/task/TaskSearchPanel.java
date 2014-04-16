package com.esofthead.mycollab.module.project.view.task;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.module.project.CurrentProjectVariables;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.criteria.TaskSearchCriteria;
import com.esofthead.mycollab.module.project.view.settings.component.ProjectMemberListSelect;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.DynamicQueryParamLayout;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class TaskSearchPanel extends
		DefaultGenericSearchPanel<TaskSearchCriteria> {

	private static final long serialVersionUID = 1L;
	protected TaskSearchCriteria searchCriteria;

	private static Param[] paramFields = new Param[] {
			TaskSearchCriteria.p_assignee, TaskSearchCriteria.p_createtime,
			TaskSearchCriteria.p_duedate, TaskSearchCriteria.p_lastupdatedtime,
			TaskSearchCriteria.p_status };

	@Override
	protected SearchLayout<TaskSearchCriteria> createBasicSearchLayout() {
		return new TaskBasicSearchLayout();
	}

	@Override
	protected SearchLayout<TaskSearchCriteria> createAdvancedSearchLayout() {
		return new TaskAdvancedSearchLayout();
	}

	public void setTextField(String name) {
		if (getCompositionRoot() instanceof TaskBasicSearchLayout) {
			((TaskBasicSearchLayout) getCompositionRoot()).setNameField(name);
		}
	}
	
	public void getAdvanceSearch() {
		moveToAdvancedSearchLayout();
	}
	
	public void getBasicSearch() {
		moveToBasicSearchLayout();
	}

	private class TaskBasicSearchLayout extends
			BasicSearchLayout<TaskSearchCriteria> {

		private static final long serialVersionUID = 1L;

		public TaskBasicSearchLayout() {
			super(TaskSearchPanel.this);
		}

		private TextField nameField;
		private CheckBox myItemCheckbox;

		public void setNameField(String value) {
			this.nameField.setValue(value);
		}

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

			this.myItemCheckbox = new CheckBox("My Items");
			this.myItemCheckbox.setWidth("75px");
			UiUtils.addComponent(basicSearchBody, this.myItemCheckbox,
					Alignment.MIDDLE_CENTER);

			final Button searchBtn = new Button("Search");
			searchBtn.setIcon(MyCollabResource
					.newResource("icons/16/search.png"));
			searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			searchBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					callSearchAction();
				}
			});
			UiUtils.addComponent(basicSearchBody, searchBtn,
					Alignment.MIDDLE_LEFT);

			final Button cancelBtn = new Button(
					LocalizationHelper.getMessage(GenericI18Enum.BUTTON_CLEAR));
			cancelBtn.setStyleName(UIConstants.THEME_BLANK_LINK);
			cancelBtn.addStyleName("cancel-button");
			cancelBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					nameField.setValue("");
				}
			});
			UiUtils.addComponent(basicSearchBody, cancelBtn,
					Alignment.MIDDLE_CENTER);

			final Button advancedSearchBtn = new Button(
					LocalizationHelper
							.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							moveToAdvancedSearchLayout();
						}
					});
			advancedSearchBtn.setStyleName("link");
			UiUtils.addComponent(basicSearchBody, advancedSearchBtn,
					Alignment.MIDDLE_CENTER);
			return basicSearchBody;
		}

		@Override
		protected TaskSearchCriteria fillupSearchCriteria() {
			TaskSearchPanel.this.searchCriteria = new TaskSearchCriteria();
			TaskSearchPanel.this.searchCriteria
					.setProjectid(new NumberSearchField(CurrentProjectVariables
							.getProjectId()));
			TaskSearchPanel.this.searchCriteria
					.setTaskName(new StringSearchField(this.nameField
							.getValue().toString().trim()));
			if (this.myItemCheckbox.getValue()) {
				TaskSearchPanel.this.searchCriteria
						.setAssignUser(new StringSearchField(SearchField.AND,
								AppContext.getUsername()));
			} else {
				TaskSearchPanel.this.searchCriteria.setAssignUser(null);
			}
			return TaskSearchPanel.this.searchCriteria;
		}

		@Override
		public ComponentContainer constructHeader() {
			return new HorizontalLayout();
		}

	}

	private class TaskAdvancedSearchLayout extends
			DynamicQueryParamLayout<TaskSearchCriteria> {
		private static final long serialVersionUID = 1L;

		public TaskAdvancedSearchLayout() {
			super(TaskSearchPanel.this, ProjectTypeConstants.TASK);
		}

		@Override
		public ComponentContainer constructHeader() {
			return new HorizontalLayout();
		}

		@Override
		protected Class<TaskSearchCriteria> getType() {
			return TaskSearchCriteria.class;
		}

		@Override
		public Param[] getParamFields() {
			return paramFields;
		}

		@Override
		protected Component buildSelectionComp(String fieldId) {
			if ("task-assignuser".equals(fieldId)) {
				return new ProjectMemberListSelect();
			}
			return null;
		}
	}

}
