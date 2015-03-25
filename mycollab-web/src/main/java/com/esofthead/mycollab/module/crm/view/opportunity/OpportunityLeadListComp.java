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

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.Opportunity;
import com.esofthead.mycollab.module.crm.domain.OpportunityLead;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.service.OpportunityService;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.RelatedListComp2;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.FontIcon;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.layouts.MHorizontalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class OpportunityLeadListComp extends
		RelatedListComp2<LeadService, LeadSearchCriteria, SimpleLead> {
	private static final long serialVersionUID = -2052696198773718949L;

	private Opportunity opportunity;

	public OpportunityLeadListComp() {
		super(ApplicationContextUtil.getSpringBean(LeadService.class), 20);
		this.setBlockDisplayHandler(new OpportunityLeadBlockDisplay());
	}

	@Override
	protected Component generateTopControls() {
		VerticalLayout controlsBtnWrap = new VerticalLayout();
		controlsBtnWrap.setWidth("100%");
		final SplitButton controlsBtn = new SplitButton();
		controlsBtn.setSizeUndefined();
		controlsBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_LEAD));
		controlsBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
		controlsBtn.setCaption(AppContext
				.getMessage(LeadI18nEnum.BUTTON_NEW_LEAD));
		controlsBtn.setIcon(FontAwesome.PLUS);
		controlsBtn
				.addClickListener(new SplitButton.SplitButtonClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void splitButtonClick(
							final SplitButton.SplitButtonClickEvent event) {
						fireNewRelatedItem("");
					}
				});
		final Button selectBtn = new Button("Select from existing leads",
				new Button.ClickListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void buttonClick(final ClickEvent event) {
						final OpportunityLeadSelectionWindow leadsWindow = new OpportunityLeadSelectionWindow(
								OpportunityLeadListComp.this);
						final LeadSearchCriteria criteria = new LeadSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						UI.getCurrent().addWindow(leadsWindow);
						leadsWindow.setSearchCriteria(criteria);
						controlsBtn.setPopupVisible(false);
					}
				});
		selectBtn.setIcon(MyCollabResource.newResource(WebResourceIds._16_select));
		selectBtn.setStyleName("link");
		VerticalLayout buttonControlLayout = new VerticalLayout();
		buttonControlLayout.addComponent(selectBtn);
		controlsBtn.setContent(buttonControlLayout);

		controlsBtnWrap.addComponent(controlsBtn);
		controlsBtnWrap.setComponentAlignment(controlsBtn,
				Alignment.MIDDLE_RIGHT);
		return controlsBtnWrap;
	}

	public void displayLeads(final Opportunity opportunity) {
		this.opportunity = opportunity;
		loadLeads();
	}

	private void loadLeads() {
		final LeadSearchCriteria criteria = new LeadSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setOpportunityId(new NumberSearchField(SearchField.AND,
				opportunity.getId()));
		setSearchCriteria(criteria);
	}

	@Override
	public void refresh() {
		loadLeads();
	}

	public class OpportunityLeadBlockDisplay implements
			BlockDisplayHandler<SimpleLead> {

		@Override
		public Component generateBlock(final SimpleLead lead, int blockIndex) {
			CssLayout beanBlock = new CssLayout();
			beanBlock.addStyleName("bean-block");
			beanBlock.setWidth("350px");

			VerticalLayout blockContent = new VerticalLayout();
			MHorizontalLayout blockTop = new MHorizontalLayout();
			CssLayout iconWrap = new CssLayout();
			iconWrap.setStyleName("icon-wrap");
			FontIconLabel leadAvatar = new FontIconLabel(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
            leadAvatar.addStyleName("icon-48px");
			iconWrap.addComponent(leadAvatar);
			blockTop.addComponent(iconWrap);

			VerticalLayout leadInfo = new VerticalLayout();
			leadInfo.setSpacing(true);

			MButton btnDelete = new MButton(FontAwesome.TRASH_O);
            btnDelete.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    ConfirmDialogExt.show(
                            UI.getCurrent(),
                            AppContext.getMessage(
                                    GenericI18Enum.DIALOG_DELETE_TITLE,
                                    SiteConfiguration.getSiteName()),
                            AppContext
                                    .getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            AppContext
                                    .getMessage(GenericI18Enum.BUTTON_YES),
                            AppContext
                                    .getMessage(GenericI18Enum.BUTTON_NO),
                            new ConfirmDialog.Listener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClose(ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        final OpportunityService accountService = ApplicationContextUtil
                                                .getSpringBean(OpportunityService.class);
                                        final OpportunityLead associateLead = new OpportunityLead();
                                        associateLead
                                                .setOpportunityid(opportunity
                                                        .getId());
                                        associateLead.setLeadid(lead.getId());
                                        accountService
                                                .removeOpportunityLeadRelationship(
                                                        associateLead,
                                                        AppContext
                                                                .getAccountId());
                                        OpportunityLeadListComp.this.refresh();
                                    }
                                }
                            });
                }
            });
			btnDelete.addStyleName(UIConstants.BUTTON_ICON_ONLY);

			blockContent.addComponent(btnDelete);
			blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

			Label leadName = new Label("Name: <a href='"
					+ SiteConfiguration.getSiteUrl(AppContext.getSession()
							.getSubdomain())
					+ CrmLinkGenerator.generateCrmItemLink(
							CrmTypeConstants.LEAD, lead.getId()) + "'>"
					+ lead.getLeadName() + "</a>", ContentMode.HTML);

			leadInfo.addComponent(leadName);

			Label leadStatus = new Label("Status: "
					+ (lead.getStatus() != null ? lead.getStatus() : ""));
			leadInfo.addComponent(leadStatus);

			Label leadEmail = new Label("Email: "
					+ (lead.getEmail() != null ? "<a href='mailto:"
							+ lead.getEmail() + "'>" + lead.getEmail() + "</a>"
							: ""), ContentMode.HTML);
			leadInfo.addComponent(leadEmail);

			Label leadOfficePhone = new Label("Office Phone: "
					+ (lead.getOfficephone() != null ? lead.getOfficephone()
							: ""));
			leadInfo.addComponent(leadOfficePhone);

			blockTop.addComponent(leadInfo);
			blockTop.setExpandRatio(leadInfo, 1.0f);
			blockTop.setWidth("100%");
			blockContent.addComponent(blockTop);

			blockContent.setWidth("100%");

			beanBlock.addComponent(blockContent);
			return beanBlock;
		}

	}
}
