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
package com.mycollab.module.crm.view.account;

import com.google.common.base.MoreObjects;
import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.AccountLead;
import com.mycollab.module.crm.domain.SimpleLead;
import com.mycollab.module.crm.domain.criteria.LeadSearchCriteria;
import com.mycollab.module.crm.i18n.LeadI18nEnum;
import com.mycollab.module.crm.i18n.OptionI18nEnum;
import com.mycollab.module.crm.i18n.OptionI18nEnum.LeadStatus;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.module.crm.service.LeadService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.RelatedListComp2;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.MyCollabUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.ConfirmDialogExt;
import com.mycollab.vaadin.web.ui.OptionPopupContent;
import com.mycollab.vaadin.web.ui.SplitButton;
import com.mycollab.vaadin.web.ui.WebUIConstants;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class AccountLeadListComp extends RelatedListComp2<LeadService, LeadSearchCriteria, SimpleLead> {
    private static final long serialVersionUID = -8793172002298632506L;

    private Account account;

    public AccountLeadListComp() {
        super(AppContextUtil.getSpringBean(LeadService.class), 20);
        this.setBlockDisplayHandler(new AccountLeadBlockDisplay());
    }

    @Override
    protected Component generateTopControls() {
        VerticalLayout controlsBtnWrap = new VerticalLayout();
        controlsBtnWrap.setWidth("100%");

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_LEAD)) {
            final SplitButton controlsBtn = new SplitButton();
            controlsBtn.addStyleName(WebUIConstants.BUTTON_ACTION);
            controlsBtn.setCaption(UserUIContext.getMessage(LeadI18nEnum.NEW));
            controlsBtn.setIcon(FontAwesome.PLUS);
            controlsBtn.addClickListener(event -> fireNewRelatedItem(""));
            MButton selectBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> {
                AccountLeadSelectionWindow leadsWindow = new AccountLeadSelectionWindow(AccountLeadListComp.this);
                LeadSearchCriteria criteria = new LeadSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                UI.getCurrent().addWindow(leadsWindow);
                leadsWindow.setSearchCriteria(criteria);
                controlsBtn.setPopupVisible(false);
            }).withIcon(CrmAssetsManager.getAsset(CrmTypeConstants.LEAD));

            OptionPopupContent buttonControlLayout = new OptionPopupContent();
            buttonControlLayout.addOption(selectBtn);
            controlsBtn.setContent(buttonControlLayout);

            controlsBtnWrap.addComponent(controlsBtn);
            controlsBtnWrap.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
        }
        return controlsBtnWrap;
    }

    void displayLeads(final Account account) {
        this.account = account;
        loadLeads();
    }

    private void loadLeads() {
        LeadSearchCriteria criteria = new LeadSearchCriteria();
        criteria.setSaccountid(NumberSearchField.equal(MyCollabUI.getAccountId()));
        criteria.setAccountId(NumberSearchField.equal(account.getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadLeads();
    }

    private class AccountLeadBlockDisplay implements BlockDisplayHandler<SimpleLead> {

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
            iconWrap.addComponent(leadAvatar);
            blockTop.addComponent(iconWrap);

            VerticalLayout leadInfo = new VerticalLayout();
            leadInfo.setSpacing(true);

            MButton btnDelete = new MButton("", clickEvent -> {
                ConfirmDialogExt.show(UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, MyCollabUI.getSiteName()),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                final AccountService accountService = AppContextUtil.getSpringBean(AccountService.class);
                                final AccountLead associateLead = new AccountLead();
                                associateLead.setAccountid(account.getId());
                                associateLead.setLeadid(lead.getId());
                                accountService.removeAccountLeadRelationship(associateLead, MyCollabUI.getAccountId());
                                AccountLeadListComp.this.refresh();
                            }
                        });
            }).withIcon(FontAwesome.TRASH_O).withStyleName(WebUIConstants.BUTTON_ICON_ONLY);
            blockContent.addComponent(btnDelete);
            blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

            Label leadName = ELabel.html(UserUIContext.getMessage(GenericI18Enum.FORM_NAME) + ": " + new A(CrmLinkGenerator.generateCrmItemLink(
                    CrmTypeConstants.LEAD, lead.getId())).appendText(lead.getLeadName()).write());

            leadInfo.addComponent(leadName);

            Label leadStatus = new Label(UserUIContext.getMessage(GenericI18Enum.FORM_STATUS) + ": " +
                    UserUIContext.getMessage(LeadStatus.class, lead.getStatus()));
            leadInfo.addComponent(leadStatus);

            String email = MoreObjects.firstNonNull(lead.getEmail(), "");
            Label leadEmail = ELabel.html(UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL) + ": " + new A("mailto:" + email).appendText(email).write());
            leadInfo.addComponent(leadEmail);

            Label leadOfficePhone = new Label(UserUIContext.getMessage(LeadI18nEnum.FORM_OFFICE_PHONE) + ": " + MoreObjects.firstNonNull(lead.getOfficephone(), ""));
            leadInfo.addComponent(leadOfficePhone);

            blockTop.with(leadInfo).expand(leadInfo);
            blockContent.addComponent(blockTop);
            blockContent.setWidth("100%");
            beanBlock.addComponent(blockContent);
            return beanBlock;
        }
    }
}
