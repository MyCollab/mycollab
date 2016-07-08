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
package com.mycollab.module.crm.view.campaign;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.configuration.SiteConfiguration;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.db.arguments.SearchField;
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
public class CampaignAccountListComp extends RelatedListComp2<AccountService, AccountSearchCriteria, SimpleAccount> {
    private static final long serialVersionUID = 4624196496275152351L;

    private CampaignWithBLOBs campaign;

    public CampaignAccountListComp() {
        super(AppContextUtil.getSpringBean(AccountService.class), 20);
        this.setBlockDisplayHandler(new CampaignAccountBlockDisplay());
    }

    @Override
    public void refresh() {
        loadAccounts();
    }

    public void displayAccounts(CampaignWithBLOBs campaign) {
        this.campaign = campaign;
        loadAccounts();
    }

    private void loadAccounts() {
        AccountSearchCriteria criteria = new AccountSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(SearchField.AND, AppContext.getAccountId()));
        criteria.setCampaignId(new NumberSearchField(SearchField.AND, campaign.getId()));
        this.setSearchCriteria(criteria);
    }

    @Override
    protected Component generateTopControls() {
        VerticalLayout controlsBtnWrap = new VerticalLayout();
        controlsBtnWrap.setWidth("100%");

        if (AppContext.canWrite(RolePermissionCollections.CRM_ACCOUNT)) {
            final SplitButton controlsBtn = new SplitButton();
            controlsBtn.addStyleName(UIConstants.BUTTON_ACTION);
            controlsBtn.setCaption(AppContext.getMessage(AccountI18nEnum.NEW));
            controlsBtn.setIcon(FontAwesome.PLUS);
            controlsBtn.addClickListener(event -> fireNewRelatedItem(""));
            final Button selectBtn = new Button(AppContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> {
                final CampaignAccountSelectionWindow accountsWindow = new CampaignAccountSelectionWindow(CampaignAccountListComp.this);
                final AccountSearchCriteria criteria = new AccountSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(AppContext.getAccountId()));
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

    protected class CampaignAccountBlockDisplay implements BlockDisplayHandler<SimpleAccount> {

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

            MButton btnDelete = new MButton("", clickEvent -> {
                ConfirmDialogExt.show(UI.getCurrent(),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, AppContext.getSiteName()),
                        AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                        AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                CampaignService campaignService = AppContextUtil.getSpringBean(CampaignService.class);
                                CampaignAccount associateAccount = new CampaignAccount();
                                associateAccount.setAccountid(account.getId());
                                associateAccount.setCampaignid(campaign.getId());
                                campaignService.removeCampaignAccountRelationship(associateAccount, AppContext.getAccountId());
                                CampaignAccountListComp.this.refresh();
                            }
                        });
            }).withIcon(FontAwesome.TRASH_O).withStyleName(UIConstants.BUTTON_ICON_ONLY);

            blockContent.addComponent(btnDelete);
            blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

            Label accountName = new Label("Name: <a href='"
                    + SiteConfiguration.getSiteUrl(AppContext.getUser()
                    .getSubdomain())
                    + CrmLinkGenerator.generateCrmItemLink(
                    CrmTypeConstants.ACCOUNT, account.getId()) + "'>"
                    + account.getAccountname() + "</a>", ContentMode.HTML);

            accountInfo.addComponent(accountName);

            Label accountOfficePhone = new Label(
                    "Office Phone: "
                            + (account.getPhoneoffice() != null ? account
                            .getPhoneoffice() : ""));
            accountInfo.addComponent(accountOfficePhone);
            Label accountEmail = new Label("Email: "
                    + (account.getEmail() != null ? "<a href='mailto:"
                    + account.getEmail() + "'>" + account.getEmail()
                    + "</a>" : ""), ContentMode.HTML);
            accountInfo.addComponent(accountEmail);

            Label accountCity = new Label("City: "
                    + (account.getCity() != null ? account.getCity() : ""));
            accountInfo.addComponent(accountCity);

            blockTop.with(accountInfo).expand(accountInfo);
            blockContent.addComponent(blockTop);

            blockContent.setWidth("100%");

            beanBlock.addComponent(blockContent);
            return beanBlock;
        }

    }
}
