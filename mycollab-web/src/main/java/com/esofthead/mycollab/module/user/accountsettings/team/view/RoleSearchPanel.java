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

package com.esofthead.mycollab.module.user.accountsettings.team.view;

import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.user.domain.criteria.RoleSearchCriteria;
import com.esofthead.mycollab.module.user.events.RoleEvent;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.GenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class RoleSearchPanel extends GenericSearchPanel<RoleSearchCriteria> {
	private static final long serialVersionUID = 1L;
	private RoleSearchCriteria searchCriteria;

	public RoleSearchPanel() {
		this.setCompositionRoot(new RoleBasicSearchLayout());
	}

	private HorizontalLayout createSearchTopPanel() {
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);

		final Label searchtitle = new Label("Roles");
		searchtitle.setStyleName(Reindeer.LABEL_H2);
		layout.addComponent(searchtitle);
		layout.setComponentAlignment(searchtitle, Alignment.MIDDLE_LEFT);

		final Button createBtn = new Button("Create",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final Button.ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new RoleEvent.GotoAdd(this, null));
					}
				});
		createBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		createBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		createBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.ACCOUNT_ROLE));

		UiUtils.addComponent(layout, createBtn, Alignment.MIDDLE_RIGHT);

		return layout;
	}

	@SuppressWarnings("rawtypes")
	private class RoleBasicSearchLayout extends
			GenericSearchPanel.BasicSearchLayout {

		@SuppressWarnings("unchecked")
		public RoleBasicSearchLayout() {
			super(RoleSearchPanel.this);
		}

		private static final long serialVersionUID = 1L;
		private TextField nameField;

		@Override
		public ComponentContainer constructHeader() {
			return RoleSearchPanel.this.createSearchTopPanel();
		}

		@Override
		public ComponentContainer constructBody() {
			final HorizontalLayout basicSearchBody = new HorizontalLayout();
			basicSearchBody.addComponent(new Label("Name"));
			basicSearchBody.setSpacing(true);
			basicSearchBody.setMargin(true);

			final HorizontalLayout searchComp = new HorizontalLayout();
			searchComp.addStyleName("search-comp");
			this.nameField = new TextField();
			this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
			searchComp.addComponent(this.nameField);

			final Button searchBtn = new Button();
			searchBtn.setStyleName("search-icon-button");
			searchBtn.setIcon(MyCollabResource
					.newResource("icons/16/search_white.png"));
			searchBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final Button.ClickEvent event) {
					RoleBasicSearchLayout.this.callSearchAction();
				}
			});
			searchComp.addComponent(searchBtn);
			basicSearchBody.addComponent(searchComp);

			final Button clearBtn = new Button("Clear",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final Button.ClickEvent event) {
							RoleBasicSearchLayout.this.nameField.setValue("");
						}
					});
			clearBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
			clearBtn.addStyleName("cancel-button");
			basicSearchBody.addComponent(clearBtn);
			basicSearchBody.setComponentAlignment(clearBtn,
					Alignment.MIDDLE_LEFT);
			return basicSearchBody;
		}

		@Override
		protected SearchCriteria fillupSearchCriteria() {
			RoleSearchPanel.this.searchCriteria = new RoleSearchCriteria();
			if (StringUtils
					.isNotNullOrEmpty((String) this.nameField.getValue())) {
				RoleSearchPanel.this.searchCriteria
						.setRoleName(new StringSearchField(SearchField.AND,
								(String) this.nameField.getValue()));
			}
			return RoleSearchPanel.this.searchCriteria;
		}
	}
}
