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
package com.mycollab.module.crm.view.opportunity;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Opportunity;
import com.mycollab.module.crm.domain.OpportunityLead;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.service.LeadService;
import com.mycollab.module.crm.service.OpportunityService;
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
import com.vaadin.ui.Button.ClickEvent;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class OpportunityLeadListComp extends RelatedListComp2<LeadService, LeadSearchCriteria, SimpleLead> {
    private static final long serialVersionUID = -2052696198773718949L;

    private Opportunity opportunity;

    public OpportunityLeadListComp() {
        super(AppContextUtil.getSpringBean(LeadService.class), 20);
        this.setBlockDisplayHandler(new OpportunityLeadBlockDisplay());
    }

    @Override
    protected Component generateTopControls() {
        VerticalLayout controlsBtnWrap = new VerticalLayout();
        controlsBtnWrap.setWidth("100%");

        if (AppContext.canWrite(RolePermissionCollections.CRM_LEAD)) {
            final SplitButton controlsBtn = new SplitButton();
            controlsBtn.addStyleName(UIConstants.BUTTON_ACTION);
            controlsBtn.setCaption(AppContext.getMessage(LeadI18nEnum.NEW));
            controlsBtn.setIcon(FontAwesome.PLUS);
            controlsBtn.addClickListener(event -> fireNewRelatedItem(""));
            final Button selectBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> {
                final OpportunityLeadSelectionWindow leadsWindow = new OpportunityLeadSelectionWindow(OpportunityLeadListComp.this);
                final LeadSearchCriteria criteria = new LeadSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
                UI.getCurrent().addWindow(leadsWindow);
                leadsWindow.setSearchCriteria(criteria);
                controlsBtn.setPopupVisible(false);
            });
            selectBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
            OptionPopupContent buttonControlLayout = new OptionPopupContent();
            buttonControlLayout.addOption(selectBtn);
            controlsBtn.setContent(buttonControlLayout);

            controlsBtnWrap.addComponent(controlsBtn);
            controlsBtnWrap.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
        }

        return controlsBtnWrap;
    }

    public void displayLeads(final Opportunity opportunity) {
        this.opportunity = opportunity;
        loadLeads();
    }

    private void loadLeads() {
        final LeadSearchCriteria criteria = new LeadSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
        criteria.setOpportunityId(new NumberSearchField(SearchField.AND, opportunity.getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadLeads();
    }

    public class OpportunityLeadBlockDisplay implements BlockDisplayHandler<SimpleLead> {

        @Override
        public Component generateBlock(final SimpleLead lead, int blockIndex) {
            CssLayout beanBlock = new CssLayout();
            beanBlock.addStyleName("bean-block");
            beanBlock.setWidth("350px");

            VerticalLayout blockContent = new VerticalLayout();
            MHorizontalLayout blockTop = new MHorizontalLayout().withFullWidth();
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            ELabel leadAvatar = ELabel.fontIcon(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));
            leadAvatar.addStyleName("icon-48px");
            iconWrap.addComponent(leadAvatar);
            blockTop.addComponent(iconWrap);

            VerticalLayout leadInfo = new VerticalLayout();
            leadInfo.setSpacing(true);

            MButton btnDelete = new MButton("", clickEvent -> {
                ConfirmDialogExt.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                final OpportunityService accountService = AppContextUtil.getSpringBean(OpportunityService.class);
                                final OpportunityLead associateLead = new OpportunityLead();
                                associateLead.setOpportunityid(opportunity.getId());
                                associateLead.setLeadid(lead.getId());
                                accountService.removeOpportunityLeadRelationship(associateLead, AppContext.getAccountId());
                                OpportunityLeadListComp.this.refresh();
                            }
                        });
            }).withIcon(FontAwesome.TRASH_O).withStyleName(UIConstants.BUTTON_ICON_ONLY);

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

            blockTop.with(leadInfo).expand(leadInfo);
            blockContent.addComponent(blockTop);

            blockContent.setWidth("100%");

            beanBlock.addComponent(blockContent);
            return beanBlock;
        }

    }
}
