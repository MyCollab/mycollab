/**
 * Copyright © MyCollab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mycollab.module.crm.view.campaign;

import com.google.common.base.MoreObjects;
import com.hp.gagawa.java.elements.A;
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.CampaignAccount;
import com.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.mycollab.module.crm.domain.SimpleAccount;
import com.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.mycollab.module.crm.i18n.AccountI18nEnum;
import com.mycollab.module.crm.service.AccountService;
import com.mycollab.module.crm.service.CampaignService;
import com.mycollab.module.crm.ui.CrmAssetsManager;
import com.mycollab.module.crm.ui.components.RelatedListComp2;
import com.mycollab.security.RolePermissionCollections;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.AppUI;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.ui.ELabel;
import com.mycollab.vaadin.web.ui.*;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class CampaignAccountListComp extends RelatedListComp2<AccountService, AccountSearchCriteria, SimpleAccount> {
    private static final long serialVersionUID = 4624196496275152351L;

    private CampaignWithBLOBs campaign;

    public CampaignAccountListComp() {
        super(AppContextUtil.getSpringBean(AccountService.class), 20);
        setMargin(true);
        this.setBlockDisplayHandler(new CampaignAccountBlockDisplay());
    }

    @Override
    public void refresh() {
        loadAccounts();
    }

    void displayAccounts(CampaignWithBLOBs campaign) {
        this.campaign = campaign;
        loadAccounts();
    }

    private void loadAccounts() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
        criteria.setCampaignId(new NumberSearchField(campaign.getId()));
        this.setSearchCriteria(criteria);
    }

    @Override
    protected Component generateTopControls() {
        VerticalLayout controlsBtnWrap = new VerticalLayout();
        controlsBtnWrap.setWidth("100%");

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_ACCOUNT)) {
            final SplitButton controlsBtn = new SplitButton();
            controlsBtn.addStyleName(WebThemes.BUTTON_ACTION);
            controlsBtn.setCaption(UserUIContext.getMessage(AccountI18nEnum.NEW));
            controlsBtn.setIcon(FontAwesome.PLUS);
            controlsBtn.addClickListener(event -> fireNewRelatedItem(""));
            final Button selectBtn = new Button(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> {
                final CampaignAccountSelectionWindow accountsWindow = new CampaignAccountSelectionWindow(CampaignAccountListComp.this);
                final AccountSearchCriteria criteria = new AccountSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppUI.getAccountId()));
                UI.getCurrent().addWindow(accountsWindow);
                accountsWindow.setSearchCriteria(criteria);
                controlsBtn.setPopupVisible(false);
            });
            selectBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
            OptionPopupContent buttonControlLayout = new OptionPopupContent();
            buttonControlLayout.addOption(selectBtn);
            controlsBtn.setContent(buttonControlLayout);

            controlsBtnWrap.addComponent(controlsBtn);
            controlsBtnWrap.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
        }

        return controlsBtnWrap;
    }

    private class CampaignAccountBlockDisplay implements BlockDisplayHandler<SimpleAccount> {

        @Override
        public Component generateBlock(final SimpleAccount account, int blockIndex) {
            CssLayout beanBlock = new CssLayout();
            beanBlock.addStyleName("bean-block");
            beanBlock.setWidth("350px");

            VerticalLayout blockContent = new VerticalLayout();
            MHorizontalLayout blockTop = new MHorizontalLayout().withFullWidth();
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            ELabel accountAvatar = ELabel.fontIcon(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
            iconWrap.addComponent(accountAvatar);
            blockTop.addComponent(iconWrap);

            VerticalLayout accountInfo = new VerticalLayout();
            accountInfo.setSpacing(true);

            MButton btnDelete = new MButton("", clickEvent ->
                    ConfirmDialogExt.show(UI.getCurrent(),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppUI.getSiteName()),
                            UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                            UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                            confirmDialog -> {
                                if (confirmDialog.isConfirmed()) {
                                    CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
                                    CampaignAccount associateAccount = new CampaignAccount();
                                    associateAccount.setAccountid(account.getId());
                                    associateAccount.setCampaignid(campaign.getId());
                                    campaignService.removeCampaignAccountRelationship(associateAccount, AppUI.getAccountId());
                                    CampaignAccountListComp.this.refresh();
                                }
                            })
            ).withIcon(FontAwesome.TRASH_O).withStyleName(WebThemes.BUTTON_ICON_ONLY);

            blockContent.addComponent(btnDelete);
            blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

            Label accountName = ELabel.html(UserUIContext.getMessage(GenericI18Enum.FORM_NAME) + ": " + new A(CrmLinkGenerator.generateCrmItemLink(
                    CrmTypeConstants.ACCOUNT, account.getId())).appendText(account.getAccountname()).write());

            accountInfo.addComponent(accountName);

            Label accountOfficePhone = new Label(UserUIContext.getMessage(AccountI18nEnum.FORM_OFFICE_PHONE) + ": " + MoreObjects.firstNonNull(account.getPhoneoffice(), ""));
            accountInfo.addComponent(accountOfficePhone);
            String email = MoreObjects.firstNonNull(account.getEmail(), "");
            Label accountEmail = ELabel.html(UserUIContext.getMessage(GenericI18Enum.FORM_EMAIL) + ": " + new A("mailto:" + email).appendText(email).write());
            accountInfo.addComponent(accountEmail);

            Label accountCity = new Label(UserUIContext.getMessage(AccountI18nEnum.FORM_BILLING_CITY) + ": " + MoreObjects.firstNonNull(account.getCity(), ""));
            accountInfo.addComponent(accountCity);

            blockTop.with(accountInfo).expand(accountInfo);
            blockContent.addComponent(blockTop);

            blockContent.setWidth("100%");

            beanBlock.addComponent(blockContent);
            return beanBlock;
        }
    }
}
