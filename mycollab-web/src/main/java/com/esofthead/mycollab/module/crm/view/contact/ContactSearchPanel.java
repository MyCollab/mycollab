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
package com.esofthead.mycollab.module.crm.view.contact;

import java.util.Arrays;
import java.util.Collection;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.utils.LocalizationHelper;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.esofthead.mycollab.module.crm.events.ContactEvent;
import com.esofthead.mycollab.module.crm.view.lead.LeadSourceListSelect;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserListSelect;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.CountryListSelect;
import com.esofthead.mycollab.vaadin.ui.DefaultAdvancedSearchLayout;
import com.esofthead.mycollab.vaadin.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.GridFormLayoutHelper;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.Separator;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;

@SuppressWarnings("serial")
public class ContactSearchPanel extends
		DefaultGenericSearchPanel<ContactSearchCriteria> {

	private HorizontalLayout createSearchTopPanel() {
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);

		final Image titleIcon = new Image(null,
				MyCollabResource.newResource("icons/22/crm/contact.png"));
		layout.addComponent(titleIcon);
		layout.setComponentAlignment(titleIcon, Alignment.MIDDLE_LEFT);

		final Label searchtitle = new Label("Contacts");
		searchtitle.setStyleName(Reindeer.LABEL_H2);
		layout.addComponent(searchtitle);
		layout.setExpandRatio(searchtitle, 1.0f);
		layout.setComponentAlignment(searchtitle, Alignment.MIDDLE_LEFT);

		final Button createAccountBtn = new Button("Create",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						EventBus.getInstance().fireEvent(
								new ContactEvent.GotoAdd(this, null));
					}
				});
		createAccountBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		createAccountBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		createAccountBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_CONTACT));

		UiUtils.addComponent(layout, createAccountBtn, Alignment.MIDDLE_RIGHT);

		return layout;
	}

	@SuppressWarnings("rawtypes")
	private class ContactBasicSearchLayout extends BasicSearchLayout {

		private static final long serialVersionUID = 1L;
		private TextField nameField;
		private CheckBox myItemCheckbox;

		@SuppressWarnings("unchecked")
		public ContactBasicSearchLayout() {
			super(ContactSearchPanel.this);
		}

		@Override
		public ComponentContainer constructHeader() {
			return ContactSearchPanel.this.createSearchTopPanel();
		}

		@Override
		public ComponentContainer constructBody() {
			final HorizontalLayout layout = new HorizontalLayout();
			layout.setSpacing(false);
			layout.setMargin(true);
			// layout.addComponent(new Label("Name"));
			this.nameField = this.createSeachSupportTextField(new TextField(),
					"NameFieldOfBasicSearch");
			this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
			UiUtils.addComponent(layout, this.nameField,
					Alignment.MIDDLE_CENTER);

			final Button searchBtn = new Button();
			searchBtn.setStyleName("search-icon-button");
			searchBtn.setIcon(MyCollabResource
					.newResource("icons/16/search_white.png"));

			searchBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					ContactBasicSearchLayout.this.callSearchAction();
				}
			});

			UiUtils.addComponent(layout, searchBtn, Alignment.MIDDLE_LEFT);

			this.myItemCheckbox = new CheckBox(
					LocalizationHelper
							.getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
			this.myItemCheckbox.setWidth("75px");
			UiUtils.addComponent(layout, this.myItemCheckbox,
					Alignment.MIDDLE_CENTER);

			final Separator separator1 = new Separator();

			UiUtils.addComponent(layout, separator1, Alignment.MIDDLE_LEFT);

			final Button cancelBtn = new Button(
					LocalizationHelper.getMessage(GenericI18Enum.BUTTON_CLEAR));
			cancelBtn.setStyleName(UIConstants.THEME_LINK);
			cancelBtn.addStyleName("cancel-button");
			cancelBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					ContactBasicSearchLayout.this.nameField.setValue("");
				}
			});
			UiUtils.addComponent(layout, cancelBtn, Alignment.MIDDLE_CENTER);

			final Separator separator2 = new Separator();

			UiUtils.addComponent(layout, separator2, Alignment.MIDDLE_LEFT);

			final Button advancedSearchBtn = new Button(
					LocalizationHelper
							.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							ContactSearchPanel.this
									.moveToAdvancedSearchLayout();
						}
					});
			advancedSearchBtn.setStyleName("link");
			UiUtils.addComponent(layout, advancedSearchBtn,
					Alignment.MIDDLE_CENTER);
			return layout;
		}

		@Override
		protected SearchCriteria fillupSearchCriteria() {
			final ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
			searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
					AppContext.getAccountId()));
			if (StringUtils.isNotNullOrEmpty(this.nameField.getValue()
					.toString().trim())) {
				searchCriteria.setContactName(new StringSearchField(
						SearchField.AND, this.nameField.getValue().toString()
								.trim()));
			}

			if (this.myItemCheckbox.getValue()) {
				searchCriteria.setAssignUsers(new SetSearchField<String>(
						SearchField.AND, new String[] { AppContext
								.getUsername() }));
			} else {
				searchCriteria.setAssignUsers(null);
			}
			return searchCriteria;
		}
	}

	private class ContactAdvancedSearchLayout extends
			DefaultAdvancedSearchLayout<ContactSearchCriteria> {

		private static final long serialVersionUID = 1L;
		private TextField firstnameField;
		private TextField lastnameField;
		private TextField accountnameField;
		private ActiveUserListSelect assignUserField;
		private TextField anyEmailField;
		private TextField anyAddressField;
		private TextField stateField;
		private CountryListSelect countryField;
		private TextField anyPhoneField;
		private TextField postalCodeField;
		private TextField cityField;
		private LeadSourceListSelect leadSourceField;

		public ContactAdvancedSearchLayout() {
			super(ContactSearchPanel.this, CrmTypeConstants.CONTACT);
		}

		@Override
		public ComponentContainer constructHeader() {
			return ContactSearchPanel.this.createSearchTopPanel();
		}

		@Override
		public ComponentContainer constructBody() {
			GridFormLayoutHelper gridLayout = new GridFormLayoutHelper(3, 4,
					"100%", "90px");
			gridLayout.getLayout().setWidth("100%");
			gridLayout.getLayout().setMargin(
					new MarginInfo(true, true, true, false));

			this.firstnameField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(),
							"firstnameField"), "First Name", 0, 0);
			this.lastnameField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(),
							"lastnameField"), "Last Name", 0, 1);
			this.accountnameField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(),
							"accountnameField"), "Account Name", 0, 2);
			this.assignUserField = (ActiveUserListSelect) gridLayout
					.addComponent(
							this.createSeachSupportComboBox(new ActiveUserListSelect()),
							LocalizationHelper
									.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD),
							0, 3);

			this.anyEmailField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(),
							"anyEmailField"), "Any Email", 1, 0);
			this.anyAddressField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(),
							"anyAddressField"), "Any Address", 1, 1);
			this.stateField = (TextField) gridLayout.addComponent(
					this.createSeachSupportTextField(new TextField(),
							"stateField"), "State", 1, 2);
			this.countryField = (CountryListSelect) gridLayout.addComponent(
					this.createSeachSupportComboBox(new CountryListSelect()),
					"Country", 1, 3);

			this.anyPhoneField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(),
							"anyPhoneField"), "Any Phone", 2, 0);
			this.cityField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(), "cityField"),
					"City", 2, 1);
			this.postalCodeField = (TextField) gridLayout.addComponent(this
					.createSeachSupportTextField(new TextField(),
							"postalCodeField"), "Postal Code", 2, 2);
			this.leadSourceField = (LeadSourceListSelect) gridLayout
					.addComponent(
							this.createSeachSupportComboBox(new LeadSourceListSelect()),
							"Lead Source", 2, 3);

			gridLayout.getLayout().setSpacing(true);

			return gridLayout.getLayout();
		}

		@Override
		protected ContactSearchCriteria fillupSearchCriteria() {
			final ContactSearchCriteria searchCriteria = new ContactSearchCriteria();
			searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND,
					AppContext.getAccountId()));

			if (StringUtils.isNotNullOrEmpty((String) this.firstnameField
					.getValue())) {
				searchCriteria.setFirstname(new StringSearchField(
						SearchField.AND, ((String) this.firstnameField
								.getValue()).trim()));
			}

			if (StringUtils.isNotNullOrEmpty((String) this.lastnameField
					.getValue())) {
				searchCriteria.setLastname(new StringSearchField(
						SearchField.AND, ((String) this.lastnameField
								.getValue()).trim()));
			}

			if (StringUtils.isNotNullOrEmpty((String) this.accountnameField
					.getValue())) {
				searchCriteria.setAccountName(new StringSearchField(
						SearchField.AND, ((String) this.accountnameField
								.getValue()).trim()));
			}

			final Collection<String> assignUsers = (Collection<String>) this.assignUserField
					.getValue();
			if (assignUsers != null && assignUsers.size() > 0) {
				searchCriteria.setAssignUsers(new SetSearchField<String>(
						SearchField.AND, assignUsers));
			}

			if (StringUtils.isNotNullOrEmpty((String) this.anyEmailField
					.getValue())) {
				searchCriteria
						.setAnyEmail(new StringSearchField(SearchField.AND,
								(String) this.anyEmailField.getValue()));
			}

			if (StringUtils.isNotNullOrEmpty((String) this.anyAddressField
					.getValue())) {
				searchCriteria.setAnyAddress(new StringSearchField(
						SearchField.AND, (String) this.anyAddressField
								.getValue()));
			}

			if (StringUtils.isNotNullOrEmpty((String) this.stateField
					.getValue())) {
				searchCriteria.setAnyState(new StringSearchField(
						SearchField.AND, (String) this.stateField.getValue()));
			}

			final Collection<String> countries = (Collection<String>) this.countryField
					.getValue();
			if (countries != null && countries.size() > 0) {
				searchCriteria.setCountries(new SetSearchField<String>(
						SearchField.AND, countries));
			}

			if (StringUtils.isNotNullOrEmpty((String) this.anyPhoneField
					.getValue())) {
				searchCriteria
						.setAnyPhone(new StringSearchField(SearchField.AND,
								(String) this.anyPhoneField.getValue()));
			}

			if (StringUtils
					.isNotNullOrEmpty((String) this.cityField.getValue())) {
				searchCriteria.setAnyCity(new StringSearchField(
						SearchField.AND, (String) this.cityField.getValue()));
			}

			if (StringUtils.isNotNullOrEmpty((String) this.postalCodeField
					.getValue())) {
				searchCriteria.setAnyPostalCode(new StringSearchField(
						SearchField.AND, (String) this.postalCodeField
								.getValue()));
			}

			final Collection<String> leadSources = (Collection<String>) this.leadSourceField
					.getValue();
			if (leadSources != null && leadSources.size() > 0) {
				searchCriteria.setLeadSources(new SetSearchField<String>(
						SearchField.AND, leadSources));
			}
			return searchCriteria;
		}

		@Override
		protected void clearFields() {
			this.firstnameField.setValue("");
			this.lastnameField.setValue("");
			this.accountnameField.setValue("");
			this.assignUserField.setValue(null);
			this.anyEmailField.setValue("");
			this.anyAddressField.setValue("");
			this.stateField.setValue("");
			this.countryField.setValue(null);
			this.anyPhoneField.setValue("");
			this.postalCodeField.setValue("");
			this.cityField.setValue("");
			this.leadSourceField.setValue(null);
		}

		@Override
		protected void loadSaveSearchToField(final ContactSearchCriteria value) {
			if (value.getFirstname() != null) {
				this.firstnameField.setValue(value.getFirstname().getValue());
			}
			if (value.getLastname() != null) {
				this.lastnameField.setValue(value.getLastname().getValue());
			}
			if (value.getAccountName() != null) {
				this.accountnameField.setValue(value.getAccountName()
						.getValue());
			}
			if (value.getAnyEmail() != null) {
				this.anyEmailField.setValue(value.getAnyEmail().getValue());
			}
			if (value.getAnyAddress() != null) {
				this.anyAddressField.setValue(value.getAnyAddress().getValue());
			}
			if (value.getAnyState() != null) {
				this.stateField.setValue(value.getAnyState().getValue());
			}
			if (value.getAnyPhone() != null) {
				this.anyPhoneField.setValue(value.getAnyPhone().getValue());
			}
			if (value.getAnyPostalCode() != null) {
				this.postalCodeField.setValue(value.getAnyPostalCode()
						.getValue());
			}
			if (value.getAnyCity() != null) {
				this.cityField.setValue(value.getAnyCity().getValue());
			}
			if (value.getAssignUsers() != null) {
				final Object[] assignUser = value.getAssignUsers().values;
				this.assignUserField.setValue(Arrays.asList(assignUser));
			}
			if (value.getCountries() != null) {
				final Object[] conField = value.getCountries().values;
				this.countryField.setValue(Arrays.asList(conField));
			}

			if (value.getLeadSources() != null) {
				final Object[] leadField = value.getLeadSources().values;
				this.leadSourceField.setValue(Arrays.asList(leadField));
			}
		}

		@Override
		protected Class<ContactSearchCriteria> getType() {
			return ContactSearchCriteria.class;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected BasicSearchLayout<ContactSearchCriteria> createBasicSearchLayout() {
		return new ContactBasicSearchLayout();
	}

	@Override
	protected SearchLayout<ContactSearchCriteria> createAdvancedSearchLayout() {
		return new ContactAdvancedSearchLayout();
	}
}
