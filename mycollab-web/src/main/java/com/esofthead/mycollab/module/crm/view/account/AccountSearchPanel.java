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

import java.util.Arrays;
import java.util.Collection;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.events.AccountEvent;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserListSelect;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DefaultAdvancedSearchLayout;
import com.esofthead.mycollab.vaadin.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.Separator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.esofthead.mycollab.web.MyCollabResource;
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
import com.vaadin.ui.themes.Reindeer;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 * 
 */
@SuppressWarnings("serial")
public class AccountSearchPanel extends
		DefaultGenericSearchPanel<AccountSearchCriteria> {

	public class AccountAdvancedSearchLayout extends
			DefaultAdvancedSearchLayout<AccountSearchCriteria> {

		private TextField nameField;
		private TextField websiteField;
		private TextField anyPhoneField;
		private TextField anyMailField;
		private TextField anyAddressField;
		private TextField cityField;
		private AccountIndustryListSelect industryField;
		private AccountTypeListSelect typeField;
		private ActiveUserListSelect userField;

		public AccountAdvancedSearchLayout() {
			super(AccountSearchPanel.this, CrmTypeConstants.ACCOUNT);
		}

		@Override
		public ComponentContainer constructBody() {

			GridFormLayoutHelper gridLayout = new GridFormLayoutHelper(3, 3,
					"100%", "90px");
			gridLayout.getLayout().setWidth("100%");
			gridLayout.getLayout().setMargin(
					new MarginInfo(true, true, true, false));

			this.nameField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(), "nameField"),
					"Name", 0, 0);

			this.websiteField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(),
							"websiteField"), "Website", 1, 0);

			this.anyPhoneField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(),
							"anyPhoneField"), "Any Phone", 2, 0);

			this.anyMailField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(),
							"anyMailField"), "Any Email", 0, 1);

			this.anyAddressField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(),
							"anyAddressField"), "Any Address", 1, 1);

			this.cityField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(), "cityField"),
					"City", 2, 1);

			this.industryField = (AccountIndustryListSelect) gridLayout
					.addComponent(
							this.createSeachSupportComboBox(new AccountIndustryListSelect()),
							"Industry", 0, 2);

			this.typeField = (AccountTypeListSelect) gridLayout
					.addComponent(
							this.createSeachSupportComboBox(new AccountTypeListSelect()),
							"Type", 1, 2);

			this.userField = (ActiveUserListSelect) gridLayout
					.addComponent(
							this.createSeachSupportComboBox(new ActiveUserListSelect()),
							LocalizationHelper
									.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD),
							2, 2);
			gridLayout.getLayout().setSpacing(true);
			return gridLayout.getLayout();
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
					SearchField.AND, (String) this.nameField.getValue()));

			if (StringUtils
					.isNotNullOrEmpty((String) this.nameField.getValue())) {
				searchCriteria.setAccountname(new StringSearchField(
						SearchField.AND, (String) this.nameField.getValue()));
			}

			if (StringUtils.isNotNullOrEmpty((String) this.websiteField
					.getValue())) {
				searchCriteria
						.setWebsite(new StringSearchField(SearchField.AND,
								(String) this.websiteField.getValue()));
			}

			if (StringUtils.isNotNullOrEmpty((String) this.anyPhoneField
					.getValue())) {
				searchCriteria
						.setAnyPhone(new StringSearchField(SearchField.AND,
								(String) this.anyPhoneField.getValue()));
			}

			if (StringUtils.isNotNullOrEmpty((String) this.anyAddressField
					.getValue())) {
				searchCriteria.setAnyAddress(new StringSearchField(
						SearchField.AND, (String) this.anyAddressField
								.getValue()));
			}

			if (StringUtils.isNotNullOrEmpty((String) this.anyMailField
					.getValue())) {
				searchCriteria
						.setAnyMail(new StringSearchField(SearchField.AND,
								(String) this.anyMailField.getValue()));
			}

			if (StringUtils
					.isNotNullOrEmpty((String) this.cityField.getValue())) {
				searchCriteria.setAnyCity(new StringSearchField(
						SearchField.AND, (String) this.cityField.getValue()));
			}

			final Collection<String> industries = (Collection<String>) this.industryField
					.getValue();
			if (industries != null && industries.size() > 0) {
				searchCriteria.setIndustries(new SetSearchField(
						SearchField.AND, industries));
			}

			final Collection<String> types = (Collection<String>) this.typeField
					.getValue();
			if (types != null && types.size() > 0) {
				searchCriteria.setTypes(new SetSearchField(SearchField.AND,
						types));
			}

			final Collection<String> users = (Collection<String>) this.userField
					.getValue();
			if (users != null && users.size() > 0) {
				searchCriteria.setAssignUsers(new SetSearchField(
						SearchField.AND, users));
			}

			return searchCriteria;
		}

		@Override
		protected void clearFields() {
			this.nameField.setValue("");
			this.websiteField.setValue("");
			this.anyPhoneField.setValue("");
			this.anyMailField.setValue("");
			this.anyAddressField.setValue("");
			this.cityField.setValue("");
			this.industryField.setValue(null);
			this.typeField.setValue(null);
			this.userField.setValue(null);
		}

		@Override
		protected void loadSaveSearchToField(final AccountSearchCriteria value) {
			if (value != null) {
				if (value.getAccountname() != null) {
					this.nameField.setValue(value.getAccountname().getValue());
				} else {
					this.nameField.setValue("");
				}
				if (value.getWebsite() != null) {
					this.websiteField.setValue(value.getWebsite().getValue());
				} else {
					this.websiteField.setValue("");
				}
				if (value.getAnyPhone() != null) {
					this.anyPhoneField.setValue(value.getAnyPhone().getValue());
				} else {
					this.anyPhoneField.setValue("");
				}
				if (value.getAnyMail() != null) {
					this.anyMailField.setValue(value.getAnyMail().getValue());
				} else {
					this.anyMailField.setValue("");
				}
				if (value.getAnyAddress() != null) {
					this.anyAddressField.setValue(value.getAnyAddress()
							.getValue());
				} else {
					this.anyAddressField.setValue("");
				}
				if (value.getAnyCity() != null) {
					this.cityField.setValue(value.getAnyCity().getValue());
				} else {
					this.cityField.setValue("");
				}
				if (value.getIndustries() != null) {
					final Object[] userString = value.getIndustries().values;
					this.industryField.setValue(Arrays.asList(userString));
				} else {
					this.industryField.setValue(null);
				}
				if (value.getTypes() != null) {
					final Object[] typeObj = value.getTypes().values;
					this.typeField.setValue(Arrays.asList(typeObj));
				} else {
					this.typeField.setValue(null);
				}
				if (value.getAssignUsers() != null) {
					final Object[] userObj = value.getAssignUsers().values;
					this.userField.setValue(Arrays.asList(userObj));
				} else {
					this.userField.setValue(null);
				}
			}
		}

		@Override
		protected Class<AccountSearchCriteria> getType() {
			return AccountSearchCriteria.class;
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
			final HorizontalLayout basicSearchBody = new HorizontalLayout();
			basicSearchBody.setSpacing(false);
			basicSearchBody.setMargin(true);
			// basicSearchBody.addComponent(new Label("Name"));

			this.nameField = this.createSeachSupportTextField(new TextField(),
					"NameFieldOfBasicSearch");

			this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
			UiUtils.addComponent(basicSearchBody, this.nameField,
					Alignment.MIDDLE_CENTER);
			// final Button searchBtn = new Button(
			// LocalizationHelper
			// .getMessage(CrmCommonI18nEnum.BUTTON_SEARCH));

			final Button searchBtn = new Button();
			searchBtn.setStyleName("search-icon-button");
			searchBtn.setIcon(MyCollabResource
					.newResource("icons/16/search_white.png"));
			searchBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					AccountBasicSearchLayout.this.callSearchAction();
				}
			});

			UiUtils.addComponent(basicSearchBody, searchBtn,
					Alignment.MIDDLE_LEFT);

			this.myItemCheckbox = new CheckBox(
					LocalizationHelper
							.getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
			this.myItemCheckbox.setWidth("75px");
			UiUtils.addComponent(basicSearchBody, this.myItemCheckbox,
					Alignment.MIDDLE_CENTER);

			final Separator separator1 = new Separator();

			UiUtils.addComponent(basicSearchBody, separator1,
					Alignment.MIDDLE_LEFT);

			final Button cancelBtn = new Button(
					LocalizationHelper.getMessage(GenericI18Enum.BUTTON_CLEAR));
			cancelBtn.setStyleName(UIConstants.THEME_LINK);
			cancelBtn.addStyleName("cancel-button");
			cancelBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					AccountBasicSearchLayout.this.nameField.setValue("");
				}
			});
			UiUtils.addComponent(basicSearchBody, cancelBtn,
					Alignment.MIDDLE_CENTER);

			final Separator separator2 = new Separator();
			UiUtils.addComponent(basicSearchBody, separator2,
					Alignment.MIDDLE_LEFT);

			final Button advancedSearchBtn = new Button(
					LocalizationHelper
							.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
					new Button.ClickListener() {
						@Override
						public void buttonClick(final ClickEvent event) {
							AccountSearchPanel.this
									.moveToAdvancedSearchLayout();
						}
					});
			advancedSearchBtn.setStyleName("link");
			UiUtils.addComponent(basicSearchBody, advancedSearchBtn,
					Alignment.MIDDLE_CENTER);
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
					SearchField.AND, ((String) this.nameField.getValue())
							.trim()));
			if (this.myItemCheckbox.getValue()) {
				searchCriteria.setAssignUser(new StringSearchField(
						SearchField.AND, AppContext.getUsername()));
			} else {
				searchCriteria.setAssignUsers(null);
			}

			return searchCriteria;
		}
	}

	private HorizontalLayout createSearchTopPanel() {
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);

		final Image titleIcon = new Image(null,
				MyCollabResource.newResource("icons/22/crm/account.png"));
		layout.addComponent(titleIcon);
		layout.setComponentAlignment(titleIcon, Alignment.MIDDLE_LEFT);

		final Label searchtitle = new Label("Accounts");
		searchtitle.setStyleName(Reindeer.LABEL_H2);
		layout.addComponent(searchtitle);
		layout.setExpandRatio(searchtitle, 1.0f);
		layout.setComponentAlignment(searchtitle, Alignment.MIDDLE_LEFT);

		final Button createAccountBtn = new Button("Create",
				new Button.ClickListener() {
					@Override
					public void buttonClick(final ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new AccountEvent.GotoAdd(this, null));
					}
				});
		createAccountBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		createAccountBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		createAccountBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_ACCOUNT));

		UiUtils.addComponent(layout, createAccountBtn, Alignment.MIDDLE_RIGHT);

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

}
