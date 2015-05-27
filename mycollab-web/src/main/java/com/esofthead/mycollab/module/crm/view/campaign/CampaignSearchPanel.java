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
package com.esofthead.mycollab.module.crm.view.campaign;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.core.arguments.SetSearchField;
import com.esofthead.mycollab.core.arguments.StringSearchField;
import com.esofthead.mycollab.core.db.query.Param;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.events.CampaignEvent;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.module.crm.ui.components.CrmViewHeader;
import com.esofthead.mycollab.module.user.ui.components.ActiveUserListSelect;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * 
 * @author MyCollab Ltd
 * @since 1.0
 * 
 */
public class CampaignSearchPanel extends DefaultGenericSearchPanel<CampaignSearchCriteria> {
	private static final long serialVersionUID = 1L;

	private static Param[] paramFields = new Param[] {
			CampaignSearchCriteria.p_campaignName,
			CampaignSearchCriteria.p_startDate,
			CampaignSearchCriteria.p_endDate,
			CampaignSearchCriteria.p_createdtime,
			CampaignSearchCriteria.p_lastUpdatedTime,
			CampaignSearchCriteria.p_types, CampaignSearchCriteria.p_statuses,
			CampaignSearchCriteria.p_assignee };

    @Override
    protected HeaderWithFontAwesome buildSearchTitle() {
        return new CrmViewHeader(CrmTypeConstants.CAMPAIGN, AppContext.getMessage(CampaignI18nEnum.VIEW_LIST_TITLE));
    }

    @Override
    protected void buildExtraControls() {
		Button createCampaignBtn = new Button(AppContext.getMessage(CampaignI18nEnum.BUTTON_NEW_CAMPAIGN),
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        EventBusFactory.getInstance().post(new CampaignEvent.GotoAdd(this, null));

                    }
                });
        createCampaignBtn.setIcon(FontAwesome.PLUS);
        createCampaignBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
        createCampaignBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.CRM_CAMPAIGN));
        addHeaderRight(createCampaignBtn);
    }

    @Override
	protected BasicSearchLayout<CampaignSearchCriteria> createBasicSearchLayout() {
		return new CampaignBasicSearchLayout();
	}

	@Override
	protected SearchLayout<CampaignSearchCriteria> createAdvancedSearchLayout() {
		return new CampaignAdvancedSearchLayout();
	}

	private class CampaignBasicSearchLayout extends BasicSearchLayout<CampaignSearchCriteria> {
		private static final long serialVersionUID = 1L;
		private TextField nameField;
		private CheckBox myItemCheckbox;

		public CampaignBasicSearchLayout() {
			super(CampaignSearchPanel.this);
		}

		@Override
		public ComponentContainer constructHeader() {
			return CampaignSearchPanel.this.constructHeader();
		}

		@Override
		public ComponentContainer constructBody() {
			MHorizontalLayout basicSearchBody = new MHorizontalLayout().withMargin(true);

            nameField = ShortcutExtension.installShortcutAction(new TextField(),
                    new ShortcutListener("CampaignSearchRequest", ShortcutAction.KeyCode.ENTER,
                            null) {
                        @Override
                        public void handleAction(Object o, Object o1) {
                            callSearchAction();
                        }
                    });

			nameField.setWidth(UIConstants.DEFAULT_CONTROL_WIDTH);
			nameField.setInputPrompt("Query by campaign name");
			basicSearchBody.with(nameField).withAlign(nameField, Alignment.MIDDLE_CENTER);

			this.myItemCheckbox = new CheckBox(AppContext.getMessage(GenericI18Enum.SEARCH_MYITEMS_CHECKBOX));
			this.myItemCheckbox.setWidth("75px");
			basicSearchBody.with(myItemCheckbox).withAlign(myItemCheckbox, Alignment.MIDDLE_CENTER);

			Button searchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SEARCH));
			searchBtn.setStyleName(UIConstants.THEME_GREEN_LINK);
			searchBtn.setIcon(FontAwesome.SEARCH);
			searchBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					CampaignBasicSearchLayout.this.callSearchAction();
				}
			});

			basicSearchBody.with(searchBtn).withAlign(searchBtn, Alignment.MIDDLE_LEFT);

			Button cancelBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_CLEAR));
			cancelBtn.setStyleName(UIConstants.THEME_GRAY_LINK);
			cancelBtn.addClickListener(new Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(final ClickEvent event) {
					CampaignBasicSearchLayout.this.nameField.setValue("");
				}
			});
			basicSearchBody.with(cancelBtn).withAlign(cancelBtn, Alignment.MIDDLE_CENTER);

			Button advancedSearchBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_ADVANCED_SEARCH),
					new Button.ClickListener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void buttonClick(final ClickEvent event) {
							CampaignSearchPanel.this.moveToAdvancedSearchLayout();
						}
					});
			advancedSearchBtn.setStyleName("link");
			basicSearchBody.with(advancedSearchBtn).withAlign(advancedSearchBtn, Alignment.MIDDLE_CENTER);
			return basicSearchBody;
		}

		@Override
		protected CampaignSearchCriteria fillUpSearchCriteria() {
            CampaignSearchCriteria searchCriteria = new CampaignSearchCriteria();
			searchCriteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));

			if (StringUtils.isNotBlank(this.nameField.getValue().toString())) {
				searchCriteria.setCampaignName(new StringSearchField(SearchField.AND, this.nameField.getValue()));
			}

			if (this.myItemCheckbox.getValue()) {
				searchCriteria.setAssignUsers(new SetSearchField<>(
								SearchField.AND, new String[] { AppContext.getUsername() }));
			} else {
				searchCriteria.setAssignUsers(null);
			}
			return searchCriteria;
		}
	}

	private class CampaignAdvancedSearchLayout extends DynamicQueryParamLayout<CampaignSearchCriteria> {
		private static final long serialVersionUID = 1L;

		public CampaignAdvancedSearchLayout() {
			super(CampaignSearchPanel.this, CrmTypeConstants.CAMPAIGN);
		}

		@Override
		public ComponentContainer constructHeader() {
			return CampaignSearchPanel.this.constructHeader();
		}

		@Override
		public Param[] getParamFields() {
			return paramFields;
		}

		@Override
		protected Class<CampaignSearchCriteria> getType() {
			return CampaignSearchCriteria.class;
		}

		@Override
		protected Component buildSelectionComp(String fieldId) {
			if ("campaign-assignuser".equals(fieldId)) {
				return new ActiveUserListSelect();
			}
			return null;
		}
	}
}
