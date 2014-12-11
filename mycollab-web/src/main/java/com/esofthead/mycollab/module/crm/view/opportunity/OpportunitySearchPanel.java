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

import org.apache.commons.lang3.StringUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.criteria.OpportunitySearchCriteria;
import com.esofthead.mycollab.module.crm.events.OpportunityEvent;
import com.esofthead.mycollab.module.crm.i18n.OpportunityI18nEnum;
import com.esofthead.mycollab.module.crm.view.account.AccountSelectionField;
import com.esofthead.mycollab.module.crm.view.campaign.CampaignSelectionField;
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
public class OpportunitySearchPanel extends
		DefaultGenericSearchPanel<OpportunitySearchCriteria> {

	private static Param[] paramFields = new Param[] {
			OpportunitySearchCriteria.p_opportunityName,
			OpportunitySearchCriteria.p_account,
			OpportunitySearchCriteria.p_nextStep,
			OpportunitySearchCriteria.p_campaign,
			OpportunitySearchCriteria.p_leadSource,
			OpportunitySearchCriteria.p_saleStage,
			OpportunitySearchCriteria.p_type,
			OpportunitySearchCriteria.p_expectedcloseddate,
			OpportunitySearchCriteria.p_createdtime,
			OpportunitySearchCriteria.p_lastupdatedtime };

	protected OpportunitySearchCriteria searchCriteria;

	public OpportunitySearchPanel() {
		this.searchCriteria = new OpportunitySearchCriteria();
	}

	private HorizontalLayout createSearchTopPanel() {
		final MHorizontalLayout layout = new MHorizontalLayout()
				.withWidth("100%").withSpacing(true)
				.withMargin(new MarginInfo(true, false, true, false))
				.withStyleName(UIConstants.HEADER_VIEW);

		final Image titleIcon = new Image(null,
				MyCollabResource.newResource("icons/22/crm/opportunity.png"));
		layout.addComponent(titleIcon);
		layout.setComponentAlignment(titleIcon, Alignment.MIDDLE_LEFT);

		final Label searchtitle = new Label(
				AppContext.getMessage(OpportunityI18nEnum.VIEW_LIST_TITLE));
		searchtitle.setStyleName(UIConstants.HEADER_TEXT);
		layout.addComponent(searchtitle);
		layout.setExpandRatio(searchtitle, 1.0f);
		layout.setComponentAlignment(searchtitle, Alignment.MIDDLE_LEFT);

		final Button createAccountBtn = new Button(
				AppContext
						.getMessage(OpportunityI18nEnum.BUTTON_NEW_OPPORTUNITY),
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						EventBusFactory.getInstance().post(
								new OpportunityEvent.GotoAdd(
										OpportunitySearchPanel.this, null));
					}
				});
		createAccountBtn.setIcon(MyCollabResource
				.newResource(WebResourceIds._16_addRecord));
		createAccountBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		createAccountBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_OPPORTUNITY));
		layout.with(createAccountBtn).withAlign(createAccountBtn,
				Alignment.MIDDLE_RIGHT);

		return layout;
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
			final MHorizontalLayout layout = new MHorizontalLayout()
					.withSpacing(true).withMargin(true);

			this.nameField = this.createSeachSupportTextField(new TextField(),
					"NameFieldOfSearch");
			this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
			layout.with(this.nameField).withAlign(nameField,
					Alignment.MIDDLE_CENTER);

			this.myItemCheckbox = new CheckBox(
					AppContext
							.getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
			this.myItemCheckbox.setWidth("75px");
			layout.with(myItemCheckbox).withAlign(myItemCheckbox,
					Alignment.MIDDLE_CENTER);

			final Button searchBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
			searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			searchBtn.setIcon(MyCollabResource
					.newResource(WebResourceIds._16_search));

			searchBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					OpportunityBasicSearchLayout.this.callSearchAction();
				}
			});
			layout.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

			final Button cancelBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR));
			cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
			cancelBtn.addStyleName("cancel-button");
			cancelBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					OpportunityBasicSearchLayout.this.nameField.setValue("");
				}
			});
			layout.with(cancelBtn)
					.withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

			final Button advancedSearchBtn = new Button(
					AppContext
							.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							OpportunitySearchPanel.this
									.moveToAdvancedSearchLayout();
						}
					});
			advancedSearchBtn.setStyleName("link");
			layout.with(advancedSearchBtn).withAlign(advancedSearchBtn,
					Alignment.MIDDLE_CENTER);
			return layout;
		}

		@Override
		protected SearchCriteria fillupSearchCriteria() {
			OpportunitySearchPanel.this.searchCriteria = new OpportunitySearchCriteria();
			OpportunitySearchPanel.this.searchCriteria
					.setSaccountid(new NumberSearchField(SearchField.AND,
							AppContext.getAccountId()));

			if (StringUtils.isNotBlank(this.nameField.getValue().toString()
					.trim())) {
				OpportunitySearchPanel.this.searchCriteria
						.setOpportunityName(new StringSearchField(
								SearchField.AND, this.nameField.getValue()
										.trim()));
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
			DynamicQueryParamLayout<OpportunitySearchCriteria> {

		public OpportunityAdvancedSearchLayout() {
			super(OpportunitySearchPanel.this, CrmTypeConstants.OPPORTUNITY);
		}

		@Override
		public ComponentContainer constructHeader() {
			return OpportunitySearchPanel.this.createSearchTopPanel();
		}

		@Override
		public Param[] getParamFields() {
			return paramFields;
		}

		@Override
		protected Class<OpportunitySearchCriteria> getType() {
			return OpportunitySearchCriteria.class;
		}

		@Override
		protected Component buildSelectionComp(String fieldId) {
			if ("opportunity-assignee".equals(fieldId)) {
				return new ActiveUserListSelect();
			} else if ("opportunity-account".equals(fieldId)) {
				return new AccountSelectionField();
			} else if ("opportunity-campaign".equals(fieldId)) {
				return new CampaignSelectionField();
			}
			return null;
		}
	}
}
