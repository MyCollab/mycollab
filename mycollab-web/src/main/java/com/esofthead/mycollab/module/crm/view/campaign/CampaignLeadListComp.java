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
import com.esofthead.mycollab.configuration.SiteConfiguration;
import com.esofthead.mycollab.core.arguments.NumberSearchField;
import com.esofthead.mycollab.core.arguments.SearchField;
import com.esofthead.mycollab.module.crm.CrmLinkGenerator;
import com.esofthead.mycollab.module.crm.CrmTypeConstants;
import com.esofthead.mycollab.module.crm.domain.CampaignLead;
import com.esofthead.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleLead;
import com.esofthead.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.LeadI18nEnum;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.service.LeadService;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.RelatedListComp2;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.ELabel;
import com.esofthead.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.esofthead.mycollab.vaadin.web.ui.OptionPopupContent;
import com.esofthead.mycollab.vaadin.web.ui.SplitButton;
import com.esofthead.mycollab.vaadin.web.ui.UIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignLeadListComp extends RelatedListComp2<LeadService, LeadSearchCriteria, SimpleLead> {
    private static final long serialVersionUID = 1L;

    private CampaignWithBLOBs campaign;

    public CampaignLeadListComp() {
        super(AppContextUtil.getSpringBean(LeadService.class), 20);
        this.setBlockDisplayHandler(new CampaignLeadBlockDisplay());
    }

    @Override
    protected Component generateTopControls() {
        VerticalLayout controlsBtnWrap = new VerticalLayout();
        controlsBtnWrap.setWidth("100%");

        final SplitButton controlsBtn = new SplitButton();
        controlsBtn.setSizeUndefined();
        controlsBtn.setEnabled(AppContext.canWrite(RolePermissionCollections.CRM_LEAD));
        controlsBtn.addStyleName(UIConstants.BUTTON_ACTION);
        controlsBtn.setCaption(AppContext.getMessage(LeadI18nEnum.NEW));
        controlsBtn.setIcon(FontAwesome.PLUS);
        controlsBtn.addClickListener(new SplitButton.SplitButtonClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void splitButtonClick(
                    final SplitButton.SplitButtonClickEvent event) {
                fireNewRelatedItem("");
            }
        });
        final Button selectBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SELECT), new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(final ClickEvent event) {
                CampaignLeadSelectionWindow leadsWindow = new CampaignLeadSelectionWindow(CampaignLeadListComp.this);
                LeadSearchCriteria criteria = new LeadSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                UI.getCurrent().addWindow(leadsWindow);
                leadsWindow.setSearchCriteria(criteria);
                controlsBtn.setPopupVisible(false);
            }
        });
        selectBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
        OptionPopupContent buttonControlLayout = new OptionPopupContent();
        buttonControlLayout.addOption(selectBtn);
        controlsBtn.setContent(buttonControlLayout);

        controlsBtnWrap.addComponent(controlsBtn);
        controlsBtnWrap.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
        return controlsBtnWrap;
    }

    public void displayLeads(final CampaignWithBLOBs campaign) {
        this.campaign = campaign;
        loadLeads();
    }

    private void loadLeads() {
        final LeadSearchCriteria criteria = new LeadSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
        criteria.setCampaignId(new NumberSearchField(SearchField.AND, campaign.getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadLeads();
    }

    public class CampaignLeadBlockDisplay implements BlockDisplayHandler<SimpleLead> {

        @Override
        public Component generateBlock(final SimpleLead lead, int blockIndex) {
            CssLayout beanBlock = new CssLayout();
            beanBlock.addStyleName("bean-block");
            beanBlock.setWidth("350px");

            VerticalLayout blockContent = new VerticalLayout();
            MHorizontalLayout blockTop = new MHorizontalLayout();
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            ELabel leadAvatar = ELabel.fontIcon(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
            leadAvatar.addStyleName("icon-48px");
            iconWrap.addComponent(leadAvatar);
            blockTop.addComponent(iconWrap);

            VerticalLayout leadInfo = new VerticalLayout();
            leadInfo.setSpacing(true);

            MButton btnDelete = new MButton(FontAwesome.TRASH_O);
            btnDelete.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                            AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                            new ConfirmDialog.Listener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClose(ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        final CampaignService accountService = AppContextUtil.getSpringBean(CampaignService.class);
                                        final CampaignLead associateLead = new CampaignLead();
                                        associateLead.setCampaignid(campaign.getId());
                                        associateLead.setLeadid(lead.getId());
                                        accountService.removeCampaignLeadRelationship(associateLead, AppContext.getAccountId());
                                        CampaignLeadListComp.this.refresh();
                                    }
                                }
                            });
                }
            });
            btnDelete.addStyleName(UIConstants.BUTTON_ICON_ONLY);

            blockContent.addComponent(btnDelete);
            blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

            Label leadName = new Label("Name: <a href='"
                    + SiteConfiguration.getSiteUrl(AppContext.getUser()
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
