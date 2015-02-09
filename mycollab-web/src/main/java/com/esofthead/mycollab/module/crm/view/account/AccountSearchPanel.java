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
package com.esofthead.mycollab.module.crm.view.account;

import com.vaadin.server.FontAwesome;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.events.AccountEvent;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserListSelect;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.DynamicQueryParamLayout;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.WebResourceIds;
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
@SuppressWarnings("serial")
public class AccountSearchPanel extends
		DefaultGenericSearchPanel<AccountSearchCriteria> {

	private static Param[] paramFields = new Param[] {
			AccountSearchCriteria.p_accountName,
			AccountSearchCriteria.p_anyPhone, AccountSearchCriteria.p_website,
			AccountSearchCriteria.p_numemployees,
			AccountSearchCriteria.p_assignee,
			AccountSearchCriteria.p_industries, AccountSearchCriteria.p_types,
			AccountSearchCriteria.p_assignee,
			AccountSearchCriteria.p_billingCountry,
			AccountSearchCriteria.p_shippingCountry,
			AccountSearchCriteria.p_anyCity,
			AccountSearchCriteria.p_createdtime,
			AccountSearchCriteria.p_lastupdatedtime };

	private HorizontalLayout createSearchTopPanel() {
		final MHorizontalLayout layout = new MHorizontalLayout()
				.withStyleName(UIConstants.HEADER_VIEW).withWidth("100%")
				.withSpacing(true)
				.withMargin(new MarginInfo(true, false, true, false));

		final Image titleIcon = new Image(null,
				MyCollabResource.newResource(WebResourceIds._22_crm_account));
		layout.with(titleIcon).withAlign(titleIcon, Alignment.MIDDLE_LEFT);

		final Label searchTitle = new Label(
				AppContext.getMessage(AccountI18nEnum.VIEW_LIST_TITLE));
		searchTitle.setStyleName(UIConstants.HEADER_TEXT);

		layout.with(searchTitle).withAlign(searchTitle, Alignment.MIDDLE_LEFT)
				.expand(searchTitle);

		final Button createAccountBtn = new Button(
				AppContext.getMessage(AccountI18nEnum.BUTTON_NEW_ACCOUNT),
				new Button.ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {
						EventBusFactory.getInstance().post(
								new AccountEvent.GotoAdd(this, null));
					}
				});
		createAccountBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		createAccountBtn.setIcon(FontAwesome.PLUS);
		createAccountBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_ACCOUNT));

		layout.with(createAccountBtn).withAlign(createAccountBtn,
				Alignment.MIDDLE_RIGHT);

		return layout;
	}

	@Override
	protected BasicSearchLayout<AccountSearchCriteria> createBasicSearchLayout() {
		return new AccountBasicSearchLayout();
	}

	@Override
	protected SearchLayout<AccountSearchCriteria> createAdvancedSearchLayout() {
		return new AccountAdvancedSearchLayout();
	}

	private class AccountAdvancedSearchLayout extends
			DynamicQueryParamLayout<AccountSearchCriteria> {

		public AccountAdvancedSearchLayout() {
			super(AccountSearchPanel.this, CrmTypeConstants.ACCOUNT);
		}

		@Override
		public ComponentContainer constructHeader() {
			return AccountSearchPanel.this.createSearchTopPanel();
		}

		@Override
		public Param[] getParamFields() {
			return paramFields;
		}

		@Override
		protected Class<AccountSearchCriteria> getType() {
			return AccountSearchCriteria.class;
		}

		@Override
		protected Component buildSelectionComp(String fieldId) {
			if ("account-assignuser".equals(fieldId)) {
				return new ActiveUserListSelect();
			}
			return null;
		}
	}

	private class AccountBasicSearchLayout extends
			BasicSearchLayout<AccountSearchCriteria> {

		private TextField nameField;
		private CheckBox myItemCheckbox;

		public AccountBasicSearchLayout() {
			super(AccountSearchPanel.this);
		}

		@Override
		public ComponentContainer constructBody() {
			final MHorizontalLayout basicSearchBody = new MHorizontalLayout()
					.withSpacing(true).withMargin(true);

			this.nameField = this.createSeachSupportTextField(new TextField(),
					"NameFieldOfBasicSearch");
			this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);

			basicSearchBody.with(nameField).withAlign(nameField,
					Alignment.MIDDLE_CENTER);

			this.myItemCheckbox = new CheckBox(
					AppContext
							.getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
			this.myItemCheckbox.setWidth("75px");

			basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox,
					Alignment.MIDDLE_CENTER);

			final Button searchBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
			searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			searchBtn.setIcon(FontAwesome.SEARCH);
			searchBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					AccountBasicSearchLayout.this.callSearchAction();
				}
			});

			basicSearchBody.with(searchBtn).withAlign(searchBtn,
					Alignment.MIDDLE_LEFT);

			final Button cancelBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR));
			cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
			cancelBtn.addStyleName("cancel-button");
			cancelBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					AccountBasicSearchLayout.this.nameField.setValue("");
				}
			});
			basicSearchBody.with(cancelBtn).withAlign(cancelBtn,
					Alignment.MIDDLE_CENTER);

			final Button advancedSearchBtn = new Button(
					AppContext
							.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
					new Button.ClickListener() {
						@Override
						public void buttonClick(final ClickEvent event) {
							AccountSearchPanel.this
									.moveToAdvancedSearchLayout();
						}
					});
			advancedSearchBtn.setStyleName("link");
			basicSearchBody.with(advancedSearchBtn).withAlign(
					advancedSearchBtn, Alignment.MIDDLE_CENTER);
			return basicSearchBody;
		}

		@Override
		public ComponentContainer constructHeader() {
			return AccountSearchPanel.this.createSearchTopPanel();
		}

		@Override
		protected AccountSearchCriteria fillupSearchCriteria() {
			final AccountSearchCriteria searchCriteria = new AccountSearchCriteria();
			searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
					AppContext.getAccountId()));
			searchCriteria.setAccountname(new StringSearchField(
					SearchField.AND, this.nameField.getValue().trim()));
			if (this.myItemCheckbox.getValue()) {
				searchCriteria.setAssignUser(new StringSearchField(
						SearchField.AND, AppContext.getUsername()));
			} else {
				searchCriteria.setAssignUsers(null);
			}

			return searchCriteria;
		}
	}

}
