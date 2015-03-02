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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.module.project.ProjectTypeConstants;
import com.esofthead.mycollab.module.project.domain.SimpleProject;
import com.esofthead.mycollab.module.project.domain.criteria.FollowingTicketSearchCriteria;
import com.esofthead.mycollab.module.project.service.ProjectService;
import com.esofthead.mycollab.module.project.ui.ProjectAssetsManager;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.HeaderWithFontAwesome;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 4.0.0
 */
public class FollowingTicketSearchPanel extends
		DefaultGenericSearchPanel<FollowingTicketSearchCriteria> {

	private static final long serialVersionUID = 1L;

	private FollowingTicketBasicSearchLayout basicSearchLayout;

	private List<SimpleProject> projects;

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return null;
    }

    @Override
    protected void buildExtraControls() {

    }

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
			return null;
		}

		@Override
		public ComponentContainer constructBody() {
			final MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);

			final GridLayout selectionLayout = new GridLayout(5, 2);
			selectionLayout.setSpacing(true);
			selectionLayout.setDefaultComponentAlignment(Alignment.TOP_RIGHT);
			selectionLayout.setMargin(true);
			basicSearchBody.addComponent(selectionLayout);

			Label summaryLb = new Label("Summary:");
			summaryLb.setWidthUndefined();
			selectionLayout.addComponent(summaryLb, 0, 0);

			this.summaryField = new TextField();
			this.summaryField.setWidth("100%");
			selectionLayout.addComponent(this.summaryField, 1, 0);

			Label typeLb = new Label("Type:");
			typeLb.setWidthUndefined();

			selectionLayout.addComponent(typeLb, 0, 1);

			MHorizontalLayout typeSelectWrapper = new MHorizontalLayout().withMargin(new MarginInfo(false, true, false,
                    false));
			selectionLayout.addComponent(typeSelectWrapper, 1, 1);

			this.taskSelect = new CheckBox("Task", true);
			this.taskSelect.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.TASK));

			this.bugSelect = new CheckBox("Bug", true);
			this.bugSelect.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.BUG));

			this.problemSelect = new CheckBox("Problem", true);
			this.problemSelect.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.PROBLEM));

			this.riskSelect = new CheckBox("Risk", true);
			this.riskSelect.setIcon(ProjectAssetsManager.getAsset(ProjectTypeConstants.RISK));

            typeSelectWrapper.with(this.taskSelect, this.bugSelect, this.riskSelect, this.problemSelect);

			Label projectLb = new Label("Project:");
			projectLb.setWidthUndefined();
			selectionLayout.addComponent(projectLb, 2, 0);

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

			selectionLayout.addComponent(queryBtn, 4, 0);

			return basicSearchBody;
		}

		@Override
		@SuppressWarnings("unchecked")
		protected SearchCriteria fillUpSearchCriteria() {
			FollowingTicketSearchCriteria searchCriteria = new FollowingTicketSearchCriteria();
			searchCriteria.setUser(new StringSearchField(AppContext
					.getUsername()));

			List<String> types = new ArrayList<>();
			if (this.taskSelect.getValue()) {
				types.add(ProjectTypeConstants.TASK);
			}
			if (this.bugSelect.getValue()) {
				types.add(ProjectTypeConstants.BUG);
			}
			if (this.problemSelect.getValue()) {
				types.add(ProjectTypeConstants.PROBLEM);
			}
			if (this.riskSelect.getValue()) {
				types.add(ProjectTypeConstants.RISK);
			}

			if (types.size() > 0) {
				searchCriteria.setTypes(new SetSearchField<>(types
						.toArray(new String[types.size()])));
			} else {
				searchCriteria.setTypes(null);
			}

			String summary = summaryField.getValue().trim();
			searchCriteria.setSummary(new StringSearchField(StringUtils
					.isEmpty(summary) ? "" : summary));

			Collection<Integer> selectedProjects = (Collection<Integer>) projectField
					.getValue();
			if (CollectionUtils.isNotEmpty(selectedProjects)) {
				searchCriteria.setExtraTypeIds(new SetSearchField<>(
						SearchField.AND, selectedProjects));
			} else {
				List<Integer> keys = new ArrayList<>();
				for (SimpleProject project : projects) {
					keys.add(project.getId());
				}
				searchCriteria.setExtraTypeIds(new SetSearchField<>(
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
