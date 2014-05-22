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

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.billing.RegisterStatusConstants;
import com.esofthead.mycollab.module.user.domain.criteria.UserSearchCriteria;
import com.esofthead.mycollab.module.user.events.UserEvent;
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
public class UserSearchPanel extends GenericSearchPanel<UserSearchCriteria> {
	private static final long serialVersionUID = 1L;
	private UserSearchCriteria searchCriteria;

	public UserSearchPanel() {
		this.setCompositionRoot(new UserBasicSearchLayout());
	}

	private HorizontalLayout createSearchTopPanel() {
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);

		final Label searchtitle = new Label("Users");
		searchtitle.setStyleName(Reindeer.LABEL_H2);
		layout.addComponent(searchtitle);
		layout.setComponentAlignment(searchtitle, Alignment.MIDDLE_LEFT);

		final Button createBtn = new Button("Invite",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final Button.ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new UserEvent.GotoAdd(this, null));
					}
				});
		createBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		createBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		createBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.ACCOUNT_USER));

		UiUtils.addComponent(layout, createBtn, Alignment.MIDDLE_RIGHT);

		return layout;
	}

	@SuppressWarnings("rawtypes")
	private class UserBasicSearchLayout extends BasicSearchLayout {

		@SuppressWarnings("unchecked")
		public UserBasicSearchLayout() {
			super(UserSearchPanel.this);
		}

		private static final long serialVersionUID = 1L;
		private TextField nameField;

		@Override
		public ComponentContainer constructHeader() {
			return UserSearchPanel.this.createSearchTopPanel();
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

			final Button searchBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH_LABEL));
			searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			searchBtn.setIcon(MyCollabResource
					.newResource("icons/16/search.png"));
			searchBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final Button.ClickEvent event) {
					UserBasicSearchLayout.this.callSearchAction();
				}
			});
			searchComp.addComponent(searchBtn);
			basicSearchBody.addComponent(searchComp);

			final Button clearBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR_LABEL),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final Button.ClickEvent event) {
							UserBasicSearchLayout.this.nameField.setValue("");
						}
					});
			clearBtn.setStyleName(UIConstants.THEME_BLANK_LINK);
			clearBtn.addStyleName("cancel-button");
			basicSearchBody.addComponent(clearBtn);
			basicSearchBody.setComponentAlignment(clearBtn,
					Alignment.MIDDLE_LEFT);
			return basicSearchBody;
		}

		@Override
		protected SearchCriteria fillupSearchCriteria() {
			UserSearchPanel.this.searchCriteria = new UserSearchCriteria();
			UserSearchPanel.this.searchCriteria
					.setSaccountid(new NumberSearchField(AppContext
							.getAccountId()));
			UserSearchPanel.this.searchCriteria
					.setRegisterStatuses(new SetSearchField<String>(
							SearchField.AND,
							new String[] {
									RegisterStatusConstants.ACTIVE,
									RegisterStatusConstants.SENT_VERIFICATION_EMAIL,
									RegisterStatusConstants.VERIFICATING }));

			if (StringUtils
					.isNotNullOrEmpty((String) this.nameField.getValue())) {
				UserSearchPanel.this.searchCriteria
						.setDisplayName(new StringSearchField(SearchField.AND,
								(String) this.nameField.getValue()));
			}
			return UserSearchPanel.this.searchCriteria;
		}
	}
}
