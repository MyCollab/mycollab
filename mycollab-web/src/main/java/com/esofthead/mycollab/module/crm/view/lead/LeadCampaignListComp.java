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

import org.vaadin.dialogs.ConfirmDialog;

import com.esofthead.mycollab.common.i18n.GenericI18Enum;
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.CampaignLead;
import com.esofthead.mycollab.module.crm.domain.Lead;
import com.esofthead.mycollab.module.crm.domain.SimpleCampaign;
import com.esofthead.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.ui.components.RelatedListComp2;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.ui.MyCollabResource;
import com.esofthead.mycollab.vaadin.ui.SplitButton;
import com.esofthead.mycollab.vaadin.ui.UIConstants;
import com.vaadin.event.MouseEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 *
 */
public class LeadCampaignListComp
		extends
		RelatedListComp2<CampaignService, CampaignSearchCriteria, SimpleCampaign> {
	private static final long serialVersionUID = -1891728022550632203L;
	private Lead lead;

	public LeadCampaignListComp() {
		super(ApplicationContextUtil.getSpringBean(CampaignService.class), 20);
		this.setBlockDisplayHandler(new LeadCampaignBlockDisplay());
	}

	public void displayCampaigns(Lead lead) {
		this.lead = lead;
		loadCampaigns();
	}

	private void loadCampaigns() {
		CampaignSearchCriteria criteria = new CampaignSearchCriteria();
		criteria.setSaccountid(new NumberSearchField(SearchField.AND,
				AppContext.getAccountId()));
		criteria.setLeadId(new NumberSearchField(SearchField.AND, lead.getId()));
		this.setSearchCriteria(criteria);
	}

	@Override
	public void refresh() {
		loadCampaigns();
	}

	@Override
	protected Component generateTopControls() {
		VerticalLayout controlBtnWrap = new VerticalLayout();
		controlBtnWrap.setWidth("100%");

		final SplitButton controlsBtn = new SplitButton();
		controlsBtn.setSizeUndefined();
		controlsBtn.setEnabled(AppContext
				.canWrite(RolePermissionCollections.CRM_CAMPAIGN));
		controlsBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
		controlsBtn.setCaption(AppContext
				.getMessage(CampaignI18nEnum.BUTTON_NEW_CAMPAIGN));
		controlsBtn.setIcon(MyCollabResource
				.newResource("icons/16/addRecord.png"));
		controlsBtn
				.addClickListener(new SplitButton.SplitButtonClickListener() {
					private static final long serialVersionUID = 1099580202385205069L;

					@Override
					public void splitButtonClick(
							SplitButton.SplitButtonClickEvent event) {
						fireNewRelatedItem("");
					}
				});
		Button selectBtn = new Button("Select from existing campaigns",
				new Button.ClickListener() {
					private static final long serialVersionUID = 3046728004767791528L;

					@Override
					public void buttonClick(Button.ClickEvent event) {
						LeadCampaignSelectionWindow leadsWindow = new LeadCampaignSelectionWindow(
								LeadCampaignListComp.this);
						CampaignSearchCriteria criteria = new CampaignSearchCriteria();
						criteria.setSaccountid(new NumberSearchField(AppContext
								.getAccountId()));
						UI.getCurrent().addWindow(leadsWindow);
						leadsWindow.setSearchCriteria(criteria);
						controlsBtn.setPopupVisible(false);
					}
				});
		selectBtn.setIcon(MyCollabResource.newResource("icons/16/select.png"));
		selectBtn.setStyleName("link");

		VerticalLayout buttonControlsLayout = new VerticalLayout();
		buttonControlsLayout.addComponent(selectBtn);
		controlsBtn.setContent(buttonControlsLayout);

		controlBtnWrap.addComponent(controlsBtn);
		controlBtnWrap.setComponentAlignment(controlsBtn,
				Alignment.MIDDLE_RIGHT);

		return controlBtnWrap;
	}

	protected class LeadCampaignBlockDisplay implements
			BlockDisplayHandler<SimpleCampaign> {

		@Override
		public Component generateBlock(final SimpleCampaign campaign,
				int blockIndex) {
			CssLayout beanBlock = new CssLayout();
			beanBlock.addStyleName("bean-block");
			beanBlock.setWidth("350px");

			VerticalLayout blockContent = new VerticalLayout();
			HorizontalLayout blockTop = new HorizontalLayout();
			blockTop.setSpacing(true);
			CssLayout iconWrap = new CssLayout();
			iconWrap.setStyleName("icon-wrap");
			Image campaignIcon = new Image(null,
					MyCollabResource.newResource("icons/48/crm/campaign.png"));
			iconWrap.addComponent(campaignIcon);
			blockTop.addComponent(iconWrap);

			VerticalLayout campaignInfo = new VerticalLayout();
			campaignInfo.setSpacing(true);

			Image btnDelete = new Image(null,
					MyCollabResource.newResource("icons/12/project/icon_x.png"));
			btnDelete.addClickListener(new MouseEvents.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void click(MouseEvents.ClickEvent event) {
					ConfirmDialogExt.show(
							UI.getCurrent(),
							AppContext.getMessage(
									GenericI18Enum.DIALOG_DELETE_TITLE,
									SiteConfiguration.getSiteName()),
							AppContext
									.getMessage(GenericI18Enum.DIALOG_CONFIRM_DELETE_RECORD_MESSAGE),
							AppContext
									.getMessage(GenericI18Enum.BUTTON_YES_LABEL),
							AppContext
									.getMessage(GenericI18Enum.BUTTON_NO_LABEL),
							new ConfirmDialog.Listener() {
								private static final long serialVersionUID = 1L;

								@Override
								public void onClose(ConfirmDialog dialog) {
									if (dialog.isConfirmed()) {
										CampaignService campaignService = ApplicationContextUtil
												.getSpringBean(CampaignService.class);
										CampaignLead associateLead = new CampaignLead();
										associateLead.setLeadid(lead.getId());
										associateLead.setCampaignid(campaign
												.getId());
										campaignService
												.removeCampaignLeadRelationship(
														associateLead,
														AppContext
																.getAccountId());
										LeadCampaignListComp.this.refresh();
									}
								}
							});
				}
			});
			btnDelete.addStyleName("icon-btn");

			blockContent.addComponent(btnDelete);
			blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

			Label contactName = new Label("Name: <a href='"
					+ SiteConfiguration.getSiteUrl(AppContext.getSession()
							.getSubdomain())
					+ CrmLinkGenerator.generateCrmItemLink(
							CrmTypeConstants.CAMPAIGN, campaign.getId()) + "'>"
					+ campaign.getCampaignname() + "</a>", ContentMode.HTML);

			campaignInfo.addComponent(contactName);

			Label campaignStatus = new Label(
					"Status: "
							+ (campaign.getStatus() != null ? campaign
									.getStatus() : ""));
			campaignInfo.addComponent(campaignStatus);

			Label campaignType = new Label("Type: "
					+ (campaign.getType() != null ? campaign.getType() : ""));
			campaignInfo.addComponent(campaignType);

			Label campaignEndDate = new Label(
					"End Date: "
							+ (campaign.getEnddate() != null ? AppContext
									.formatDate(campaign.getEnddate()) : ""));
			campaignInfo.addComponent(campaignEndDate);

			blockTop.addComponent(campaignInfo);
			blockTop.setExpandRatio(campaignInfo, 1.0f);
			blockTop.setWidth("100%");
			blockContent.addComponent(blockTop);

			blockContent.setWidth("100%");

			beanBlock.addComponent(blockContent);
			return beanBlock;
		}

	}

}
