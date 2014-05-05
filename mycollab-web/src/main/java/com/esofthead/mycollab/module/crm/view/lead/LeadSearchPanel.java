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
package com.esofthead.mycollab.module.crm.view.lead;

import com.esofthead.mycollab.common.localization.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchCriteria;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.core.utils.StringUtils;
import com.esofthead.mycollab.eventmanager.EventBus;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.events.LeadEvent;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserListSelect;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.DefaultGenericSearchPanel;
import com.esofthead.mycollab.vaadin.ui.DynamicQueryParamLayout;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.esofthead.mycollab.vaadin.ui.UiUtils;
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
public class LeadSearchPanel extends
DefaultGenericSearchPanel<LeadSearchCriteria> {
	private static final long serialVersionUID = 1L;

	private static Param[] paramFields = new Param[] {
		LeadSearchCriteria.p_leadContactName,
		LeadSearchCriteria.p_accountName, LeadSearchCriteria.p_website,
		LeadSearchCriteria.p_anyEmail, LeadSearchCriteria.p_anyPhone,
		LeadSearchCriteria.p_billingCountry,
		LeadSearchCriteria.p_shippingCountry,
		LeadSearchCriteria.p_statuses, LeadSearchCriteria.p_sources,
		LeadSearchCriteria.p_assignee };

	protected LeadSearchCriteria searchCriteria;

	public LeadSearchPanel() {
		this.searchCriteria = new LeadSearchCriteria();
	}

	private HorizontalLayout createSearchTopPanel() {
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth("100%");
		layout.setSpacing(true);
		layout.setMargin(new MarginInfo(true, false, true, false));
		layout.setStyleName(UIConstants.HEADER_VIEW);

		final Image titleIcon = new Image(null,
				MyCollabResource.newResource("icons/22/crm/lead.png"));
		layout.addComponent(titleIcon);
		layout.setComponentAlignment(titleIcon, Alignment.MIDDLE_LEFT);

		final Label searchtitle = new Label("Leads");
		searchtitle.setStyleName(UIConstants.HEADER_TEXT);
		layout.addComponent(searchtitle);
		layout.setExpandRatio(searchtitle, 1.0f);
		layout.setComponentAlignment(searchtitle, Alignment.MIDDLE_LEFT);

		final Button createAccountBtn = new Button("Create Lead",
				new Button.ClickListener() {

			@Override
			public void buttonClick(final ClickEvent event) {
				EventBus.getInstance().fireEvent(
						new LeadEvent.GotoAdd(this, null));
			}
		});
		createAccountBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		createAccountBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
		createAccountBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_LEAD));
		UiUtils.addComponent(layout, createAccountBtn, Alignment.MIDDLE_RIGHT);

		return layout;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected BasicSearchLayout<LeadSearchCriteria> createBasicSearchLayout() {
		return new LeadBasicSearchLayout();
	}

	@Override
	protected SearchLayout<LeadSearchCriteria> createAdvancedSearchLayout() {
		return new LeadAdvancedSearchLayout();
	}

	@SuppressWarnings("rawtypes")
	private class LeadBasicSearchLayout extends BasicSearchLayout {
		private TextField nameField;
		private CheckBox myItemCheckbox;

		@SuppressWarnings("unchecked")
		public LeadBasicSearchLayout() {
			super(LeadSearchPanel.this);
		}

		@Override
		public ComponentContainer constructHeader() {
			return LeadSearchPanel.this.createSearchTopPanel();
		}

		@Override
		public ComponentContainer constructBody() {
			final HorizontalLayout layout = new HorizontalLayout();
			layout.setSpacing(true);
			layout.setMargin(true);

			this.nameField = this.createSeachSupportTextField(new TextField(),
					"nameFieldOfSearch");
			this.nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
			UiUtils.addComponent(layout, this.nameField,
					Alignment.MIDDLE_CENTER);

			this.myItemCheckbox = new CheckBox(
					AppContext
					.getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
			this.myItemCheckbox.setWidth("75px");
			UiUtils.addComponent(layout, this.myItemCheckbox,
					Alignment.MIDDLE_CENTER);

			final Button searchBtn = new Button("Search");
			searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			searchBtn.setIcon(MyCollabResource
					.newResource("icons/16/search.png"));

			searchBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					LeadBasicSearchLayout.this.callSearchAction();
				}
			});
			UiUtils.addComponent(layout, searchBtn, Alignment.MIDDLE_LEFT);

			final Button cancelBtn = new Button(
					AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR));
			cancelBtn.setStyleName(UIConstants.THEME_BLANK_LINK);
			cancelBtn.addStyleName("cancel-button");
			cancelBtn.addClickListener(new Button.ClickListener() {
				@Override
				public void buttonClick(final ClickEvent event) {
					LeadBasicSearchLayout.this.nameField.setValue("");
				}
			});
			UiUtils.addComponent(layout, cancelBtn, Alignment.MIDDLE_CENTER);

			final Button advancedSearchBtn = new Button(
					AppContext
					.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
					new Button.ClickListener() {

						@Override
						public void buttonClick(final ClickEvent event) {
							LeadSearchPanel.this.moveToAdvancedSearchLayout();
						}
					});
			advancedSearchBtn.setStyleName("link");
			UiUtils.addComponent(layout, advancedSearchBtn,
					Alignment.MIDDLE_CENTER);
			return layout;
		}

		@Override
		protected SearchCriteria fillupSearchCriteria() {
			LeadSearchPanel.this.searchCriteria = new LeadSearchCriteria();
			LeadSearchPanel.this.searchCriteria
			.setSaccountid(new NumberSearchField(SearchField.AND,
					AppContext.getAccountId()));

			if (StringUtils.isNotNullOrEmpty(this.nameField.getValue()
					.toString().trim())) {
				LeadSearchPanel.this.searchCriteria
				.setLeadName(new StringSearchField(SearchField.AND,
						this.nameField.getValue()));
			}

			if (this.myItemCheckbox.getValue()) {
				LeadSearchPanel.this.searchCriteria
				.setAssignUsers(new SetSearchField<String>(
						SearchField.AND, new String[] { AppContext
								.getUsername() }));
			} else {
				LeadSearchPanel.this.searchCriteria.setAssignUsers(null);
			}
			return LeadSearchPanel.this.searchCriteria;
		}
	}

	private class LeadAdvancedSearchLayout extends
	DynamicQueryParamLayout<LeadSearchCriteria> {

		public LeadAdvancedSearchLayout() {
			super(LeadSearchPanel.this, CrmTypeConstants.LEAD);
		}

		@Override
		public ComponentContainer constructHeader() {
			return LeadSearchPanel.this.createSearchTopPanel();
		}

		@Override
		public Param[] getParamFields() {
			return paramFields;
		}

		@Override
		protected Class<LeadSearchCriteria> getType() {
			return LeadSearchCriteria.class;
		}

		@Override
		protected Component buildSelectionComp(String fieldId) {
			if ("lead-assignuser".equals(fieldId)) {
				return new ActiveUserListSelect();
			}
			return null;
		}
	}
}
