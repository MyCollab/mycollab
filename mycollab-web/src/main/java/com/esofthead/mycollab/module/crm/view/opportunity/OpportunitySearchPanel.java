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
package com.esofthead.mycollab.module.crm.view.opportunity;

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
import com.esofthead.mycollab.module.crm.domain.Account;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.view.account.AccountSelectionField;
import com.esofthead.mycollab.module.crm.view.lead.LeadSourceListSelect;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserListSelect;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
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
public class OpportunitySearchPanel extends
		DefaultGenericSearchPanel<OpportunitySearchCriteria> {

	protected OpportunitySearchCriteria searchCriteria;

	public OpportunitySearchPanel() {
		this.searchCriteria = new OpportunitySearchCriteria();
	}

	private HorizontalLayout createSearchTopPanel() {
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(true);

		final Image titleIcon = new Image(null,
				MyCollabResource.newResource("icons/22/crm/opportunity.png"));
		layout.addComponent(titleIcon);
		layout.setComponentAlignment(titleIcon, Alignment.MIDDLE_LEFT);

		final Label searchtitle = new Label("Opportunities");
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
								new OpportunityEvent.GotoAdd(
										OpportunitySearchPanel.this, null));
					}
				});
		createAccountBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		createAccountBtn.setStyleName(UIConstants.THEME_BLUE_LINK);
		createAccountBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_OPPORTUNITY));
		UiUtils.addComponent(layout, createAccountBtn, Alignment.MIDDLE_RIGHT);

		return layout;
	}

	@SuppressWarnings("rawtypes")
	private class OpportunityBasicSearchLayout extends BasicSearchLayout {

		private static final long serialVersionUID = 1L;
		private TextField nameField;
		private CheckBox myItemCheckbox;

		@SuppressWarnings("unchecked")
		public OpportunityBasicSearchLayout() {
			super(OpportunitySearchPanel.this);
		}

		@Override
		public ComponentContainer constructHeader() {
			return OpportunitySearchPanel.this.createSearchTopPanel();
		}

		@Override
		public ComponentContainer constructBody() {
			final HorizontalLayout layout = new HorizontalLayout();
			layout.setSpacing(false);
			layout.setMargin(true);

			this.nameField = this.createSeachSupportTextField(new TextField(),
					"NameFieldOfSearch");
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
					OpportunityBasicSearchLayout.this.callSearchAction();
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
					OpportunityBasicSearchLayout.this.nameField.setValue("");
				}
			});
			UiUtils.addComponent(layout, cancelBtn, Alignment.MIDDLE_CENTER);

			final Separator separator2 = new Separator();
			UiUtils.addComponent(layout, separator2, Alignment.MIDDLE_LEFT);

			final Button advancedSearchBtn = new Button("Advanced Search",
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							OpportunitySearchPanel.this
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
			OpportunitySearchPanel.this.searchCriteria = new OpportunitySearchCriteria();
			OpportunitySearchPanel.this.searchCriteria
					.setSaccountid(new NumberSearchField(SearchField.AND,
							AppContext.getAccountId()));

			if (StringUtils.isNotNullOrEmpty(this.nameField.getValue()
					.toString().trim())) {
				OpportunitySearchPanel.this.searchCriteria
						.setOpportunityName(new StringSearchField(
								SearchField.AND, ((String) this.nameField
										.getValue()).trim()));
			}

			if (this.myItemCheckbox.getValue()) {
				OpportunitySearchPanel.this.searchCriteria
						.setAssignUsers(new SetSearchField<String>(
								SearchField.AND, new String[] { AppContext
										.getUsername() }));
			} else {
				OpportunitySearchPanel.this.searchCriteria.setAssignUsers(null);
			}

			return OpportunitySearchPanel.this.searchCriteria;
		}
	}

	private class OpportunityAdvancedSearchLayout extends
			DefaultAdvancedSearchLayout<OpportunitySearchCriteria> {

		private static final long serialVersionUID = 1L;
		private TextField opportunityNameField;
		private AccountSelectionField accountField;
		private TextField nextStepField;
		private ActiveUserListSelect userField;
		private OpportunitySalesStageListSelect stageField;
		private LeadSourceListSelect sourceField;

		@SuppressWarnings("unchecked")
		public OpportunityAdvancedSearchLayout() {
			super(OpportunitySearchPanel.this, CrmTypeConstants.OPPORTUNITY);
		}

		@Override
		public ComponentContainer constructHeader() {
			return OpportunitySearchPanel.this.createSearchTopPanel();
		}

		@Override
		public ComponentContainer constructBody() {
			GridFormLayoutHelper gridLayout = new GridFormLayoutHelper(3, 3,
					"100%", "90px");
			gridLayout.getLayout().setWidth("100%");
			gridLayout.getLayout().setMargin(
					new MarginInfo(true, true, true, false));

			this.opportunityNameField = (TextField) gridLayout.addComponent(
					new TextField(), "Name", 0, 0);
			this.accountField = (AccountSelectionField) gridLayout
					.addComponent(new AccountSelectionField(), "Account", 1, 0);
			this.nextStepField = (TextField) gridLayout.addComponent(
					new TextField(), "Next Step", 2, 0);

			this.userField = (ActiveUserListSelect) gridLayout.addComponent(
					new ActiveUserListSelect(), LocalizationHelper
							.getMessage(GenericI18Enum.FORM_ASSIGNEE_FIELD), 0,
					1);
			this.stageField = (OpportunitySalesStageListSelect) gridLayout
					.addComponent(new OpportunitySalesStageListSelect(),
							"Sales Stage", 1, 1);
			this.sourceField = (LeadSourceListSelect) gridLayout.addComponent(
					new LeadSourceListSelect(), "Lead Source", 2, 1);

			gridLayout.getLayout().setSpacing(true);
			return gridLayout.getLayout();
		}

		@Override
		protected OpportunitySearchCriteria fillupSearchCriteria() {
			OpportunitySearchPanel.this.searchCriteria = new OpportunitySearchCriteria();
			OpportunitySearchPanel.this.searchCriteria
					.setSaccountid(new NumberSearchField(SearchField.AND,
							AppContext.getAccountId()));

			if (StringUtils.isNotNullOrEmpty((String) this.opportunityNameField
					.getValue())) {
				OpportunitySearchPanel.this.searchCriteria
						.setOpportunityName(new StringSearchField(
								SearchField.AND,
								((String) this.opportunityNameField.getValue())
										.trim()));
			}

			final Account account = this.accountField.getAccount();
			if (account.getId() != null) {
				OpportunitySearchPanel.this.searchCriteria
						.setAccountId(new NumberSearchField(SearchField.AND,
								account.getId()));
			}

			if (StringUtils.isNotNullOrEmpty((String) this.nextStepField
					.getValue())) {
				OpportunitySearchPanel.this.searchCriteria
						.setNextStep(new StringSearchField(SearchField.AND,
								((String) this.nextStepField.getValue()).trim()));
			}

			final Collection<String> assignUsers = (Collection<String>) this.userField
					.getValue();
			if (assignUsers != null && assignUsers.size() > 0) {
				OpportunitySearchPanel.this.searchCriteria
						.setAssignUsers(new SetSearchField<String>(
								SearchField.AND, assignUsers));
			}

			final Collection<String> saleStages = (Collection<String>) this.stageField
					.getValue();
			if (saleStages != null && saleStages.size() > 0) {
				OpportunitySearchPanel.this.searchCriteria
						.setSalesStages(new SetSearchField<String>(
								SearchField.AND, saleStages));
			}

			final Collection<String> leadSources = (Collection<String>) this.sourceField
					.getValue();
			if (leadSources != null && leadSources.size() > 0) {
				OpportunitySearchPanel.this.searchCriteria
						.setLeadSources(new SetSearchField<String>(
								SearchField.AND, leadSources));
			}
			return OpportunitySearchPanel.this.searchCriteria;
		}

		@Override
		protected void clearFields() {
			this.opportunityNameField.setValue("");
			this.accountField.clearValue();
			this.nextStepField.setValue("");
			this.userField.setValue(null);
			this.stageField.setValue(null);
			this.sourceField.setValue(null);
		}

		@Override
		protected void loadSaveSearchToField(
				final OpportunitySearchCriteria value) {
			if (value.getOpportunityName() != null) {
				this.opportunityNameField.setValue(value.getOpportunityName()
						.getValue());
			}
			if (value.getAccountId() != null) {
				final AccountService accountService = ApplicationContextUtil
						.getSpringBean(AccountService.class);
				final SimpleAccount account = accountService.findById(
						(Integer) value.getAccountId().getValue(),
						AppContext.getAccountId());
				// this.accountField.setAccount(account);
			}
			if (value.getNextStep() != null) {
				this.nextStepField.setValue(value.getNextStep().getValue());
			}
			if (value.getAssignUsers() != null) {
				this.userField.setValue(Arrays.asList((Object[]) value
						.getAssignUsers().values));
			}
			if (value.getSalesStages() != null) {
				this.stageField.setValue(Arrays.asList((Object[]) value
						.getSalesStages().values));
			}
			if (value.getLeadSources() != null) {
				this.sourceField.setValue(Arrays.asList((Object[]) value
						.getLeadSources().values));
			}
		}

		@Override
		protected Class<OpportunitySearchCriteria> getType() {
			return OpportunitySearchCriteria.class;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected BasicSearchLayout<OpportunitySearchCriteria> createBasicSearchLayout() {
		return new OpportunityBasicSearchLayout();
	}

	@Override
	protected SearchLayout<OpportunitySearchCriteria> createAdvancedSearchLayout() {
		return new OpportunityAdvancedSearchLayout();
	}
}
