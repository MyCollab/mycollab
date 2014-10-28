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
package com.esofthead.mycollab.module.project.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class FollowingTicketSearchPanel extends
		DefaultGenericSearchPanel<FollowingTicketSearchCriteria> {

	private static final long serialVersionUID = 1L;

	private FollowingTicketBasicSearchLayout basicSearchLayout;

	private FollowingTicketSearchCriteria searchCriteria;

	private List<SimpleProject> projects;

	@Override
	@SuppressWarnings("unchecked")
	protected SearchLayout<FollowingTicketSearchCriteria> createBasicSearchLayout() {
		basicSearchLayout = new FollowingTicketBasicSearchLayout();
		return basicSearchLayout;
	}

	@Override
	protected SearchLayout<FollowingTicketSearchCriteria> createAdvancedSearchLayout() {
		return null;
	}

	public void doSearch() {
		basicSearchLayout.callSearchAction();
	}

	@SuppressWarnings("rawtypes")
	private class FollowingTicketBasicSearchLayout extends BasicSearchLayout {

		private static final long serialVersionUID = 1L;

		private UserInvolvedProjectsListSelect projectField;
		private TextField summaryField;
		private CheckBox taskSelect, bugSelect, problemSelect, riskSelect;

		@SuppressWarnings("unchecked")
		public FollowingTicketBasicSearchLayout() {
			super(FollowingTicketSearchPanel.this);
		}

		@Override
		public ComponentContainer constructHeader() {
			return new HorizontalLayout();
		}

		@Override
		public ComponentContainer constructBody() {
			final HorizontalLayout basicSearchBody = new HorizontalLayout();
			basicSearchBody.setSpacing(true);
			basicSearchBody.setMargin(true);

			final GridLayout selectionLayout = new GridLayout(5, 2);
			selectionLayout.setSpacing(true);
			selectionLayout.setDefaultComponentAlignment(Alignment.TOP_RIGHT);
			selectionLayout.setMargin(true);
			basicSearchBody.addComponent(selectionLayout);

			VerticalLayout summaryLbWrapper = new VerticalLayout();
			summaryLbWrapper.setWidth("70px");
			Label summaryLb = new Label("Summary:");
			summaryLb.setWidthUndefined();
			UiUtils.addComponent(summaryLbWrapper, summaryLb,
					Alignment.TOP_RIGHT);
			selectionLayout.addComponent(summaryLbWrapper, 0, 0);

			this.summaryField = new TextField();
			this.summaryField.setWidth("100%");
			selectionLayout.addComponent(this.summaryField, 1, 0);

			VerticalLayout typeLbWrapper = new VerticalLayout();
			typeLbWrapper.setWidth("70px");
			Label typeLb = new Label("Type:");
			typeLb.setWidthUndefined();
			UiUtils.addComponent(typeLbWrapper, typeLb, Alignment.TOP_RIGHT);
			selectionLayout.addComponent(typeLbWrapper, 0, 1);

			HorizontalLayout typeSelectWrapper = new HorizontalLayout();
			typeSelectWrapper.setSpacing(true);
			typeSelectWrapper.setMargin(new MarginInfo(false, true, false,
					false));
			selectionLayout.addComponent(typeSelectWrapper, 1, 1);

			this.taskSelect = new CheckBox("Task");
			this.taskSelect.setIcon(MyCollabResource
					.newResource("icons/16/project/task.png"));
			this.taskSelect.setValue(true);
			typeSelectWrapper.addComponent(this.taskSelect);

			this.bugSelect = new CheckBox("Bug");
			this.bugSelect.setIcon(MyCollabResource
					.newResource("icons/16/project/bug.png"));
			this.bugSelect.setValue(true);
			typeSelectWrapper.addComponent(this.bugSelect);

			this.problemSelect = new CheckBox("Problem");
			this.problemSelect.setIcon(MyCollabResource
					.newResource("icons/16/project/problem.png"));
			this.problemSelect.setValue(true);
			typeSelectWrapper.addComponent(this.problemSelect);

			this.riskSelect = new CheckBox("Risk");
			this.riskSelect.setIcon(MyCollabResource
					.newResource("icons/16/project/risk.png"));
			this.riskSelect.setValue(true);
			typeSelectWrapper.addComponent(this.riskSelect);

			VerticalLayout projectLbWrapper = new VerticalLayout();
			projectLbWrapper.setWidth("70px");
			Label projectLb = new Label("Project:");
			projectLb.setWidthUndefined();
			UiUtils.addComponent(projectLbWrapper, projectLb,
					Alignment.TOP_RIGHT);
			selectionLayout.addComponent(projectLbWrapper, 2, 0);

			this.projectField = new UserInvolvedProjectsListSelect();
			this.projectField.setWidth("300px");
			this.projectField.setItemCaptionMode(ItemCaptionMode.EXPLICIT);
			this.projectField.setNullSelectionAllowed(false);
			this.projectField.setMultiSelect(true);
			this.projectField.setRows(4);
			selectionLayout.addComponent(this.projectField, 3, 0, 3, 1);

			final Button queryBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SUBMIT),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							FollowingTicketSearchPanel.this.doSearch();
						}
					});
			queryBtn.setStyleName(UIConstants.THEME_GREEN_LINK);

			VerticalLayout queryBtnWrapper = new VerticalLayout();
			queryBtnWrapper.setWidth("100px");
			UiUtils.addComponent(queryBtnWrapper, queryBtn, Alignment.TOP_RIGHT);
			selectionLayout.addComponent(queryBtnWrapper, 4, 0);

			return basicSearchBody;
		}

		@Override
		@SuppressWarnings("unchecked")
		protected SearchCriteria fillupSearchCriteria() {
			searchCriteria = new FollowingTicketSearchCriteria();
			searchCriteria.setUser(new StringSearchField(AppContext
					.getUsername()));

			List<String> types = new ArrayList<String>();
			if (this.taskSelect.getValue()) {
				types.add("Project-Task");
			}
			if (this.bugSelect.getValue()) {
				types.add("Project-Bug");
			}
			if (this.problemSelect.getValue()) {
				types.add("Project-Problem");
			}
			if (this.riskSelect.getValue()) {
				types.add("Project-Risk");
			}

			if (types.size() > 0) {
				searchCriteria.setTypes(new SetSearchField<>(types
						.toArray(new String[types.size()])));
			} else {
				searchCriteria.setTypes(null);
			}

			String summary = summaryField.getValue().toString().trim();
			searchCriteria.setSummary(new StringSearchField(StringUtils
					.isEmpty(summary) ? "" : summary));

			Collection<Integer> selectedProjects = (Collection<Integer>) projectField
					.getValue();
			if (CollectionUtils.isNotEmpty(selectedProjects)) {
				searchCriteria.setExtraTypeIds(new SetSearchField<Integer>(
						SearchField.AND, selectedProjects));
			} else {
				List<Integer> keys = new ArrayList<Integer>();
				for (SimpleProject project : projects) {
					keys.add(project.getId());
				}
				searchCriteria.setExtraTypeIds(new SetSearchField<Integer>(
						SearchField.AND, keys));
			}

			return searchCriteria;
		}
	}

	private class UserInvolvedProjectsListSelect extends ListSelect {
		private static final long serialVersionUID = 1L;

		public UserInvolvedProjectsListSelect() {
			FollowingTicketSearchPanel.this.projects = ApplicationContextUtil
					.getSpringBean(ProjectService.class)
					.getProjectsUserInvolved(AppContext.getUsername(),
							AppContext.getAccountId());

			for (SimpleProject project : projects) {
				this.addItem(project.getId());
				this.setItemCaption(project.getId(), project.getName());
			}
		}
	}
}
