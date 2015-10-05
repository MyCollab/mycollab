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
import com.esofthead.mycollab.module.crm.domain.CampaignAccount;
import com.esofthead.mycollab.module.crm.domain.CampaignWithBLOBs;
import com.esofthead.mycollab.module.crm.domain.SimpleAccount;
import com.esofthead.mycollab.module.crm.domain.criteria.AccountSearchCriteria;
import com.esofthead.mycollab.module.crm.i18n.AccountI18nEnum;
import com.esofthead.mycollab.module.crm.service.AccountService;
import com.esofthead.mycollab.module.crm.service.CampaignService;
import com.esofthead.mycollab.module.crm.ui.CrmAssetsManager;
import com.esofthead.mycollab.module.crm.ui.components.RelatedListComp2;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.ApplicationContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.ui.*;
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
public class CampaignAccountListComp extends
        RelatedListComp2<AccountService, AccountSearchCriteria, SimpleAccount> {
    private static final long serialVersionUID = 4624196496275152351L;

    private CampaignWithBLOBs campaign;

    public CampaignAccountListComp() {
        super(ApplicationContextUtil.getSpringBean(AccountService.class), 20);
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
        criteria.setSaccountid(new NumberSearchField(SearchField.AND,
                AppContext.getAccountId()));
        criteria.setCampaignId(new NumberSearchField(SearchField.AND, campaign
                .getId()));
        this.setSearchCriteria(criteria);
    }

    @Override
    protected Component generateTopControls() {
        VerticalLayout controlsBtnWrap = new VerticalLayout();
        controlsBtnWrap.setWidth("100%");
        final SplitButton controlsBtn = new SplitButton();
        controlsBtn.setSizeUndefined();
        controlsBtn.setEnabled(AppContext
                .canWrite(RolePermissionCollections.CRM_ACCOUNT));
        controlsBtn.addStyleName(UIConstants.THEME_GREEN_LINK);
        controlsBtn.setCaption(AppContext
                .getMessage(AccountI18nEnum.BUTTON_NEW_ACCOUNT));
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
        final Button selectBtn = new Button("Select from existing accounts",
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void buttonClick(final ClickEvent event) {
                        final CampaignAccountSelectionWindow accountsWindow = new CampaignAccountSelectionWindow(
                                CampaignAccountListComp.this);
                        final AccountSearchCriteria criteria = new AccountSearchCriteria();
                        criteria.setSaccountid(new NumberSearchField(AppContext
                                .getAccountId()));
                        UI.getCurrent().addWindow(accountsWindow);
                        accountsWindow.setSearchCriteria(criteria);
                        controlsBtn.setPopupVisible(false);
                    }
                });
        selectBtn.setIcon(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
        OptionPopupContent buttonControlLayout = new OptionPopupContent();
        buttonControlLayout.addOption(selectBtn);
        controlsBtn.setContent(buttonControlLayout);

        controlsBtnWrap.addComponent(controlsBtn);
        controlsBtnWrap.setComponentAlignment(controlsBtn,
                Alignment.MIDDLE_RIGHT);
        return controlsBtnWrap;
    }

    protected class CampaignAccountBlockDisplay implements
            BlockDisplayHandler<SimpleAccount> {

        @Override
        public Component generateBlock(final SimpleAccount account,
                                       int blockIndex) {
            CssLayout beanBlock = new CssLayout();
            beanBlock.addStyleName("bean-block");
            beanBlock.setWidth("350px");

            VerticalLayout blockContent = new VerticalLayout();
            MHorizontalLayout blockTop = new MHorizontalLayout().withWidth("100%");
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            ELabel accountAvatar = new ELabel(CrmAssetsManager.getAsset(CrmTypeConstants.ACCOUNT));
            iconWrap.addComponent(accountAvatar);
            blockTop.addComponent(iconWrap);

            VerticalLayout accountInfo = new VerticalLayout();
            accountInfo.setSpacing(true);

            MButton btnDelete = new MButton(FontAwesome.TRASH_O);
            btnDelete.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(ClickEvent clickEvent) {
                    ConfirmDialogExt.show(UI.getCurrent(),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE,
                                    AppContext.getSiteName()),
                            AppContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                            AppContext.getMessage(GenericI18Enum.BUTTON_YES),
                            AppContext.getMessage(GenericI18Enum.BUTTON_NO),
                            new ConfirmDialog.Listener() {
                                private static final long serialVersionUID = 1L;

                                @Override
                                public void onClose(ConfirmDialog dialog) {
                                    if (dialog.isConfirmed()) {
                                        CampaignService campaignService = ApplicationContextUtil
                                                .getSpringBean(CampaignService.class);
                                        CampaignAccount associateAccount = new CampaignAccount();
                                        associateAccount.setAccountid(account.getId());
                                        associateAccount.setCampaignid(campaign.getId());
                                        campaignService.removeCampaignAccountRelationship(
                                                associateAccount,
                                                AppContext.getAccountId());
                                        CampaignAccountListComp.this.refresh();
                                    }
                                }
                            });
                }
            });
            btnDelete.addStyleName(UIConstants.BUTTON_ICON_ONLY);

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
