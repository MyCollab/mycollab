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
import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.crm.CrmLinkGenerator;
import com.mycollab.module.crm.CrmTypeConstants;
import com.mycollab.module.crm.domain.Account;
import com.mycollab.module.crm.domain.SimpleContact;
import com.mycollab.module.crm.domain.criteria.ContactSearchCriteria;
import com.mycollab.module.crm.i18n.ContactI18nEnum;
import com.mycollab.module.crm.service.ContactService;
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
import com.mycollab.vaadin.web.ui.WebThemes;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.layouts.MHorizontalLayout;

/**
 * @author MyCollab Ltd.
 * @since 4.0
 */
public class AccountContactListComp extends RelatedListComp2<ContactService, ContactSearchCriteria, SimpleContact> {
    private static final long serialVersionUID = -6451639268047881135L;

    private Account account;

    public AccountContactListComp() {
        super(AppContextUtil.getSpringBean(ContactService.class), 20);
        setMargin(true);
        this.setBlockDisplayHandler(new AccountContactBlockDisplay());
    }

    @Override
    protected Component generateTopControls() {
        VerticalLayout controlsBtnWrap = new VerticalLayout();
        controlsBtnWrap.setWidth("100%");

        if (UserUIContext.canWrite(RolePermissionCollections.CRM_CONTACT)) {
            final SplitButton controlsBtn = new SplitButton();
            controlsBtn.addStyleName(WebThemes.BUTTON_ACTION);
            controlsBtn.setCaption(UserUIContext.getMessage(ContactI18nEnum.NEW));
            controlsBtn.setIcon(FontAwesome.PLUS);
            controlsBtn.addClickListener(event -> fireNewRelatedItem(""));
            final MButton selectBtn = new MButton(UserUIContext.getMessage(GenericI18Enum.BUTTON_SELECT), clickEvent -> {
                final AccountContactSelectionWindow contactsWindow = new AccountContactSelectionWindow(AccountContactListComp.this);
                final ContactSearchCriteria criteria = new ContactSearchCriteria();
                criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
                UI.getCurrent().addWindow(contactsWindow);
                contactsWindow.setSearchCriteria(criteria);
                controlsBtn.setPopupVisible(false);
            }).withIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));

            OptionPopupContent buttonControlLayout = new OptionPopupContent();
            buttonControlLayout.addOption(selectBtn);
            controlsBtn.setContent(buttonControlLayout);

            controlsBtnWrap.addComponent(controlsBtn);
            controlsBtnWrap.setComponentAlignment(controlsBtn, Alignment.MIDDLE_RIGHT);
        }

        return controlsBtnWrap;
    }

    void displayContacts(Account account) {
        this.account = account;
        loadContacts();
    }

    private void loadContacts() {
        final ContactSearchCriteria criteria = new ContactSearchCriteria();
        criteria.setSaccountid(new NumberSearchField(MyCollabUI.getAccountId()));
        criteria.setAccountId(new NumberSearchField(account.getId()));
        setSearchCriteria(criteria);
    }

    @Override
    public void refresh() {
        loadContacts();
    }

    private class AccountContactBlockDisplay implements BlockDisplayHandler<SimpleContact> {

        @Override
        public Component generateBlock(final SimpleContact contact, int blockIndex) {
            CssLayout beanBlock = new CssLayout();
            beanBlock.addStyleName("bean-block");
            beanBlock.setWidth("350px");

            VerticalLayout blockContent = new VerticalLayout();
            MHorizontalLayout blockTop = new MHorizontalLayout();
            CssLayout iconWrap = new CssLayout();
            iconWrap.setStyleName("icon-wrap");
            ELabel contactAvatar = ELabel.fontIcon(CrmAssetsManager.getAsset(CrmTypeConstants.CONTACT));
            iconWrap.addComponent(contactAvatar);
            blockTop.addComponent(iconWrap);

            VerticalLayout contactInfo = new VerticalLayout();
            contactInfo.setSpacing(true);

            MButton btnDelete = new MButton("", clickEvent -> {
                ConfirmDialogExt.show(
                        UI.getCurrent(),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_TITLE, MyCollabUI.getSiteName()),
                        UserUIContext.getMessage(GenericI18Enum.DIALOG_DELETE_SINGLE_ITEM_MESSAGE),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_YES),
                        UserUIContext.getMessage(GenericI18Enum.BUTTON_NO),
                        confirmDialog -> {
                            if (confirmDialog.isConfirmed()) {
                                ContactService contactService = AppContextUtil.getSpringBean(ContactService.class);
                                contact.setAccountid(null);
                                contactService.updateWithSession(contact, UserUIContext.getUsername());
                                AccountContactListComp.this.refresh();
                            }
                        });
            }).withIcon(FontAwesome.TRASH_O).withStyleName(WebThemes.BUTTON_ICON_ONLY);

            blockContent.addComponent(btnDelete);
            blockContent.setComponentAlignment(btnDelete, Alignment.TOP_RIGHT);

            ELabel contactName = ELabel.html(String.format("Name: <a href='%s'>%s</a>", CrmLinkGenerator.generateCrmItemLink(
                    CrmTypeConstants.CONTACT, contact.getId()), contact.getContactName()));
            contactInfo.addComponent(contactName);

            Label contactTitle = new Label("Title: " + MoreObjects.firstNonNull(contact.getTitle(), ""));
            contactInfo.addComponent(contactTitle);

            Label contactEmail = new Label(String.format("Email: %s", contact.getEmail() == null ? "" :
                    String.format("<a href='mailto:%s'>%s</a>", contact.getEmail(), contact.getEmail())), ContentMode.HTML);
            contactInfo.addComponent(contactEmail);

            Label contactOfficePhone = new Label(String.format("Office Phone: %s",
                    MoreObjects.firstNonNull(contact.getOfficephone(), "")));
            contactInfo.addComponent(contactOfficePhone);

            blockTop.addComponent(contactInfo);
            blockTop.setExpandRatio(contactInfo, 1.0f);
            blockTop.setWidth("100%");
            blockContent.addComponent(blockTop);

            blockContent.setWidth("100%");

            beanBlock.addComponent(blockContent);
            return beanBlock;
        }
    }
}
