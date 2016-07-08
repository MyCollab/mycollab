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
package com.mycollab.module.crm.view.lead;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.CampaignLead;
import com.mycollab.module.crm.domain.Lead;
import com.mycollab.module.crm.domain.SimpleCampaign;
import com.mycollab.module.crm.domain.criteria.CampaignSearchCriteria;
import com.mycollab.module.crm.i18n.CampaignI18nEnum;
import com.mycollab.module.crm.service.CampaignService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.RelatedListComp2;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.SplitButton;
import com.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class LeadCampaignListComp extends RelatedListComp2<CampaignService, CampaignSearchCriteria, SimpleCampaign> {
    private static final long serialVersionUID = -1891728022550632203L;
    private Lead lead;

    public LeadCampaignListComp() {
        super(AppContextUtil.getSpringBean(CampaignService.class), 20);
        this.setBlockDisplayHandler(new LeadCampaignBlockDisplay());
    }

    public void displayCampaigns(Lead lead) {
        this.lead = lead;
        loadCampaigns();
    }

    private void loadCampaigns() {
        CampaignSearchCriteria criteria = new CampaignSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
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

        if (AppContext.canWrite(RolePermissionCollections.CRM_CAMPAIGN)) {
            final SplitButton controlsBtn = new SplitButton();
            controlsBtn.setSizeUndefined();
            controlsBtn.addStyleName(UIConstants.BUTTON_ACTION);
            controlsBtn.setCaption(AppContext.getMessage(CampaignI18nEnum.NEW));
            controlsBtn.setIcon(FontAwesome.PLUS);
            controlsBtn.addClickListener(event -> fireNewRelatedItem(""));
            Button selectBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> {
                LeadCampaignSelectionWindow leadsWindow = new LeadCampaignSelectionWindow(LeadCampaignListComp.this);
                CampaignSearchCriteria criteria = new CampaignSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                UI.getCurrent().addWindow(leadsWindow);
                leadsWindow.setSearchCriteria(criteria);
                controlsBtn.setPopupVisible(false);
            });
            selectBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN));
            OptionPopupContent buttonControlsLayout = new OptionPopupContent();
            buttonControlsLayout.addOption(selectBtn);
            controlsBtn.setContent(buttonControlsLayout);

            controlBtnWrap.addComponent(controlsBtn);
            controlBtnWrap.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
        }

        return controlBtnWrap;
    }

    protected class LeadCampaignBlockDisplay implements BlockDisplayHandler<SimpleCampaign> {

        @Override
        public Component generateBlock(final SimpleCampaign campaign, int blockIndex) {
            CssLayout beanBlock = new CssLayout();
            beanBlock.addStyleName("bean-block");
            beanBlock.setWidth("350px");

            VerticalLayout blockContent = new VerticalLayout();
            MHorizontalLayout blockTop = new MHorizontalLayout().withFullWidth();
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            ELabel campaignIcon = ELabel.fontIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CAMPAIGN));
            iconWrap.addComponent(campaignIcon);
            blockTop.addComponent(iconWrap);

            VerticalLayout campaignInfo = new VerticalLayout();
            campaignInfo.setSpacing(true);

            MButton btnDelete = new MButton("", clickEvent -> {
                ConfirmDialogExt.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
                                CampaignLead associateLead = new CampaignLead();
                                associateLead.setLeadid(lead.getId());
                                associateLead.setCampaignid(campaign.getId());
                                campaignService.removeCampaignLeadRelationship(associateLead, AppContext.getAccountId());
                                LeadCampaignListComp.this.refresh();
                            }
                        });
            }).withIcon(FontAwesome.TRASH_O).withStyleName(UIConstants.BUTTON_ICON_ONLY);

            blockContent.addComponent(btnDelete);
            blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

            Label contactName = new Label("Name: <a href='"
                    + SiteConfiguration.getSiteUrl(AppContext.getUser()
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

            ELabel campaignEndDate = new ELabel(
                    "End Date: "
                            + AppContext
                            .formatPrettyTime(campaign.getEnddate())).withDescription(AppContext.formatDate(campaign
                    .getEnddate()));
            campaignInfo.addComponent(campaignEndDate);

            blockTop.with(campaignInfo).expand(campaignInfo);
            blockContent.addComponent(blockTop);

            blockContent.setWidth("100%");

            beanBlock.addComponent(blockContent);
            return beanBlock;
        }

    }

}
